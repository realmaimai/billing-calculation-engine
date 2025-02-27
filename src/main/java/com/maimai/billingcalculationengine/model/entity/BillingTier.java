package com.maimai.billingcalculationengine.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "billing_tiers", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BillingTierKey.class)
public class BillingTier {

    @Id
    @NotBlank(message = "Tier ID is required")
    @Size(max = 10, message = "Tier ID cannot exceed 10 characters")
    @Column(name = "tier_id", nullable = false, length = 10)
    private String tierId;

    @Id
    @NotNull(message = "Portfolio AUM Min is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Portfolio AUM Min must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Portfolio AUM Min exceeds allowed numeric precision")
    @Column(name = "portfolio_aum_min", nullable = false, precision = 15, scale = 2)
    private BigDecimal portfolioAumMin;

    @Id
    @NotNull(message = "Portfolio AUM Max is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Portfolio AUM Max must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Portfolio AUM Max exceeds allowed numeric precision")
    @Column(name = "portfolio_aum_max", nullable = false, precision = 15, scale = 2)
    private BigDecimal portfolioAumMax;

    @NotNull(message = "Fee percentage is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fee percentage must be greater than or equal to 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Fee percentage must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "Fee percentage exceeds allowed numeric precision")
    @Column(name = "fee_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal feePercentage;
}