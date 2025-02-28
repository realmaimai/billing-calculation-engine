package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.exception.ResourceNotFoundException;
import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.response.ClientResponse;
import com.maimai.billingcalculationengine.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private ClientService clientService;

    private Client client1;
    private Client client2;
    private List<Client> clientList;

    @BeforeEach
    void setUp() {
        // Setup test data
        client1 = Client.builder()
                .clientId("C001")
                .clientName("Test Client 1")
                .province("Ontario")
                .country("Canada")
                .billingTierId("T001")
                .build();

        client2 = Client.builder()
                .clientId("C002")
                .clientName("Test Client 2")
                .province("Quebec")
                .country("Canada")
                .billingTierId("T002")
                .build();

        clientList = Arrays.asList(client1, client2);
    }

    @Test
    void testGetAllClients() {
        // Arrange
        when(clientRepository.findAll()).thenReturn(clientList);

        // Setup calculation service mocks
        when(calculationService.calculateClientTotalAum(anyString())).thenReturn(new BigDecimal("100000.00"));
        when(calculationService.calculateClientTotalFee(anyString())).thenReturn(new BigDecimal("1250.00"));
        when(calculationService.calculateEffectiveFeeRate(any(), any())).thenReturn(new BigDecimal("1.25"));

        // Act
        List<ClientResponse> result = clientService.getAllClients();

        // Assert
        assertEquals(2, result.size(), "Should return all clients");
        assertEquals("C001", result.get(0).getClientId(), "First client ID should match");
        assertEquals("Test Client 1", result.get(0).getClientName(), "First client name should match");
        assertEquals(new BigDecimal("100000.00"), result.get(0).getTotalAum(), "Total AUM should be calculated");
        assertEquals(new BigDecimal("1250.00"), result.get(0).getTotalFee(), "Total fee should be calculated");
        assertEquals(new BigDecimal("1.25"), result.get(0).getEffectiveFeeRate(), "Effective fee rate should be calculated");

        verify(calculationService, times(2)).calculateClientTotalAum(anyString());
        verify(calculationService, times(2)).calculateClientTotalFee(anyString());
        verify(calculationService, times(2)).calculateEffectiveFeeRate(any(), any());
    }

    @Test
    void testGetClientById_ExistingClient() {
        // Arrange
        when(clientRepository.findByClientId("C001")).thenReturn(Optional.of(client1));
        when(calculationService.calculateClientTotalAum("C001")).thenReturn(new BigDecimal("100000.00"));
        when(calculationService.calculateClientTotalFee("C001")).thenReturn(new BigDecimal("1250.00"));
        when(calculationService.calculateEffectiveFeeRate(any(), any())).thenReturn(new BigDecimal("1.25"));

        // Act
        ClientResponse result = clientService.getClientById("C001");

        // Assert
        assertNotNull(result, "Should return a client response");
        assertEquals("C001", result.getClientId(), "Client ID should match");
        assertEquals("Test Client 1", result.getClientName(), "Client name should match");
        assertEquals(new BigDecimal("100000.00"), result.getTotalAum(), "Total AUM should be calculated");
        assertEquals(new BigDecimal("1250.00"), result.getTotalFee(), "Total fee should be calculated");
        assertEquals(new BigDecimal("1.25"), result.getEffectiveFeeRate(), "Effective fee rate should be calculated");
    }

    @Test
    void testGetClientById_NonExistingClient() {
        // Arrange
        when(clientRepository.findByClientId("C999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientById("C999");
        }, "Should throw ResourceNotFoundException for non-existing client");
    }

    @Test
    void testGetTotalNumberOfClients() {
        // Arrange
        when(clientRepository.count()).thenReturn(2L);

        // Act
        Integer result = clientService.getTotalNumberOfClients();

        // Assert
        assertEquals(2, result, "Should return the total number of clients");
    }

    @Test
    void testGetTotalAumOfClient() {
        // Arrange
        when(clientRepository.findAll()).thenReturn(clientList);
        when(calculationService.calculateClientTotalAum("C001")).thenReturn(new BigDecimal("100000.00"));
        when(calculationService.calculateClientTotalAum("C002")).thenReturn(new BigDecimal("200000.00"));

        // Act
        BigDecimal result = clientService.getTotalAumOfClient();

        // Assert
        assertEquals(0, new BigDecimal("300000.00").compareTo(result), "Should return the sum of all client AUMs");
    }
}