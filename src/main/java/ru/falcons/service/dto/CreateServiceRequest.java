package ru.falcons.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateServiceRequest(

        @Schema(description = "Service title", example = "Create parser")
        @NotBlank(message = "title cannot be empty")
        @Size(min = 5, max = 100, message = "title from 5 to 100 characters")
        String title,

        @Schema(description = "Detailed description", example = "warranty...")
        @Size(max = 2000, message = "description is too long")
        String description,

        @Schema(description = "Price (rub)", example = "1500.00")
        @NotNull(message = "price is required")
        @PositiveOrZero(message = "price cannot be negative")
        @Digits(integer = 15, fraction = 2)
        BigDecimal price
) {
}
