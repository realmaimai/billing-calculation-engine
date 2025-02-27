package com.maimai.billingcalculationengine.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    @Id
    @NotBlank(message = "Portfolio ID is required")
    @Size(max = 10, message = "Portfolio ID cannot exceed 10 characters")
    @Column(name = "portfolio_id", length = 10)
    private String portfolioId;

    @NotBlank(message = "Client ID is required")
    @Size(max = 10, message = "Client ID cannot exceed 10 characters")
    @Column(name = "client_id", nullable = false, length = 10)
    private String clientId;

    @NotBlank(message = "Portfolio currency is required")
    @Size(max = 3, message = "Currency code must be 3 characters")
    @Column(name = "portfolio_currency", nullable = false, length = 3)
    private String portfolioCurrency;

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