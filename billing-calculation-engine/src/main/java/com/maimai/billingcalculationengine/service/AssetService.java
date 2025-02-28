package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.annotations.TrackExecution;
import com.maimai.billingcalculationengine.common.enums.Layer;
import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.response.AssetResponse;
import com.maimai.billingcalculationengine.model.response.ClientResponse;
import com.maimai.billingcalculationengine.repository.AssetRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AssetService {
    @Resource
    private AssetRepository assetRepository;

    /**
     * Retrieves all assets in the system.
     *
     * @return List of all Asset entities
     */
    @TrackExecution(Layer.SERVICE)
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    /**
     * Retrieves assets by portfolio ID.
     *
     * @param portfolioId The ID of the portfolio
     * @return List of Asset entities associated with the specified portfolio
     */
    public List<AssetResponse> getAssetsByPortfolioId(String portfolioId) {
        log.info("Retrieving assets for portfolio ID: {}", portfolioId);

        List<Asset> assets = assetRepository.findAllByPortfolioId(portfolioId);

        List<AssetResponse> assetResponses = assets.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        log.info("Found {} assets for portfolio ID: {}", assets.size(), portfolioId);
        return assetResponses;
    }

    private AssetResponse convertToResponse(Asset asset) {
        return AssetResponse.builder()
                .date(asset.getDate())
                .assetId(asset.getAssetId())
                .currency(asset.getCurrency())
                .assetValue(asset.getAssetValue())
                .build();
    }
}
