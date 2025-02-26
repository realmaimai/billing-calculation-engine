package com.maimai.billingcalculationengine.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "File upload response data")
public class FileUploadResponse {
    @Schema(description = "Upload record ID")
    private Long uploadId;

    @Schema(description = "Original file name")
    private String fileName;

    @Schema(description = "File type/content type")
    private String fileType;

    @Schema(description = "File size in bytes")
    private Long fileSize;

    @Schema(description = "Processing status (PROCESSING, COMPLETED, FAILED)")
    private String status;

    @Schema(description = "Processing result message")
    private String processingResult;

    @Schema(description = "Number of records processed")
    private Integer recordsProcessed;
}