package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.BillingTier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingTierRepository extends JpaRepository<BillingTier, Long> {
}
