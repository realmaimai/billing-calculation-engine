package com.maimai.billingcalculationengine.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioResponse {
    private String portfolioId;
    private String clientId;
    private String portfolioCurrency;
    private BigDecimal portfolioAum;
    private BigDecimal portfolioFee;
}