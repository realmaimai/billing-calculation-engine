package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.entity.BillingTier;
import com.maimai.billingcalculationengine.repository.BillingTierRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for billing tier operations.
 * Provides methods for retrieving and managing fee structures based on portfolio AUM.
 */
@Slf4j
@Service
public class BillingTierService {
    @Resource
    private BillingTierRepository billingTierRepository;

    /**
     * Retrieves all billing tiers from the database.
     *
     * @return List of all billing tiers
     */
    public List<BillingTier> getAllBillingTiers() {
        return billingTierRepository.findAll();
    }

}
