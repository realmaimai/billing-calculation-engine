package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.BaseContext;
import com.maimai.billingcalculationengine.model.entity.FileUploadRecord;
import com.maimai.billingcalculationengine.repository.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FileUploadService {
    @Resource
    private FileUploadRepository fileUploadRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private PortfolioRepository portfolioRepository;

    @Resource
    private AssetRepository assetRepository;

    @Resource
    private BillingTierRepository billingTierRepository;

    private static final Map<String, List<String>> EXPECTED_SHEETS_AND_COLUMNS = new HashMap<>();

    // store sheet names and column headers into a hashmap
    static {
        EXPECTED_SHEETS_AND_COLUMNS.put("client_billing", Arrays.asList(
                "client_id", "client_name", "province", "country", "billing_tier_id"
        ));

        EXPECTED_SHEETS_AND_COLUMNS.put("portfolio", Arrays.asList(
                "client_id", "portfolio_id", "portfolio_currency"
        ));

        EXPECTED_SHEETS_AND_COLUMNS.put("assets", Arrays.asList(
                "asset_id", "portfolio_id", "asset_value", "currency", "date"
        ));

        EXPECTED_SHEETS_AND_COLUMNS.put("billing_tier", Arrays.asList(
                "tier_id", "portfolio_aum_min($)", "portfolio_aum_max($)", "fee_percentage(%)"
        ));
    }

    public List<FileUploadRecord> getAllFileRecords() {
        return fileUploadRepository.findAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public FileUploadRecord upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        log.info("Processing upload file: {}", originalFilename);

        // create upload record
        FileUploadRecord uploadRecord = FileUploadRecord.builder()
                .fileName(originalFilename)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .uploadDate(LocalDateTime.now())
                .createdBy(String.valueOf(BaseContext.getCurrentId())) // get user id from base context
                .status("PROCESSING") //
                .build();
        FileUploadRecord savedRecord = fileUploadRepository.save(uploadRecord);
        log.info("Saved record, id: {}", savedRecord.getUploadId());

        // start processing file
        try {
            String resultSummary = processExcelFile(file);
            savedRecord.setStatus("COMPLETED");
            savedRecord.setProcessingResult(resultSummary);
            return fileUploadRepository.save(savedRecord);
        } catch (Exception e) {
            log.error("Error processing file: {}", e.getMessage(), e);
            savedRecord.setStatus("FAILED");
            savedRecord.setProcessingResult("Error: " + e.getMessage());
            return fileUploadRepository.save(savedRecord);
        }
    }

    protected String processExcelFile(MultipartFile file) throws IOException {
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            throw new IOException("Invalid Excel File Format", e);
        }

        Map<String, Integer> processingStats = new HashMap<>();
        // upload result description
        StringBuilder resultSummary = new StringBuilder();

        // process each sheet
        for (String sheetName : EXPECTED_SHEETS_AND_COLUMNS.keySet()) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                log.warn("Sheet {} not found", sheetName);
                resultSummary.append("Sheet not found: ").append(sheetName).append("\n");
                continue;
            }

            log.info("Processing sheet {}", sheetName);
            int rowProcessed = processSheet(sheet);
            processingStats.put(sheetName, rowProcessed);
            resultSummary.append("Processed ").append(rowProcessed)
                    .append(" rows from '").append(sheetName).append("'. ");
        }

        log.info("Finish processing sheet, sheets amount: {}", processingStats.size());

        workbook.close();
        return resultSummary.toString();
    }

    protected int processSheet(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            log.warn("Header row not found in sheet: {}", sheet.getSheetName());
            return 0;
        }



        return 0;
    }
}
