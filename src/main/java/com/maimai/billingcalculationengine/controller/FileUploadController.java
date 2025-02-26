package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.entity.FileUploadRecord;
import com.maimai.billingcalculationengine.service.FileUploadService;
import jakarta.annotation.Resource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {
    @Resource
    private FileUploadService fileUploadService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public Result<Void> uploadFile(@RequestParam("file") MultipartFile file) {
        fileUploadService.upload(file);
        return Result.successWithoutData("okok");
    }

    // Get all document upload records
    @GetMapping
    public Result<List<FileUploadRecord>> getAllFileRecords() {
        List<FileUploadRecord> fileRecords = fileUploadService.getAllFileRecords();
        return Result.success(fileRecords, "Documents retrieved successfully");
    }
}
