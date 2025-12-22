package ru.falcons.service.service;

import ru.falcons.service.dto.CreateServiceRequest;
import ru.falcons.service.dto.ServiceResponse;
import ru.falcons.service.dto.UpdateServiceRequest;

import java.util.List;

public interface ServiceService {
    ServiceResponse create(CreateServiceRequest request, Long userId);

    ServiceResponse getById(Long id);

    List<ServiceResponse> getAll();

    ServiceResponse update(Long id, UpdateServiceRequest request, Long userId);

    void delete(Long id, Long userId);

    List<ServiceResponse> getMyServices(Long userId);
}
