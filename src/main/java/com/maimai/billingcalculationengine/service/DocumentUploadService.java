package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.model.entity.DocumentUpload;
import com.maimai.billingcalculationengine.repository.DocumentUploadRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Service
public class DocumentUploadService {
    @Resource
    private DocumentUploadRepository documentUploadRepository;

    @GetMapping()
    public List<DocumentUpload> getAllDocumentRecords() {
        return documentUploadRepository.findAll();
    }
}
