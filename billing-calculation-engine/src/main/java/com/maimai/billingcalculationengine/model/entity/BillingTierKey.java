package com.maimai.billingcalculationengine.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

// Composite Key Class
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingTierKey implements Serializable {
    private String tierId;
    private BigDecimal portfolioAumMin;
    private BigDecimal portfolioAumMax;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingTierKey that = (BillingTierKey) o;
        return Objects.equals(tierId, that.tierId) &&
                Objects.equals(portfolioAumMin, that.portfolioAumMin) &&
                Objects.equals(portfolioAumMax, that.portfolioAumMax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tierId, portfolioAumMin, portfolioAumMax);
    }
}
