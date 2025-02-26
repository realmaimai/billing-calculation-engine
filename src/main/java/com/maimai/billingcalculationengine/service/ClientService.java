package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.annotations.TrackExecution;
import com.maimai.billingcalculationengine.common.enums.Layer;
import com.maimai.billingcalculationengine.common.exception.ResourceNotFoundException;
import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.model.response.ClientResponse;
import com.maimai.billingcalculationengine.repository.ClientRepository;
import com.maimai.billingcalculationengine.repository.PortfolioRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    /**
     * Retrieves all clients with their calculated financial metrics.
     *
     * @return List of ClientResponse objects containing client information and financial metrics
     */
    @TrackExecution(Layer.SERVICE)
    public List<ClientResponse> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientResponse> clientResponses = clients.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return clientResponses;
    }

    /**
     * Converts a Client entity to a ClientResponse DTO with calculated fee information.
     *
     * @param client The Client entity to convert
     * @return A ClientResponse with all calculated financial metrics
     */
    private ClientResponse convertToResponse(Client client) {
        log.info("Converting client to response - ID: {}, Name: {}", client.getClientId(), client.getClientName());

        BigDecimal totalAum = calculationService.calculateClientTotalAum(client.getClientId());
        BigDecimal totalFee = calculationService.calculateClientTotalFee(client.getClientId());
        BigDecimal effectiveFeeRate = calculationService.calculateEffectiveFeeRate(totalFee, totalAum);

        log.info("Client {} conversion complete - Total AUM: ${} CAD, Total Fee: ${} CAD, Effective Rate: {}%",
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

    /**
     * Retrieves a single client by ID with calculated financial metrics.
     *
     * @param clientId The ID of the client to retrieve
     * @return ClientResponse containing client information and financial metrics
     * @throws ResourceNotFoundException if client not found
     */
    public ClientResponse getClientById(String clientId) {
        log.info("Retrieving client with ID: {}", clientId);

        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> {
                    log.error("Client not found with ID: {}", clientId);
                    return new ResourceNotFoundException("Client not found with ID: " + clientId);
                });

        ClientResponse response = convertToResponse(client);
        log.info("Successfully retrieved client: {}", clientId);

        return response;
    }

}
