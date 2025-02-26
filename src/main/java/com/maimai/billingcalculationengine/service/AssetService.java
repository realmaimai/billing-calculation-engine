package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.repository.AssetRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AssetService {
    @Resource
    private AssetRepository assetRepository;

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }
}
