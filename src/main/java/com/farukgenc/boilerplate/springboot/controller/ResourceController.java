package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
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
}
