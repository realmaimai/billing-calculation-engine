package com.maimai.billingcalculationengine.model.dto;

import com.maimai.billingcalculationengine.common.constants.MessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@Schema(description = "Data transfer object for user registration")
public class UserRegisterDTO {

    @NotBlank(message = "Email is required")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = MessageConstants.Validation.EMAIL_INVALID)
    @Size(max = 255)
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = MessageConstants.Validation.PASSWORD_WEAK)
    @Schema(description = "User's password (6-20 characters)", example = "SecurePass123", required = true)
    private String password;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can only contain letters")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "User's first name (letters only)", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name can only contain letters")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "User's last name (letters only)", example = "Doe", required = true)
    private String lastName;
}