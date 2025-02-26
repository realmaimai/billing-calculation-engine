package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findAll();

    List<Asset> findAllByPortfolioId(String portfolioId);

}
