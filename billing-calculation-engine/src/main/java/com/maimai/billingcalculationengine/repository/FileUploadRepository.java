package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.FileUploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileUploadRepository extends JpaRepository<FileUploadRecord, Long> {
    List<FileUploadRecord> findAll();
}
