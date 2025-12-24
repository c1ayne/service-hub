package ru.falcons.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.falcons.service.entity.ServiceEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceResponse(

        @Schema(description = "Service id", example = "15")
        Long id,

        @Schema(description = "Service title", example = "Create parser")
        String title,

        @Schema(description = "Detailed description", example = "warranty...")
        String description,

        @Schema(description = "Price (rub)", example = "1500.00")
        BigDecimal price,

        @Schema(description = "Service owner id", example = "3")
        Long ownerUserId,

        @Schema(description = "The time when the service was created", example = "2009-12-02T11:25:25")
        LocalDateTime createdAt
) {
    public static ServiceResponse fromEntity(ServiceEntity entity) {
        return new ServiceResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getOwnerUserId(),
                entity.getCreatedAt()
        );
    }
}
