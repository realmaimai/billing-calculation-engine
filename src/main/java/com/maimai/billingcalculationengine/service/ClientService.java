package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.response.ClientResponse;
import com.maimai.billingcalculationengine.repository.AssetRepository;
import com.maimai.billingcalculationengine.repository.ClientRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientService {
    @Resource
    private ClientRepository clientRepository;

    public List<ClientResponse> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientResponse> clientResponses = clients.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return clientResponses;
    }

    private ClientResponse convertToResponse(Client client) {
        // TODO: get all portfolios of this client, and sum all the balance of the portfolios
        BigDecimal clientBalance = BigDecimal.valueOf(100000.00);

        return ClientResponse.builder()
                .clientId(client.getClientId())
                .clientName(client.getClientName())
                .province(client.getProvince())
                .country(client.getCountry())
                .billingTierId(client.getBillingTierId())
                .clientBalance(clientBalance)
                .build();
    }


}
