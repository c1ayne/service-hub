package ru.falcons.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.falcons.security.LaravelUser;
import ru.falcons.service.dto.CreateServiceRequest;
import ru.falcons.service.dto.ServiceResponse;
import ru.falcons.service.dto.UpdateServiceRequest;
import ru.falcons.service.service.ServiceService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping()
    public ResponseEntity<ServiceResponse> create(
            @Valid @RequestBody CreateServiceRequest request,
            @AuthenticationPrincipal LaravelUser currentUser
    ) {
        ServiceResponse response = serviceService.create(request, currentUser.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> findById(@PathVariable Long id) {

        ServiceResponse response = serviceService.getById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> findAll() {

        List<ServiceResponse> response = serviceService.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateServiceRequest request,
            @AuthenticationPrincipal LaravelUser user
    ) {
        ServiceResponse response = serviceService.update(id, request, user.id());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal LaravelUser user
    ) {
        serviceService.delete(id, user.id());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<ServiceResponse>> findMyServices(@AuthenticationPrincipal LaravelUser user) {

        List<ServiceResponse> response = serviceService.getMyServices(user.id());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}