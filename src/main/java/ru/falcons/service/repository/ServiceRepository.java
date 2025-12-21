package ru.falcons.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.falcons.service.entity.ServiceEntity;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findAllByOrderByCreatedAtDesc();

    List<ServiceEntity> findAllByOwnerUserId(Long ownerUserId);

    List<ServiceEntity> findAllByTitleContainingIgnoreCase(String title);

    boolean existsByIdAndOwnerUserId(Long id, Long ownerUserId);
}
