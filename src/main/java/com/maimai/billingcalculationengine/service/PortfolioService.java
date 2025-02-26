package com.maimai.billingcalculationengine.service;

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

@Slf4j
@Service
public class PortfolioService {
    @Resource
    private PortfolioRepository portfolioRepository;

    @Resource
    private CalculationService calculationService;

    public List<PortfolioResponse> getAllPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAll();
        List<PortfolioResponse> portfolioResponse = portfolios.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return portfolioResponse;
    }

    private PortfolioResponse convertToResponse(Portfolio portfolio) {
        BigDecimal portfolioAum = calculationService.calculatePortfolioAum(portfolio);
        BigDecimal portfolioFee = calculationService.calculatePortfolioFee(portfolioAum, portfolio);

        return PortfolioResponse.builder()
                .portfolioId(portfolio.getPortfolioId())
                .clientId(portfolio.getClientId())
                .portfolioCurrency(portfolio.getPortfolioCurrency())
                .portfolioAum(portfolioAum)
                .portfolioFee(portfolioFee)
                .build();
    }
}
