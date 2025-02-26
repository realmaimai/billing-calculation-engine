package com.maimai.billingcalculationengine.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "app.currency")
@Data
public class CurrencyProperties {
    // currency ratio converted to CAD
    // e.g. USD : CAD = 1.43
    private double USD;
}
