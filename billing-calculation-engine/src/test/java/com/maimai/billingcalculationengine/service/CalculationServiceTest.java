package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.dto.ClientFeeDTO;
import com.maimai.billingcalculationengine.model.entity.*;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeeCalculationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private BillingTierRepository billingTierRepository;

    @InjectMocks
    private FeeCalculationService feeCalculationService;

    private Client testClient;
    private Portfolio testPortfolio;
    private Asset testAsset;
    private BillingTier testBillingTier;
    private LocalDate testDate;

    @BeforeEach
    public void setup() {
        testDate = LocalDate.of(2024, 12, 31);

        // Setup test client
        testClient = Client.builder()
                .clientId("C001")
                .clientName("Test Client")
                .province("Ontario")
                .country("Canada")
                .billingTierId("T1")
                .build();

        // Setup test portfolio
        testPortfolio = Portfolio.builder()
                .portfolioId("P001")
                .clientId("C001")
                .portfolioCurrency("CAD")
                .build();

        // Setup test asset
        testAsset = Asset.builder()
                .assetId("A001")
                .portfolioId("P001")
                .date(testDate)
                .assetValue(new BigDecimal("100000.00"))
                .currency("CAD")
                .build();

        // Setup test billing tier
        testBillingTier = BillingTier.builder()
                .tierId("T1")
                .portfolioAumMin(new BigDecimal("0.00"))
                .portfolioAumMax(new BigDecimal("500000.00"))
                .feePercentage(new BigDecimal("1.50"))
                .build();
    }

    @Test
    public void testCalculateFeesForAllClients() {
        // Setup
        List<Client> clients = Collections.singletonList(testClient);
        when(clientRepository.findAll()).thenReturn(clients);
        when(portfolioRepository.findAllByClientId("C001")).thenReturn(Collections.singletonList(testPortfolio));

        AssetKey assetKey = new AssetKey(testDate, "P001", "A001");
        when(assetRepository.findAllByPortfolioIdAndDate("P001", testDate)).thenReturn(Collections.singletonList(testAsset));

        BillingTierKey billingTierKey = new BillingTierKey("T1", new BigDecimal("0.00"), new BigDecimal("500000.00"));
        when(billingTierRepository.findByTierId("T1")).thenReturn(Collections.singletonList(testBillingTier));

        // Execute
        List<ClientFeeDTO> results = feeCalculationService.calculateFeesForAllClients(testDate);

        // Verify
        assertNotNull(results);
        assertEquals(1, results.size());
        ClientFeeDTO clientFee = results.get(0);
        assertEquals("C001", clientFee.getClientId());
        assertEquals("Test Client", clientFee.getClientName());
        assertEquals(new BigDecimal("100000.00"), clientFee.getTotalAumCad());
        // Fee should be 1.5% of 100000 = 1500
        assertEquals(new BigDecimal("1500.00").setScale(2), clientFee.getFeeAmountCad().setScale(2));
        assertEquals(new BigDecimal("1.50"), clientFee.getEffectiveFeeRate());
    }

    @Test
    public void testCalculateFeeWithCurrencyConversion() {
        // Setup
        // Change asset currency to USD
        Asset usdAsset = Asset.builder()
                .assetId("A002")
                .portfolioId("P001")
                .date(testDate)
                .assetValue(new BigDecimal("100000.00"))
                .currency("USD")  // USD asset
                .build();

        List<Client> clients = Collections.singletonList(testClient);
        when(clientRepository.findAll()).thenReturn(clients);
        when(portfolioRepository.findAllByClientId("C001")).thenReturn(Collections.singletonList(testPortfolio));
        when(assetRepository.findAllByPortfolioIdAndDate("P001", testDate)).thenReturn(Collections.singletonList(usdAsset));
        when(billingTierRepository.findByTierId("T1")).thenReturn(Collections.singletonList(testBillingTier));

        // Execute
        List<ClientFeeDTO> results = feeCalculationService.calculateFeesForAllClients(testDate);

        // Verify
        assertNotNull(results);
        assertEquals(1, results.size());
        ClientFeeDTO clientFee = results.get(0);

        // USD 100,000 converted to CAD at 1 USD = 1.4085 CAD (1/0.71) should be approx CAD 140,845
        BigDecimal expectedAumCad = new BigDecimal("140845.07");
        assertEquals(expectedAumCad.setScale(2), clientFee.getTotalAumCad().setScale(2));

        // Fee should be 1.5% of 140,845.07 = 2,112.68
        BigDecimal expectedFee = new BigDecimal("2112.68");
        assertEquals(expectedFee.setScale(2), clientFee.getFeeAmountCad().setScale(2));
    }

    @Test
    public void testCalculateFeeForClientWithMultiplePortfolios() {
        // Setup
        // Create second portfolio for same client
        Portfolio testPortfolio2 = Portfolio.builder()
                .portfolioId("P002")
                .clientId("C001")
                .portfolioCurrency("CAD")
                .build();

        // Create second asset for second portfolio
        Asset testAsset2 = Asset.builder()
                .assetId("A002")
                .portfolioId("P002")
                .date(testDate)
                .assetValue(new BigDecimal("200000.00"))
                .currency("CAD")
                .build();

        List<Client> clients = Collections.singletonList(testClient);
        when(clientRepository.findAll()).thenReturn(clients);
        when(portfolioRepository.findAllByClientId("C001")).thenReturn(Arrays.asList(testPortfolio, testPortfolio2));

        when(assetRepository.findAllByPortfolioIdAndDate("P001", testDate)).thenReturn(Collections.singletonList(testAsset));
        when(assetRepository.findAllByPortfolioIdAndDate("P002", testDate)).thenReturn(Collections.singletonList(testAsset2));

        // Second tier for the higher portfolio value
        BillingTier testBillingTier2 = BillingTier.builder()
                .tierId("T1")
                .portfolioAumMin(new BigDecimal("100001.00"))
                .portfolioAumMax(new BigDecimal("1000000.00"))
                .feePercentage(new BigDecimal("1.25"))  // Lower fee for higher portfolio value
                .build();

        when(billingTierRepository.findByTierId("T1")).thenReturn(Arrays.asList(testBillingTier, testBillingTier2));

        // Execute
        List<ClientFeeDTO> results = feeCalculationService.calculateFeesForAllClients(testDate);

        // Verify
        assertNotNull(results);
        assertEquals(1, results.size());
        ClientFeeDTO clientFee = results.get(0);

        // Total AUM should be 100,000 + 200,000 = 300,000
        assertEquals(new BigDecimal("300000.00"), clientFee.getTotalAumCad());

        // P001: 100,000 at 1.5% = 1,500
        // P002: 200,000 at 1.25% = 2,500
        // Total fee: 4,000
        assertEquals(new BigDecimal("4000.00").setScale(2), clientFee.getFeeAmountCad().setScale(2));

        // Effective rate: 4,000 / 300,000 = 1.33%
        assertEquals(new BigDecimal("1.33").setScale(2), clientFee.getEffectiveFeeRate().setScale(2));
    }
}