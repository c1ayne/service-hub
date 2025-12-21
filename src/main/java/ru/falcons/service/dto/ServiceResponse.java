package ru.falcons.service.dto;

import ru.falcons.service.entity.ServiceEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        Long ownerUserId,
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
