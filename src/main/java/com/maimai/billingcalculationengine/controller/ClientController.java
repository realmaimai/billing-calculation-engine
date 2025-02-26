package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.response.ClientResponse;
import com.maimai.billingcalculationengine.service.ClientService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    @Resource
    private ClientService clientService;

    @GetMapping()
    public Result<List<ClientResponse>> getAllClients() {
        List<ClientResponse> clientResponses = clientService.getAllClients();

        return Result.success(clientResponses, "Clients retrieved successfully");
    }


}
