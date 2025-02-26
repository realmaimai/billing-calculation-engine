package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.entity.FileUploadRecord;
import com.maimai.billingcalculationengine.repository.FileUploadRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FileUploadService {
    @Resource
    private FileUploadRepository fileUploadRepository;

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

    @GetMapping()
    public List<FileUploadRecord> getAllFileRecords() {
        return fileUploadRepository.findAll();
    }

    public Integer upload(MultipartFile file) {
        return null;
    }
}
