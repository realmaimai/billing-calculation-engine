package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.properties.CurrencyProperties;
import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.entity.BillingTier;
import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.repository.AssetRepository;
import com.maimai.billingcalculationengine.repository.BillingTierRepository;
import com.maimai.billingcalculationengine.repository.ClientRepository;
import com.maimai.billingcalculationengine.repository.PortfolioRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BillingTierRepository billingTierRepository;

    @Mock
    private CurrencyProperties currencyProperties;

    @InjectMocks
    private CalculationService calculationService;

    private Portfolio cadPortfolio;
    private Portfolio usdPortfolio;
    private Client client;
    private BillingTier billingTier;
    private List<Asset> assets;

    @BeforeEach
    void setUp() {
        // Setup test data
        cadPortfolio = Portfolio.builder()
                .portfolioId("P001")
                .clientId("C001")
                .portfolioCurrency("CAD")
                .build();

        usdPortfolio = Portfolio.builder()
                .portfolioId("P002")
                .clientId("C001")
                .portfolioCurrency("USD")
                .build();

        client = Client.builder()
                .clientId("C001")
                .clientName("Test Client")
                .billingTierId("T001")
                .build();

        billingTier = BillingTier.builder()
                .tierId("T001")
                .portfolioAumMin(new BigDecimal("0.00"))
                .portfolioAumMax(new BigDecimal("1000000.00"))
                .feePercentage(new BigDecimal("1.25"))
                .build();

        assets = Arrays.asList(
                Asset.builder()
                        .portfolioId("P001")
                        .assetId("A001")
                        .assetValue(new BigDecimal("10000.00"))
                        .currency("CAD")
                        .date(LocalDate.now())
                        .build(),
                Asset.builder()
                        .portfolioId("P001")
                        .assetId("A002")
                        .assetValue(new BigDecimal("15000.00"))
                        .currency("CAD")
                        .date(LocalDate.now())
                        .build()
        );

    }

    @Test
    void testCalculatePortfolioAum_CADPortfolio() {
        // Arrange
        when(assetRepository.findAllByPortfolioId("P001")).thenReturn(assets);

        // Act
        BigDecimal result = calculationService.calculatePortfolioAum(cadPortfolio);

        // Assert
        BigDecimal expected = new BigDecimal("25000.00");
        assertEquals(0, expected.compareTo(result), "Portfolio AUM should be the sum of all asset values for CAD portfolio");
    }

   @Test
    void testCalculatePortfolioFee() {
        // Arrange
        BigDecimal portfolioAum = new BigDecimal("50000.00");
        when(clientRepository.findByClientId("C001")).thenReturn(Optional.of(client));
        when(billingTierRepository.findByTierIdAndBalance("T001", portfolioAum)).thenReturn(Optional.of(billingTier));

        // Act
        BigDecimal result = calculationService.calculatePortfolioFee(portfolioAum, cadPortfolio);

        // Assert
        BigDecimal expected = new BigDecimal("625.00"); // 50000 * 0.0125 = 625
        assertEquals(0, expected.compareTo(result), "Portfolio fee should be calculated based on AUM and fee percentage");
    }

    @Test
    void testConvertFromCadToTargetCurrency_CAD() {
        // Arrange
        BigDecimal amount = new BigDecimal("1000.00");

        // Act
        BigDecimal result = calculationService.convertFromCadToTargetCurrency(amount, "CAD");

        // Assert
        assertEquals(0, amount.compareTo(result), "When target currency is CAD, no conversion should happen");
    }

    @Test
    void testConvertFromCadToTargetCurrency_USD() {
        // Arrange
        BigDecimal amount = new BigDecimal("1000.00");

        // Act
        BigDecimal result = calculationService.convertFromCadToTargetCurrency(amount, "USD");

        // Assert
        BigDecimal expected = new BigDecimal("710.00"); // 1000 * 0.71 = 710
        assertEquals(0, expected.compareTo(result), "CAD amount should be correctly converted to USD");
    }
}