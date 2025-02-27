package com.maimai.billingcalculationengine.common.enums;

public enum SheetName {
    CLIENT_BILLING("client_billing"),
    PORTFOLIO("portfolio"),
    ASSETS("assets"),
    BILLING_TIER("billing_tier");

    private final String sheetName;

    SheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public static SheetName fromString(String sheetName) {
        for (SheetName sheet : values()) {
            if (sheet.sheetName.equalsIgnoreCase(sheetName)) {
                return sheet;
            }
        }
        throw new IllegalArgumentException("Unknown sheet name: " + sheetName);
    }
}

