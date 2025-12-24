package ru.falcons.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Services", description = "Methods for working with services")
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping()
    @Operation(
            summary = "Create new service",
            description = "Add service to DB. Token is required"
    )
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
    @Operation(summary = "Get service by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service found"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    public ResponseEntity<ServiceResponse> findById(@PathVariable Long id) {

        ServiceResponse response = serviceService.getById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all services")
    public ResponseEntity<List<ServiceResponse>> findAll() {

        List<ServiceResponse> response = serviceService.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update service",
            description = "Update service. Token is required"
    )
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
    @Operation(
            summary = "Delete service",
            description = "Delete service. Token is required"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal LaravelUser user
    ) {
        serviceService.delete(id, user.id());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    @Operation(
            summary = "Get all your services",
            description = "Get your services. Token is required"
    )
    public ResponseEntity<List<ServiceResponse>> findMyServices(@AuthenticationPrincipal LaravelUser user) {

        List<ServiceResponse> response = serviceService.getMyServices(user.id());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}