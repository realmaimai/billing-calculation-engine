package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.response.AssetResponse;
import com.maimai.billingcalculationengine.service.AssetService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    @Resource
    private AssetService assetService;

    @GetMapping
    public Result<List<Asset>> getAllAssets() {
        List<Asset> assets = assetService.getAllAssets();
        return Result.success(assets, "Assets retrieved successfully");
    }

    @GetMapping("/{portfolioId}")
    public Result<List<AssetResponse>> getAssetsByPortfolioId(@PathVariable String portfolioId) {
        List<AssetResponse> assets = assetService.getAssetsByPortfolioId(portfolioId);
        return Result.success(
                assets,
                String.format("Retrieved %d assets for portfolio ID: %s", assets.size(), portfolioId)
        );
    }
}
