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
public class BillingTierResponse {
    private String tierId;
    private BigDecimal portfolioAumMin;
    private BigDecimal portfolioAumMax;
    private BigDecimal feePercentage;
}

