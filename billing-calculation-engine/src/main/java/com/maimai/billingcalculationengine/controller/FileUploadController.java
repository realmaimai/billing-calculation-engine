package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.entity.FileUploadRecord;
import com.maimai.billingcalculationengine.model.response.FileUploadResponse;
import com.maimai.billingcalculationengine.service.FileUploadService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/files")
@Slf4j
public class FileUploadController {
    @Resource
    private FileUploadService fileUploadService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public Result<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Received file upload request: {}, size: {} bytes", file.getOriginalFilename(), file.getSize());

        // validate file type
        String contentType = file.getContentType();
        if (contentType == null || !(
                contentType.equals("application/vnd.ms-excel") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                        contentType.equals("text/csv"))) {
            log.warn("Invalid file type: {}", contentType);
            return Result.fail(400, "Invalid file type. Only Excel files (.xls, .xlsx) are supported.");
        }

        // todo: handle csv
        try {
            FileUploadRecord uploadRecord = fileUploadService.upload(file);

            // Convert entity to response
            FileUploadResponse response = FileUploadResponse.builder()
                    .uploadId(uploadRecord.getUploadId())
                    .fileName(uploadRecord.getFileName())
                    .fileType(uploadRecord.getFileType())
                    .fileSize(uploadRecord.getFileSize())
                    .status(uploadRecord.getStatus())
                    .processingResult(uploadRecord.getProcessingResult())
                    .build();

            if ("COMPLETED".equals(uploadRecord.getStatus())) {
                return Result.success(response, "File uploaded and processed successfully");
            } else {
                return Result.fail(response, 400, "File processing failed. See details in response.");
            }
        } catch (Exception e) {
            log.error("Error processing uploaded file", e);
            return Result.fail("Error processing file: " + e.getMessage());
        }
    }

    // Get all document upload records
    @GetMapping
    public Result<List<FileUploadRecord>> getAllFileRecords() {
        List<FileUploadRecord> fileRecords = fileUploadService.getAllFileRecords();
        return Result.success(fileRecords, "Documents retrieved successfully");
    }
}
