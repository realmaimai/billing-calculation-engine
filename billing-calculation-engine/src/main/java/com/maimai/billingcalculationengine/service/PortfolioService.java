package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.annotations.TrackExecution;
import com.maimai.billingcalculationengine.common.enums.Layer;
import com.maimai.billingcalculationengine.common.properties.CurrencyProperties;
import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.model.response.PortfolioResponse;
import com.maimai.billingcalculationengine.repository.PortfolioRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for portfolio-related operations.
 * Provides methods for retrieving portfolio information and calculating portfolio metrics.
 */
@Slf4j
@Service
public class PortfolioService {
    @Resource
    private PortfolioRepository portfolioRepository;

    @Resource
    private CalculationService calculationService;

    /**
     * Retrieves all portfolios with their calculated financial metrics.
     *
     * @return List of PortfolioResponse objects containing portfolio information and financial metrics
     */
    @TrackExecution(Layer.SERVICE)
    public List<PortfolioResponse> getAllPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAll();
        List<PortfolioResponse> portfolioResponse = portfolios.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return portfolioResponse;
    }

    /**
     * Retrieves all portfolios for a specific client.
     *
     * @param clientId The client ID to retrieve portfolios for
     * @return List of PortfolioResponse objects for the specified client
     */
    public List<PortfolioResponse> getPortfoliosByClientId(String clientId) {
        log.info("Retrieving all portfolios for client ID: {}", clientId);

        List<Portfolio> portfolios = portfolioRepository.findAllByClientId(clientId);
        log.info("Found {} portfolios for client ID: {}", portfolios.size(), clientId);

        List<PortfolioResponse> portfolioResponses = portfolios.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return portfolioResponses;
    }

    /**
     * Converts a Portfolio entity to a PortfolioResponse DTO with calculated financial metrics.
     *
     * This method:
     * 1. Calculates the portfolio's AUM in CAD
     * 2. Calculates the portfolio's fee in CAD
     * 3. Converts the AUM back to the portfolio's native currency if needed
     *
     * @param portfolio The Portfolio entity to convert
     * @return A PortfolioResponse with calculated financial metrics
     */
    private PortfolioResponse convertToResponse(Portfolio portfolio) {
        // get converted to CAD in Aum (if asset is USD based), and fee
        BigDecimal portfolioAum = calculationService.calculatePortfolioAum(portfolio);
        BigDecimal portfolioFee = calculationService.calculatePortfolioFee(portfolioAum, portfolio);

        // converted to other currency if apply
        portfolioAum = calculationService.convertFromCadToTargetCurrency(portfolioAum, portfolio.getPortfolioCurrency());


        return PortfolioResponse.builder()
                .portfolioId(portfolio.getPortfolioId())
                .clientId(portfolio.getClientId())
                .portfolioCurrency(portfolio.getPortfolioCurrency())
                .portfolioAum(portfolioAum)
                .portfolioFee(portfolioFee)
                .build();
    }
}
