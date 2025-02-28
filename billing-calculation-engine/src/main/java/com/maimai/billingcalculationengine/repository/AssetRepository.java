package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.entity.AssetKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, AssetKey> {
    List<Asset> findAll();

    @Query("SELECT a FROM Asset a WHERE UPPER(a.portfolioId) = UPPER(:portfolioId)")
    List<Asset> findAllByPortfolioId(@Param("portfolioId") String portfolioId);

}
