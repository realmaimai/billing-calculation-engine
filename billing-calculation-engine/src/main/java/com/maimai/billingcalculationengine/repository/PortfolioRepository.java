package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {
    List<Portfolio> findAll();
    List<Portfolio> findAllByClientId(String clientId);
}
