package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.BaseContext;
import com.maimai.billingcalculationengine.common.enums.SheetName;
import com.maimai.billingcalculationengine.common.exception.*;
import com.maimai.billingcalculationengine.common.utils.FileUtil;
import com.maimai.billingcalculationengine.model.entity.*;
import com.maimai.billingcalculationengine.repository.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    private static final Long currentUserId = BaseContext.getCurrentId();

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
                .createdBy(String.valueOf(currentUserId)) // get user id from base context
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
        resultSummary.append("Finish processing sheet, sheets amount: %d").append(processingStats.size());

        workbook.close();
        return resultSummary.toString();
    }

    protected int processSheet(Sheet sheet) {
        String sheetName = sheet.getSheetName();
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            log.warn("Header row not found in sheet: {}", sheetName);
            return 0;
        }

        List<String> expectedColumns = EXPECTED_SHEETS_AND_COLUMNS.get(sheetName);
        Map<String, Integer> columnIndexMap = FileUtil.validateHeaders(headerRow, expectedColumns);
        if (columnIndexMap == null) {
            log.warn("Invalid header in sheet: {}", sheetName);
            return 0;
        }

        int rowCount = 0;
        int successCount = 0;
        int totalRows = sheet.getLastRowNum();

        for (int i = 1; i <= totalRows; i++) {
            Row dataRow = sheet.getRow(i);
            if (dataRow == null) continue;

            rowCount++;
            try {
                processDataRow(dataRow, sheetName, columnIndexMap);
                successCount++;
            } catch (Exception e) {
                log.error("Error processing row {} in sheet {}: {}", i, sheetName, e.getMessage(), e);
            }
        }

        log.info("Processed {}/{} rows successfully in sheet: {} (Total rows: {})",
                successCount, rowCount, sheetName, totalRows);
        return successCount;
    }

    private void processDataRow(Row dataRow, String sheetName, Map<String, Integer> columnIndexMap) {
        SheetName sheetEnum;
        try {
            sheetEnum = SheetName.fromString(sheetName); // Convert string to enum
        } catch (IllegalArgumentException e) {
            log.warn("Unknown sheet name: {}", sheetName);
            return;
        }

        // sheet selection
        switch (sheetEnum) {
            case CLIENT_BILLING:
                processClientRow(dataRow, columnIndexMap);
                break;
            case PORTFOLIO:
                processPortfolioRow(dataRow, columnIndexMap);
                break;
            case ASSETS:
                processAssetRow(dataRow, columnIndexMap);
                break;
            case BILLING_TIER:
                processBillingTierRow(dataRow, columnIndexMap);
                break;
        }
    }

    private void processClientRow(Row row, Map<String, Integer> columnIndexMap) {
        String clientId = FileUtil.getCellValueAsString(row, columnIndexMap.get("client_id"));
        if (clientId == null || clientId.isEmpty()) {
            log.error("Found row with empty client_id");
            throw new InvalidDataException(String.format("Client missing in this position, row: %d", row.getRowNum()));
        }

        // check if client already exists
        Optional<Client> existingClient = clientRepository.findByClientId(clientId);

        String clientName = FileUtil.getCellValueAsString(row, columnIndexMap.get("client_name"));
        String province = FileUtil.getCellValueAsString(row, columnIndexMap.get("province"));
        String country = FileUtil.getCellValueAsString(row, columnIndexMap.get("country"));
        String billingTierId = FileUtil.getCellValueAsString(row, columnIndexMap.get("billing_tier_id"));

        Client client;
        if (existingClient.isPresent()) {
            // update existing client
            client = existingClient.get();
            client.setClientName(clientName);
            client.setProvince(province);
            client.setCountry(country);
            client.setBillingTierId(billingTierId);
            client.setUpdatedAt(LocalDateTime.now());
            client.setUpdatedBy(String.valueOf(currentUserId));
            log.debug("Updating existing client: {}", clientId);
        } else {
            // create new client
            client = Client.builder()
                    .clientId(clientId)
                    .clientName(clientName)
                    .province(province)
                    .country(country)
                    .billingTierId(billingTierId)
                    .createdAt(LocalDateTime.now())
                    .createdBy(String.valueOf(currentUserId))
                    .build();
            log.debug("Creating new client: {}", clientId);
        }

        clientRepository.save(client);
        log.debug("Saved client: {}", clientId);
    }

    private void processPortfolioRow(Row row, Map<String, Integer> columnIndexMap) {
        String portfolioId = FileUtil.getCellValueAsString(row, columnIndexMap.get("portfolio_id"));
        if (portfolioId == null || portfolioId.isEmpty()) {
            log.error("Row with empty portfolio_id");
            throw new InvalidDataException(String.format("Portfolio missing in this position, row: %d", row.getRowNum()));
        }

        String clientId = FileUtil.getCellValueAsString(row, columnIndexMap.get("client_id"));
        String portfolioCurrency = FileUtil.getCellValueAsString(row, columnIndexMap.get("portfolio_currency"));

        // validate client exists
        if (clientRepository.findByClientId(clientId).isEmpty()) {
            log.warn("Client ID {} does not exist for portfolio {}", clientId, portfolioId);
            throw new InvalidDataException(String.format("Client missing in this position, row: %d", row.getRowNum()));
        }

        // check if portfolio already exists
        Optional<Portfolio> existingPortfolio = portfolioRepository.findById(portfolioId);

        Portfolio portfolio;
        if (existingPortfolio.isPresent()) {
            // update existing portfolio
            portfolio = existingPortfolio.get();
            portfolio.setClientId(clientId);
            portfolio.setPortfolioCurrency(portfolioCurrency);
            portfolio.setUpdatedAt(LocalDateTime.now());
            portfolio.setUpdatedBy(String.valueOf(currentUserId));
            log.debug("Updating existing portfolio: {}", portfolioId);
        } else {
            // create new portfolio
            portfolio = Portfolio.builder()
                    .portfolioId(portfolioId)
                    .clientId(clientId)
                    .portfolioCurrency(portfolioCurrency)
                    .createdAt(LocalDateTime.now())
                    .createdBy(String.valueOf(currentUserId))
                    .build();
            log.debug("Creating new portfolio: {}", portfolioId);
        }

        portfolioRepository.save(portfolio);
        log.debug("Saved portfolio: {}", portfolioId);
    }

    private void processBillingTierRow(Row row, Map<String, Integer> columnIndexMap) {
        String tierId = FileUtil.getCellValueAsString(row, columnIndexMap.get("tier_id"));
        if (tierId == null || tierId.isEmpty()) {
            log.error("Found row with empty tier_id in row {}", row.getRowNum());
            throw new InvalidDataException(String.format("Found row %d with empty tier_id", row.getRowNum()));
        }

        // handle column names with special characters
        BigDecimal minAum = FileUtil.getCellValueAsBigDecimal(row, columnIndexMap.get("portfolio_aum_min($)"));
        BigDecimal maxAum = FileUtil.getCellValueAsBigDecimal(row, columnIndexMap.get("portfolio_aum_max($)"));
        BigDecimal feePercentage = FileUtil.getCellValueAsBigDecimal(row, columnIndexMap.get("fee_percentage(%)"));

        // validate fee percentage is between 0 and 100
        if (feePercentage.compareTo(BigDecimal.ZERO) < 0 || feePercentage.compareTo(new BigDecimal("100")) > 0) {
            log.error("Invalid fee percentage for tier {}: {}%", tierId, feePercentage);
            throw new InvalidDataException(String.format("Invalid fee percentage for tier %s: %s%%. It must be between 0 and 100.", tierId, feePercentage));
        }

        // validate aum min & max
        if (minAum.compareTo(maxAum) > 0) {
            log.error("Invalid AUM range for tier {}: min({}) > max({})", tierId, minAum, maxAum);
            throw new InvalidDataException(String.format("Invalid AUM range for tier %s: min(%s) > max(%s).", tierId, minAum, maxAum));
        }

        // Create the composite key
        BillingTierKey billingTierKey = new BillingTierKey(tierId, minAum, maxAum);

        // Look for the tier using the complete composite key
        Optional<BillingTier> existingTier = billingTierRepository.findById(billingTierKey);

        BillingTier billingTier;
        if (existingTier.isPresent()) {
            // Update existing tier
            billingTier = existingTier.get();
            billingTier.setFeePercentage(feePercentage);
            log.debug("Updating existing billing tier: {}", tierId);
        } else {
            // Create new tier
            billingTier = BillingTier.builder()
                    .tierId(tierId)
                    .portfolioAumMin(minAum)
                    .portfolioAumMax(maxAum)
                    .feePercentage(feePercentage)
                    .build();
            log.debug("Creating new billing tier: {}", tierId);
        }

        billingTierRepository.save(billingTier);
        log.debug("Saved billing tier: {}", tierId);
    }

    private void processAssetRow(Row row, Map<String, Integer> columnIndexMap) {
        String assetId = FileUtil.getCellValueAsString(row, columnIndexMap.get("asset_id"));
        String portfolioId = FileUtil.getCellValueAsString(row, columnIndexMap.get("portfolio_id"));

        if (assetId == null || assetId.isEmpty() ) {
            log.error("Found row with empty asset_id");
            throw new InvalidDataException(String.format("Found row %d with empty asset_id", row.getRowNum()));
        }

        if (portfolioId == null || portfolioId.isEmpty()) {
            log.error("Found row with empty portfolio_id");
            throw new InvalidDataException(String.format("Found row %d with empty portfolio_id", row.getRowNum()));
        }

        // validate portfolio exists
        if (portfolioRepository.findById(portfolioId).isEmpty()) {
            log.error("Portfolio ID {} does not exist for asset {}", portfolioId, assetId);
            throw new InvalidDataException(String.format("Portfolio ID does not exist for asset in row %d", row.getRowNum()));
        }

        BigDecimal assetValue = FileUtil.getCellValueAsBigDecimal(row, columnIndexMap.get("asset_value"));
        String currency = FileUtil.getCellValueAsString(row, columnIndexMap.get("currency"));
        LocalDate date = FileUtil.getCellValueAsDate(row, columnIndexMap.get("date"));

        if (date == null) {
            log.error("Found row with empty date for asset {}", assetId);
            throw new InvalidDataException(String.format("Found row %d with empty date", row.getRowNum()));
        }

        if (currency == null || currency.isEmpty()) {
            log.error("Found row with empty currency for asset {}", assetId);
            throw new InvalidDataException(String.format("Found row %d with empty currency", row.getRowNum()));
        }

        // get all assets for this portfolio
        List<Asset> existingAssets = assetRepository.findAllByPortfolioId(portfolioId);

        // find if this exact asset exists (same date, portfolio, asset ID)
        Optional<Asset> existingAsset = Optional.empty();
        for (Asset a : existingAssets) {
            if (a.getAssetId().equals(assetId) &&
                    a.getPortfolioId().equals(portfolioId) &&
                    a.getDate().isEqual(date)) {
                existingAsset = Optional.of(a);
                break;
            }
        }

        Asset asset;
        if (existingAsset.isPresent()) {
            // update existing asset
            asset = existingAsset.get();
            asset.setAssetValue(assetValue);
            asset.setCurrency(currency);
            asset.setUpdatedAt(LocalDateTime.now());
            asset.setUpdatedBy(String.valueOf(currentUserId));
            log.debug("Updating existing asset: {} for portfolio: {} on date: {}",
                    assetId, portfolioId, date);
        } else {
            // create new asset
            asset = Asset.builder()
                    .assetId(assetId)
                    .portfolioId(portfolioId)
                    .date(date)  // Important: set the date for the composite key
                    .assetValue(assetValue)
                    .currency(currency)
                    .createdAt(LocalDateTime.now())
                    .createdBy(String.valueOf(currentUserId))
                    .build();
            log.debug("Creating new asset: {} for portfolio: {} on date: {}",
                    assetId, portfolioId, date);
        }

        assetRepository.save(asset);
        log.debug("Saved asset: {} for portfolio: {} on date: {}", assetId, portfolioId, date);
    }

}
