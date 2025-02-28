package com.maimai.billingcalculationengine.model.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopClientResponseByFeeResponse {
    private String clientId;
    private String location;
    private BigDecimal AUM;
    private BigDecimal fee;
    private BigDecimal avgEffectiveRate;
}
