package com.maimai.billingcalculationengine.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class FileUtil {
    public static String getCellValueAsString(Row row, Integer columnIndex) {
        if (columnIndex == null) return null;

        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        return "";
                    }
                }
            default:
                return "";
        }
    }

    public static BigDecimal getCellValueAsBigDecimal(Row row, Integer columnIndex) {
        if (columnIndex == null) return BigDecimal.ZERO;

        Cell cell = row.getCell(columnIndex);
        if (cell == null) return BigDecimal.ZERO;

        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue().trim().replaceAll("[^\\d.]", ""));
                } catch (NumberFormatException e) {
                    return BigDecimal.ZERO;
                }
            case FORMULA:
                try {
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    return BigDecimal.ZERO;
                }
            default:
                return BigDecimal.ZERO;
        }
    }

    public static LocalDate getCellValueAsDate(Row row, Integer columnIndex) {
        if (columnIndex == null) return null;

        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate();
                }
                return null;
            case STRING:
                try {
                    String dateStr = cell.getStringCellValue().trim();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return LocalDate.parse(dateStr, formatter);
                } catch (Exception e) {
                    return null;
                }
            default:
                return null;
        }
    }

    public static Map<String, Integer> validateHeaders(Row headerRow, List<String> expectedColumns) {
        Map<String, Integer> columnIndexMap = new HashMap<>();

        // get actual header values and their indexes
        List<String> actualHeaders = new ArrayList<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String headerValue = cell.getStringCellValue().trim().toLowerCase();
                actualHeaders.add(headerValue);
                columnIndexMap.put(headerValue, i);
            }
        }

        // check if all expected columns are present
        for (String expectedColumn : expectedColumns) {
            if (!columnIndexMap.containsKey(expectedColumn.toLowerCase())) {
                log.error("Missing expected column: {}", expectedColumn);
                log.error("Actual headers: {}", actualHeaders);
                return null;
            }
        }

        return columnIndexMap;
    }
}
