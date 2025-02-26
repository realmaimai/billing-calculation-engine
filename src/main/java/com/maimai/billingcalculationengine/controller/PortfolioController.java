package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.model.response.PortfolioResponse;
import com.maimai.billingcalculationengine.service.PortfolioService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {
    @Resource
    private PortfolioService portfolioService;

    // Get all portfolios
    @GetMapping
    public Result<List<PortfolioResponse>> getAllPortfolios() {
        List<PortfolioResponse> portfolioResponses = portfolioService.getAllPortfolios();
        return Result.success(portfolioResponses, "Portfolios retrieved successfully");
    }
}
