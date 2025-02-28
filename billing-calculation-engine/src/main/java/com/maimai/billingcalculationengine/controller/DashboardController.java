package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.response.dashboard.SummaryResponse;
import com.maimai.billingcalculationengine.model.response.dashboard.TopClientResponseByFeeResponse;
import com.maimai.billingcalculationengine.service.DashboardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public Result<SummaryResponse> getSummary() {
        SummaryResponse summary = dashboardService.getSummary();
        return Result.success(summary, "fetch summary successful");
    }

    public Result<List<TopClientResponseByFeeResponse>> getTopClientByFeeResponse() {
        return null;
    }
}
