package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
import com.farukgenc.boilerplate.springboot.security.dto.resource.UpdateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.UpdateResourceResponse;
import com.farukgenc.boilerplate.springboot.service.ResourceService;
import jakarta.validation.Valid;
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
}
