package com.maimai.billingcalculationengine.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;
}