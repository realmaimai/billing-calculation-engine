package com.maimai.billingcalculationengine.model.response;

import com.maimai.billingcalculationengine.model.entity.AssetKey;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AssetKey.class)
public class AssetResponse {
    private LocalDate date;
    private String assetId;
    private BigDecimal assetValue;
    private String currency;

}