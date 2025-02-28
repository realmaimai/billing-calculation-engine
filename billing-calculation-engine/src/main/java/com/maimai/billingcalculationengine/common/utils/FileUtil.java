package com.maimai.billingcalculationengine.common.utils;

import com.maimai.billingcalculationengine.common.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
            case BLANK:
                throw new InvalidDataException("this row has a blank value");
            default:
                throw new InvalidDataException("This row has a value must be string");
        }
    }

    public static BigDecimal getCellValueAsBigDecimal(Row row, Integer columnIndex) {
        if (columnIndex == null) return BigDecimal.ZERO;

        Cell cell = row.getCell(columnIndex);
        if (cell == null) return BigDecimal.ZERO;

        if (Objects.requireNonNull(cell.getCellType()) == CellType.NUMERIC) {
            double numericValue = cell.getNumericCellValue();
            // check if the cell is formatted as a percentage
            if (cell.getCellStyle().getDataFormat() == 10 ||
                    cell.getCellStyle().getDataFormatString().contains("%")) {
                // multiply 100 to get the actual percentage value
                numericValue *= 100;
            }
            return BigDecimal.valueOf(numericValue);
        } else if (cell.getCellType() == CellType.BLANK) {
            throw new InvalidDataException("This row has a blank value");
        }
        throw new InvalidDataException("This row has a value must be number");
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
            case BLANK:
                throw new InvalidDataException("this row has a blank value");
            default:
                throw new InvalidDataException("This row has a value must be date format");

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
