package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
