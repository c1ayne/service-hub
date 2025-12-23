package ru.falcons.service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.falcons.service.dto.CreateServiceRequest;
import ru.falcons.service.dto.ServiceResponse;
import ru.falcons.service.dto.UpdateServiceRequest;
import ru.falcons.service.entity.ServiceEntity;
import ru.falcons.service.repository.ServiceRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Override
    public ServiceResponse create(CreateServiceRequest request, Long userId) {

        ServiceEntity newService = new ServiceEntity();
        newService.setTitle(request.title());
        newService.setDescription(request.description());
        newService.setPrice(request.price());
        newService.setOwnerUserId(userId);

        return ServiceResponse.fromEntity(serviceRepository.save(newService));
    }

    @Override
    public ServiceResponse getById(Long id) {
        return ServiceResponse.fromEntity(
                serviceRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Service not found"))
        );
    }

    @Override
    public List<ServiceResponse> getAll() {
        return serviceRepository.findAll()
                .stream()
                .map(ServiceResponse::fromEntity)
                .toList();
    }

    @Override
    public ServiceResponse update(Long id, UpdateServiceRequest request, Long userId) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        if (!service.getId().equals(userId)) {
            throw new AccessDeniedException("This is not your service");
        }

        service.setTitle(request.title());
        service.setDescription(request.description());
        service.setPrice(request.price());

        return ServiceResponse.fromEntity(service);
    }

    @Override
    public void delete(Long id, Long userId) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        if (!service.getId().equals(userId)) {
            throw new AccessDeniedException("This is not your service");
        }

        serviceRepository.delete(service);
    }

    @Override
    public List<ServiceResponse> getMyServices(Long userId) {
        return serviceRepository.findAllByOwnerUserId(userId)
                .stream()
                .map(ServiceResponse::fromEntity)
                .toList();
    }
}