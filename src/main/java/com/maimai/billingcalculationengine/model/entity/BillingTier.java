package com.maimai.billingcalculationengine.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "billing_tiers", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BillingTierKey.class)
public class BillingTier {

    @Id
    @Column(name = "tier_id", nullable = false, length = 10)
    private String tierId;

    @Id
    @Column(name = "portfolio_aum_min", nullable = false, precision = 15, scale = 2)
    private BigDecimal portfolioAumMin;

    @Id
    @Column(name = "portfolio_aum_max", nullable = false, precision = 15, scale = 2)
    private BigDecimal portfolioAumMax;

    @Column(name = "fee_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal feePercentage;
}

