package com.maimai.billingcalculationengine.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "app.currency")
@Data
public class CurrencyProperties {
    // currency ratio converted from CAD
    // e.g. CAD : USD = 0.71
    private double toUSD;
}
