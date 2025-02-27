package com.maimai.billingcalculationengine.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotNull(message = "Date is required")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Id
    @NotBlank(message = "Portfolio ID is required")
    @Size(max = 10, message = "Portfolio ID cannot exceed 10 characters")
    @Column(name = "portfolio_id", nullable = false, length = 10)
    private String portfolioId;

    @Id
    @NotBlank(message = "Asset ID is required")
    @Size(max = 10, message = "Asset ID cannot exceed 10 characters")
    @Column(name = "asset_id", nullable = false, length = 10)
    private String assetId;

    @NotNull(message = "Asset value is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Asset value must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Asset value exceeds allowed numeric precision")
    @Column(name = "asset_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal assetValue;

    @NotBlank(message = "Currency is required")
    @Size(max = 3, message = "Currency code must be 3 characters")
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