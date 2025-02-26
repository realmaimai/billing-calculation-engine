package com.maimai.billingcalculationengine.common.utils;

import java.math.BigDecimal;

public class FeeCalculationUtil {
    private static final BigDecimal USD_TO_CAD_RATE = new BigDecimal("1.4084"); // 1/0.71 = 1.4084

    public static BigDecimal calculateFee() {
        return BigDecimal.ZERO;
    }
}
