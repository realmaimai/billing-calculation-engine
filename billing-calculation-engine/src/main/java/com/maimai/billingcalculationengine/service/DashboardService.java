package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.response.dashboard.SummaryResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DashboardService {

    @Resource
    private FileUploadService fileUploadService;

    @Resource
    private ClientService clientService;


    public SummaryResponse getSummary() {
        // total aum for all client
        BigDecimal totalAumOfClient = clientService.getTotalAumOfClient();

        // total fee for all client
        BigDecimal totalFeeOfClient = clientService.getTotalFeeOfClient();

        // total client number
        Integer totalNumberOfClients = clientService.getTotalNumberOfClients();

        // get created date from file upload record
        LocalDate lastUploadDateWithCompleted = fileUploadService.getLastUploadDateWithCompleted();

        return SummaryResponse.builder()
                .totalClient(totalNumberOfClients)
                .totalAum(totalAumOfClient)
                .totalFee(totalFeeOfClient)
                .updateDate(lastUploadDateWithCompleted)
                .build();
    }
}
