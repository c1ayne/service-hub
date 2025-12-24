package ru.falcons.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.falcons.service.dto.CreateServiceRequest;
import ru.falcons.service.dto.ServiceResponse;
import ru.falcons.service.entity.ServiceEntity;
import ru.falcons.service.repository.ServiceRepository;
import ru.falcons.service.service.ServiceServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    @Test
    void create_ShouldReturnResponse_WhenDataIsValid() {
//        GIVEN
        Long userId = 10L;
        CreateServiceRequest request = new CreateServiceRequest(
                "Test Service", "Description", new BigDecimal("100.00")
        );

        ServiceEntity savedEntity = new ServiceEntity();
        savedEntity.setId(1L);
        savedEntity.setTitle(request.title());
        savedEntity.setPrice(request.price());
        savedEntity.setOwnerUserId(userId);

        when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(savedEntity);

//        WHEN
        ServiceResponse response = serviceService.create(request, userId);

//        THEN
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Test Service");

        verify(serviceRepository, times(1)).save(any(ServiceEntity.class));
    }

    @Test
    void getById_ShouldReturnResponse_WhenServiceExist() {
//        GIVEN
        Long serviceId = 5L;

        ServiceEntity entity = new ServiceEntity();
        entity.setId(serviceId);
        entity.setTitle("Existing Service");

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(entity));

//        WHEN
        ServiceResponse response = serviceService.getById(serviceId);

//        THEN
        assertThat(response.id()).isEqualTo(serviceId);
        assertThat(response.title()).isEqualTo("Existing Service");
    }

    @Test
    void getById_ShouldThrowException_WhenServiceNotFound() {
//        GIVEN
        Long serviceId = 11L;

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

//        WHEN & THEN
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
            serviceService.getById(serviceId);
        });
    }

    @Test
    void delete_ShouldThrowAccessDenied_WhenUserIsNotOwner() {
//        GIVEN
        Long serviceId = 9L;
        Long ownerId = 15L;
        Long hackerId = 66L;

        ServiceEntity entity = new ServiceEntity();
        entity.setId(serviceId);
        entity.setOwnerUserId(ownerId);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(entity));

//        WHEN & THEN
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            serviceService.delete(serviceId, hackerId);
        });

        verify(serviceRepository, never()).delete(any());
    }

    @Test
    void delete_ShouldCallRepository_WhenUserIsOwner() {
//        GIVEN
        Long serviceId = 33L;
        Long userId = 3L;

        ServiceEntity entity = new ServiceEntity();
        entity.setId(serviceId);
        entity.setOwnerUserId(userId);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(entity));

//        WHEN
        serviceService.delete(serviceId, userId);

//        THEN
        verify(serviceRepository, times(1)).delete(entity);
    }
}
