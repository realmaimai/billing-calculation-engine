package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.model.response.ClientResponse;
import com.maimai.billingcalculationengine.repository.ClientRepository;
import com.maimai.billingcalculationengine.repository.PortfolioRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientService {
    @Resource
    private ClientRepository clientRepository;

    @Resource
    private PortfolioRepository portfolioRepository;

    @Resource
    private CalculationService calculationService;

    public List<ClientResponse> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientResponse> clientResponses = clients.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return clientResponses;
    }

    private ClientResponse convertToResponse(Client client) {
        log.info("Starting fee calculation for client ID: {}, Name: {}", client.getClientId(), client.getClientName());
        BigDecimal totalAum = BigDecimal.ZERO;
        BigDecimal totalFee = BigDecimal.ZERO;

        List<Portfolio> portfoliosByClientId = portfolioRepository.findAllByClientId(client.getClientId());
        log.info("Found {} portfolios for client {}", portfoliosByClientId.size(), client.getClientId());
        for (Portfolio portfolio : portfoliosByClientId) {
            log.debug("Processing portfolio ID: {}", portfolio.getPortfolioId());

            BigDecimal portfolioAum = calculationService.calculatePortfolioAum(portfolio);
            log.debug("Portfolio {} AUM: ${} {}", portfolio.getPortfolioId(), portfolioAum, "CAD");
            totalAum = totalAum.add(portfolioAum);

            BigDecimal portfolioFee = calculationService.calculatePortfolioFee(portfolioAum, portfolio);
            log.debug("Portfolio {} Fee: ${} {}", portfolio.getPortfolioId(), portfolioFee, "CAD");
            totalFee = totalFee.add(portfolioFee);
        }

        // effective fee rate calculation
        BigDecimal effectiveFeeRate = BigDecimal.ZERO;
        if (totalAum.compareTo(BigDecimal.ZERO) > 0) {
            effectiveFeeRate = totalFee
                    .multiply(new BigDecimal("100"))
                    .divide(totalAum, 2, RoundingMode.HALF_UP);
        }
        log.info("Client {} calculation complete - Total AUM: ${} CAD, Total Fee: ${} CAD, Effective Rate: {}%",
                client.getClientId(), totalAum, totalFee, effectiveFeeRate);

        return ClientResponse.builder()
                .clientId(client.getClientId())
                .clientName(client.getClientName())
                .province(client.getProvince())
                .country(client.getCountry())
                .billingTierId(client.getBillingTierId())
                .totalAum(totalAum)
                .totalFee(totalFee)
                .effectiveFeeRate(effectiveFeeRate)
                .build();
    }


}
