package ru.falcons.service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateServiceRequest(

        @NotBlank(message = "title cannot be empty")
        @Size(min = 5, max = 100, message = "title from 5 to 100 characters")
        String title,

        @Size(max = 2000, message = "description is too long")
        String description,

        @NotNull(message = "price is required")
        @PositiveOrZero(message = "price cannot be negative")
        @Digits(integer = 15, fraction = 2)
        BigDecimal price
) {
}
