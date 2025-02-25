package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}
