package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
import com.farukgenc.boilerplate.springboot.security.dto.resource.UpdateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.UpdateResourceResponse;
import com.farukgenc.boilerplate.springboot.security.dto.resource.ResourceListItemResponse;
import com.farukgenc.boilerplate.springboot.security.dto.resource.PagedResponse;
import com.farukgenc.boilerplate.springboot.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Resource operations.
 * Handles HTTP requests related to resource management.
 * 
 * @author Generated
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * Creates a new resource.
     * 
     * @param createRequest The resource creation request with validation
     * @return ResponseEntity containing the created resource response with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<CreateResourceResponse> createResource(@Valid @RequestBody CreateResourceRequest createRequest) {
        CreateResourceResponse createdResource = resourceService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
    }

    /**
     * Updates an existing resource using PATCH operation.
     * Allows partial updates - only provided fields will be modified.
     * Supports both Generic and Biotechnology resources.
     * 
     * @param id The ID of the resource to update
     * @param updateRequest The resource update request with optional fields and validation
     * @return ResponseEntity containing the complete updated resource response with HTTP 200 status
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResourceResponse> updateResource(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateResourceRequest updateRequest) {
        UpdateResourceResponse updatedResource = resourceService.update(id, updateRequest);
        return ResponseEntity.ok(updatedResource);
    }

    /**
     * Retrieves a paginated list of resources with stable JSON structure.
     * Returns DTOs with fields according to resource type.
     *
     * @param page Page number (default 0)
     * @param size Page size (default 10)
     * @return Paginated list of ResourceListItemResponse wrapped in PagedResponse
     */
    @GetMapping
    public ResponseEntity<PagedResponse<ResourceListItemResponse>> getResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long serviceLineId) {
        PagedResponse<ResourceListItemResponse> response = resourceService.findAll(PageRequest.of(page, size),serviceLineId);
        return ResponseEntity.ok(response);
    }
}
