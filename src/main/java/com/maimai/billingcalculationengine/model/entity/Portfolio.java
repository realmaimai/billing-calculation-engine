package com.maimai.billingcalculationengine.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    @Id
    @Column(name = "portfolio_id", length = 10)
    private String portfolioId;

    @Column(name = "client_id", nullable = false, length = 10)
    private String clientId;

    @Column(name = "portfolio_currency", nullable = false, length = 3)
    private String portfolioCurrency;
}