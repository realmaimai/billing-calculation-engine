package com.maimai.billingcalculationengine.model.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryResponse {
    private BigDecimal totalAum;
    private BigDecimal totalFee;
    private Integer totalClient;
    private LocalDate updateDate;
}
