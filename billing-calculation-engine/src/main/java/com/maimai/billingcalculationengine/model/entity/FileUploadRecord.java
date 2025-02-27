package com.maimai.billingcalculationengine.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_uploads", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long uploadId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @CreatedDate
    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "status")
    private String status;

    @Column(name = "processing_result", length = 2000)
    private String processingResult;
}