package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.FileUploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FileUploadRepository extends JpaRepository<FileUploadRecord, Long> {
    List<FileUploadRecord> findAll();

    @Query("SELECT MAX(fur.uploadDate) FROM FileUploadRecord fur WHERE fur.status = 'COMPLETED'")
    Optional<LocalDateTime> findLatestUploadDateWhereStatusCompleted();
}
