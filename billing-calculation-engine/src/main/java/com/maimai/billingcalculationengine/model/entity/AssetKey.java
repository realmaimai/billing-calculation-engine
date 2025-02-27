package com.maimai.billingcalculationengine.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetKey implements Serializable {
    private LocalDate date;
    private String portfolioId;
    private String assetId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetKey assetsId = (AssetKey) o;
        return Objects.equals(date, assetsId.date) &&
                Objects.equals(portfolioId, assetsId.portfolioId) &&
                Objects.equals(assetId, assetsId.assetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, portfolioId, assetId);
    }
}
