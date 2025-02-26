package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.controller.BillingTierController;
import com.maimai.billingcalculationengine.model.entity.BillingTier;
import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.response.ClientResponse;
import com.maimai.billingcalculationengine.repository.BillingTierRepository;
import com.maimai.billingcalculationengine.repository.ClientRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class BillingTierService {
    @Resource
    private BillingTierRepository billingTierRepository;

    public List<BillingTier> getAllBillingTiers() {
        return billingTierRepository.findAll();
    }

}
