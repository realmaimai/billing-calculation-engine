package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, Long> {
    List<DocumentUpload> findAll();
}
