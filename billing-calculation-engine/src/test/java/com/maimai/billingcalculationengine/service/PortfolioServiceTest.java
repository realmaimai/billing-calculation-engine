package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.model.response.PortfolioResponse;
import com.maimai.billingcalculationengine.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private PortfolioService portfolioService;

    private Portfolio portfolio1;
    private Portfolio portfolio2;
    private List<Portfolio> portfolioList;
    private List<Portfolio> clientPortfolioList;

    @BeforeEach
    void setUp() {
        // Setup test data
        portfolio1 = Portfolio.builder()
                .portfolioId("P001")
                .clientId("C001")
                .portfolioCurrency("CAD")
                .build();

        portfolio2 = Portfolio.builder()
                .portfolioId("P002")
                .clientId("C001")
                .portfolioCurrency("USD")
                .build();

        Portfolio portfolio3 = Portfolio.builder()
                .portfolioId("P003")
                .clientId("C002")
                .portfolioCurrency("CAD")
                .build();

        portfolioList = Arrays.asList(portfolio1, portfolio2, portfolio3);
        clientPortfolioList = Arrays.asList(portfolio1, portfolio2);
    }

    @Test
    void testGetAllPortfolios() {
        // Arrange
        when(portfolioRepository.findAll()).thenReturn(portfolioList);

        // Setup calculation service behavior
        when(calculationService.calculatePortfolioAum(any(Portfolio.class)))
                .thenReturn(new BigDecimal("50000.00"));
        when(calculationService.calculatePortfolioFee(any(BigDecimal.class), any(Portfolio.class)))
                .thenReturn(new BigDecimal("625.00"));
        when(calculationService.convertFromCadToTargetCurrency(any(BigDecimal.class), anyString()))
                .thenAnswer(invocation -> {
                    BigDecimal amount = invocation.getArgument(0);
                    String currency = invocation.getArgument(1);
                    if (currency.equals("USD")) {
                        return amount.multiply(new BigDecimal("0.71"));
                    }
                    return amount;
                });

        // Act
        List<PortfolioResponse> result = portfolioService.getAllPortfolios();

        // Assert
        assertEquals(3, result.size(), "Should return all portfolios");
        verify(calculationService, times(3)).calculatePortfolioAum(any(Portfolio.class));
        verify(calculationService, times(3)).calculatePortfolioFee(any(BigDecimal.class), any(Portfolio.class));

        // Check first portfolio (CAD)
        assertEquals("P001", result.get(0).getPortfolioId());
        assertEquals("CAD", result.get(0).getPortfolioCurrency());
        assertEquals(0, new BigDecimal("50000.00").compareTo(result.get(0).getPortfolioAum()));
        assertEquals(0, new BigDecimal("625.00").compareTo(result.get(0).getPortfolioFee()));

        // Check second portfolio (USD)
        assertEquals("P002", result.get(1).getPortfolioId());
        assertEquals("USD", result.get(1).getPortfolioCurrency());
    }

    @Test
    void testGetPortfoliosByClientId() {
        // Arrange
        when(portfolioRepository.findAllByClientId("C001")).thenReturn(clientPortfolioList);

        when(calculationService.calculatePortfolioAum(any(Portfolio.class)))
                .thenReturn(new BigDecimal("50000.00"));
        when(calculationService.calculatePortfolioFee(any(BigDecimal.class), any(Portfolio.class)))
                .thenReturn(new BigDecimal("625.00"));
        when(calculationService.convertFromCadToTargetCurrency(any(BigDecimal.class), anyString()))
                .thenAnswer(invocation -> {
                    BigDecimal amount = invocation.getArgument(0);
                    String currency = invocation.getArgument(1);
                    if (currency.equals("USD")) {
                        return amount.multiply(new BigDecimal("0.71"));
                    }
                    return amount;
                });

        // Act
        List<PortfolioResponse> result = portfolioService.getPortfoliosByClientId("C001");

        // Assert
        assertEquals(2, result.size(), "Should return only portfolios for the specified client");
        assertEquals("P001", result.get(0).getPortfolioId());
        assertEquals("P002", result.get(1).getPortfolioId());
        assertEquals("C001", result.get(0).getClientId());
        assertEquals("C001", result.get(1).getClientId());

        verify(portfolioRepository).findAllByClientId("C001");
        verify(calculationService, times(2)).calculatePortfolioAum(any(Portfolio.class));
        verify(calculationService, times(2)).calculatePortfolioFee(any(BigDecimal.class), any(Portfolio.class));
    }

    @Test
    void testConvertToResponse_CadPortfolio() {
        // This test verifies the private convertToResponse method indirectly

        // Arrange
        when(portfolioRepository.findAll()).thenReturn(Arrays.asList(portfolio1));
        when(calculationService.calculatePortfolioAum(portfolio1)).thenReturn(new BigDecimal("50000.00"));
        when(calculationService.calculatePortfolioFee(new BigDecimal("50000.00"), portfolio1)).thenReturn(new BigDecimal("625.00"));
        when(calculationService.convertFromCadToTargetCurrency(new BigDecimal("50000.00"), "CAD")).thenReturn(new BigDecimal("50000.00"));

        // Act
        List<PortfolioResponse> result = portfolioService.getAllPortfolios();

        // Assert
        assertEquals(1, result.size());
        PortfolioResponse response = result.get(0);
        assertEquals("P001", response.getPortfolioId());
        assertEquals("C001", response.getClientId());
        assertEquals("CAD", response.getPortfolioCurrency());
        assertEquals(0, new BigDecimal("50000.00").compareTo(response.getPortfolioAum()));
        assertEquals(0, new BigDecimal("625.00").compareTo(response.getPortfolioFee()));
    }

    @Test
    void testConvertToResponse_UsdPortfolio() {
        // Arrange
        when(portfolioRepository.findAll()).thenReturn(Arrays.asList(portfolio2));
        when(calculationService.calculatePortfolioAum(portfolio2)).thenReturn(new BigDecimal("50000.00"));
        when(calculationService.calculatePortfolioFee(new BigDecimal("50000.00"), portfolio2)).thenReturn(new BigDecimal("625.00"));
        when(calculationService.convertFromCadToTargetCurrency(new BigDecimal("50000.00"), "USD")).thenReturn(new BigDecimal("35500.00"));

        // Act
        List<PortfolioResponse> result = portfolioService.getAllPortfolios();

        // Assert
        assertEquals(1, result.size());
        PortfolioResponse response = result.get(0);
        assertEquals("P002", response.getPortfolioId());
        assertEquals("USD", response.getPortfolioCurrency());
        assertEquals(0, new BigDecimal("35500.00").compareTo(response.getPortfolioAum()));
        assertEquals(0, new BigDecimal("625.00").compareTo(response.getPortfolioFee()));
    }
}