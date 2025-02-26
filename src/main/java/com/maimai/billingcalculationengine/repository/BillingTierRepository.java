package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.BillingTier;
import com.maimai.billingcalculationengine.model.entity.BillingTierKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BillingTierRepository extends JpaRepository<BillingTier, BillingTierKey> {
    @Query("SELECT bt FROM BillingTier bt WHERE bt.tierId = :tierId AND bt.portfolioAumMin <= :balance AND bt.portfolioAumMax >= :balance")
    Optional<BillingTier> findByTierIdAndBalance(@Param("tierId") String tierId, @Param("balance") BigDecimal balance);

    Optional<BillingTier> findByTierId(String billingTierId);
}
