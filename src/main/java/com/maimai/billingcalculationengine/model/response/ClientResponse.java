package com.maimai.billingcalculationengine.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse {
    private String clientId;
    private String clientName;
    private String province;
    private String country;
    private String billingTierId;
    private BigDecimal totalAum;
    private BigDecimal totalFee;
    private BigDecimal effectiveFeeRate;
}