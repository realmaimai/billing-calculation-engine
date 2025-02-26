package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.entity.BillingTier;
import com.maimai.billingcalculationengine.model.response.BillingTierResponse;
import com.maimai.billingcalculationengine.service.AssetService;
import com.maimai.billingcalculationengine.service.BillingTierService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/billing-tiers")
public class BillingTierController {

    @Resource
    private BillingTierService billingTierService;

    @GetMapping
    public Result<List<BillingTierResponse>> getAllBillingTiers() {
        List<BillingTier> billingTiers = billingTierService.getAllBillingTiers();
        List<BillingTierResponse> billingTierResponses = billingTiers.stream()
                .map(this::convertToResponse)
                .toList();
        return Result.success(billingTierResponses, "Billing tiers retrieved successfully");
    }

    private BillingTierResponse convertToResponse(BillingTier billingTier) {
        return BillingTierResponse.builder()
                .tierId(billingTier.getTierId())
                .portfolioAumMin(billingTier.getPortfolioAumMin())
                .portfolioAumMax(billingTier.getPortfolioAumMax())
                .feePercentage(billingTier.getFeePercentage())
                .build();
    }

}
