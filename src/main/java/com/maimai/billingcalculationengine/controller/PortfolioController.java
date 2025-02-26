package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.model.response.PortfolioResponse;
import com.maimai.billingcalculationengine.service.PortfolioService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios")
@Slf4j
public class PortfolioController {
    @Resource
    private PortfolioService portfolioService;

    /**
     * Retrieves all portfolios with their associated information.
     *
     * @return Result object containing a list of portfolio responses
     */
    @GetMapping
    public Result<List<PortfolioResponse>> getAllPortfolios() {
        List<PortfolioResponse> portfolioResponses = portfolioService.getAllPortfolios();
        log.info("Retrieved {} portfolios", portfolioResponses.size());
        return Result.success(portfolioResponses, "Portfolios retrieved successfully");
    }

    /**
     * Retrieves all portfolios associated with a specific client.
     *
     * @param clientId The ID of the client whose portfolios to retrieve
     * @return Result object containing a list of portfolio responses
     */
    @GetMapping("/client/{clientId}")
    public Result<List<PortfolioResponse>> getPortfoliosByClientId(@PathVariable String clientId) {
        List<PortfolioResponse> portfolioResponses = portfolioService.getPortfoliosByClientId(clientId);
        log.info("Retrieved {} portfolios for client with ID: {}", portfolioResponses.size(), clientId);
        return Result.success(portfolioResponses, "Client portfolios retrieved successfully");
    }
}
