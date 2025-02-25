package com.maimai.billingcalculationengine.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AssetKey.class)
public class Asset {

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Id
    @Column(name = "portfolio_id", nullable = false, length = 10)
    private String portfolioId;

    @Id
    @Column(name = "asset_id", nullable = false, length = 10)
    private String assetId;

    @Column(name = "asset_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal assetValue;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
}