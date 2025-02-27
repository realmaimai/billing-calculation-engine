package com.maimai.billingcalculationengine.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @NotBlank(message = "Client ID is required")
    @Size(max = 10, message = "Client ID cannot exceed 10 characters")
    @Column(name = "client_id", length = 10)
    private String clientId;

    @NotBlank(message = "Client name is required")
    @Size(max = 100, message = "Client name cannot exceed 100 characters")
    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;

    @Size(max = 50, message = "Province cannot exceed 50 characters")
    @Column(name = "province", length = 50)
    private String province;

    @Size(max = 50, message = "Country cannot exceed 50 characters")
    @Column(name = "country", length = 50)
    private String country;

    @NotBlank(message = "Billing tier ID is required")
    @Size(max = 10, message = "Billing tier ID cannot exceed 10 characters")
    @Column(name = "billing_tier_id", length = 10)
    private String billingTierId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;
}