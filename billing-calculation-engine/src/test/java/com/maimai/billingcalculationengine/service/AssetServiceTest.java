package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.response.AssetResponse;
import com.maimai.billingcalculationengine.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private Asset asset1;
    private Asset asset2;
    private List<Asset> assetList;
    private List<Asset> portfolioAssets;

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();

        asset1 = Asset.builder()
                .assetId("A001")
                .portfolioId("P001")
                .date(today)
                .assetValue(new BigDecimal("10000.00"))
                .currency("CAD")
                .build();

        asset2 = Asset.builder()
                .assetId("A002")
                .portfolioId("P001")
                .date(today)
                .assetValue(new BigDecimal("20000.00"))
                .currency("USD")
                .build();

        Asset asset3 = Asset.builder()
                .assetId("A003")
                .portfolioId("P002")
                .date(today)
                .assetValue(new BigDecimal("15000.00"))
                .currency("CAD")
                .build();

        assetList = Arrays.asList(asset1, asset2, asset3);
        portfolioAssets = Arrays.asList(asset1, asset2);
    }

    @Test
    void testGetAllAssets() {
        // Arrange
        when(assetRepository.findAll()).thenReturn(assetList);

        // Act
        List<Asset> result = assetService.getAllAssets();

        // Assert
        assertEquals(3, result.size(), "Should return all assets");
        assertEquals("A001", result.get(0).getAssetId(), "First asset ID should match");
        assertEquals("A002", result.get(1).getAssetId(), "Second asset ID should match");
        assertEquals("A003", result.get(2).getAssetId(), "Third asset ID should match");

        verify(assetRepository).findAll();
    }

    @Test
    void testGetAssetsByPortfolioId() {
        // Arrange
        when(assetRepository.findAllByPortfolioId("P001")).thenReturn(portfolioAssets);

        // Act
        List<AssetResponse> result = assetService.getAssetsByPortfolioId("P001");

        // Assert
        assertEquals(2, result.size(), "Should return assets for the specified portfolio");

        // Verify first asset
        AssetResponse response1 = result.get(0);
        assertEquals("A001", response1.getAssetId());
        assertEquals(0, new BigDecimal("10000.00").compareTo(response1.getAssetValue()));
        assertEquals("CAD", response1.getCurrency());
        assertNotNull(response1.getDate());

        // Verify second asset
        AssetResponse response2 = result.get(1);
        assertEquals("A002", response2.getAssetId());
        assertEquals(0, new BigDecimal("20000.00").compareTo(response2.getAssetValue()));
        assertEquals("USD", response2.getCurrency());

        verify(assetRepository).findAllByPortfolioId("P001");
    }

    @Test
    void testGetAssetsByPortfolioId_NoAssets() {
        // Arrange
        when(assetRepository.findAllByPortfolioId("P999")).thenReturn(Arrays.asList());

        // Act
        List<AssetResponse> result = assetService.getAssetsByPortfolioId("P999");

        // Assert
        assertEquals(0, result.size(), "Should return empty list for portfolio with no assets");
        verify(assetRepository).findAllByPortfolioId("P999");
    }

    @Test
    void testConvertToResponse() {
        // This test verifies the private convertToResponse method indirectly

        // Arrange
        when(assetRepository.findAllByPortfolioId("P001")).thenReturn(Arrays.asList(asset1));

        // Act
        List<AssetResponse> result = assetService.getAssetsByPortfolioId("P001");

        // Assert
        assertEquals(1, result.size());
        AssetResponse response = result.get(0);
        assertEquals(asset1.getAssetId(), response.getAssetId());
        assertEquals(asset1.getDate(), response.getDate());
        assertEquals(0, asset1.getAssetValue().compareTo(response.getAssetValue()));
        assertEquals(asset1.getCurrency(), response.getCurrency());
    }

    @Test
    void testAssetResponseMapping() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 12, 31);
        Asset testAsset = Asset.builder()
                .assetId("TEST001")
                .portfolioId("P001")
                .date(testDate)
                .assetValue(new BigDecimal("12345.67"))
                .currency("EUR")
                .build();

        when(assetRepository.findAllByPortfolioId("P001")).thenReturn(Arrays.asList(testAsset));

        // Act
        List<AssetResponse> result = assetService.getAssetsByPortfolioId("P001");

        // Assert
        assertEquals(1, result.size());
        AssetResponse response = result.get(0);
        assertEquals("TEST001", response.getAssetId());
        assertEquals(testDate, response.getDate());
        assertEquals(0, new BigDecimal("12345.67").compareTo(response.getAssetValue()));
        assertEquals("EUR", response.getCurrency());
    }
}