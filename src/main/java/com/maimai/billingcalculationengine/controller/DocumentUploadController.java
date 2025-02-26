package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.entity.DocumentUpload;
import com.maimai.billingcalculationengine.service.DocumentUploadService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentUploadController {
    @Resource
    private DocumentUploadService documentUploadService;

    // Get all document upload records
    @GetMapping
    public Result<List<DocumentUpload>> getAllDocuments() {
        List<DocumentUpload> documentRecords = documentUploadService.getAllDocumentRecords();
        return Result.success(documentRecords, "Documents retrieved successfully");
    }
}
