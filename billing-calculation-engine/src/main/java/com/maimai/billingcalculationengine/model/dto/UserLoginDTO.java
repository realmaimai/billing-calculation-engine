package com.maimai.billingcalculationengine.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@Schema(description = "Data transfer object for user login")
public class UserLoginDTO {
    @NotBlank(message = "Email is required")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Please provide a valid email address")
    @Size(max = 255)
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20)
    @Schema(description = "User's password (6-20 characters)", example = "SecurePass123", required = true)
    private String password;
}