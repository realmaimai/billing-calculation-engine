package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, Long> {
}
