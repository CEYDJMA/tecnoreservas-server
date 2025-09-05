package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.repository.ResourceRepository;
import com.farukgenc.boilerplate.springboot.repository.ServiceLineRepository;
import com.farukgenc.boilerplate.springboot.security.dto.resource.*;
import com.farukgenc.boilerplate.springboot.security.mapper.resource.ResourceMapper;
import com.farukgenc.boilerplate.springboot.service.interfaces.IResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResourceService implements IResourceService {

    private final ResourceRepository resourceRepository;
    private final ServiceLineRepository serviceLineRepository;


    public ResourceService(ResourceRepository resourceRepository, ServiceLineRepository serviceLineRepository) {
        this.resourceRepository = resourceRepository;
        this.serviceLineRepository = serviceLineRepository;
    }

    /**
     * Creates a new resource based on the provided request data.
     * Handles both BIOTECHNOLOGY and GENERIC resource types using JPA inheritance.
     * 
     * @param request The CreateResourceRequest containing resource data and type discriminator
     * @return CreateResourceResponse with the created resource's id and name
     */
    @Override
    public CreateResourceResponse create(CreateResourceRequest request) {
        // Step 1: Create appropriate entity instance using factory method
        Resource resource = ResourceMapper.createResourceInstance(request.getResourceType());

        // Set ServiceLine based on serviceLineId
        ServiceLine serviceLine = serviceLineRepository.findById(request.getServiceLineId())
                .orElseThrow(() -> new RuntimeException("ServiceLine no encontrada con ID: " + request.getServiceLineId()));

        // Step 2: Populate the entity with data from the request DTO
        ResourceMapper.mapRequestToEntity(request, resource, serviceLine);
        
        // Step 3: Persist the populated entity to database
        Resource savedResource = resourceRepository.save(resource);

        // Step 4: Transform the saved entity back to response DTO
        return ResourceMapper.mapEntityToResponse(savedResource);
    }

    @Override
    public Optional<Resource> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public PagedResponse<ResourceListItemResponse> findAll(Pageable pageable) {
    Page<Resource> resourcePage = resourceRepository.findAllWithServiceLine(pageable);
    return ResourceMapper.mapPageToPagedResponse(resourcePage);
    }

    /**
     * Updates an existing resource using PATCH operations.
     * Allows partial updates - only provided fields will be modified.
     * Supports both BIOTECHNOLOGY and GENERIC resource types.
     * 
     * @param id The ID of the resource to update
     * @param updateRequest The UpdateResourceRequest containing optional fields to update
     * @return UpdateResourceResponse with complete updated resource information
     */
    @Override
    public UpdateResourceResponse update(Long id, UpdateResourceRequest updateRequest) {
        
        // Step 1: Check if resourceType is provided to determine update strategy
        if (updateRequest.getResourceType() != null) {
            // Step 1a: Create new resource instance of requested type
            Resource newResource = ResourceMapper.createResourceInstance(updateRequest.getResourceType());
            
            // Step 1b: Find existing resource to copy data from
            Resource existingResource = resourceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Resource no encontrado con ID: " + id));

            // Step 1c: Copy existing data to new resource instance
            ResourceMapper.copyResourceFields(existingResource, newResource);

            ServiceLine serviceLine = serviceLineRepository.findById(newResource.getServiceLine().getId())
                    .orElseThrow(() -> new RuntimeException("ServiceLine no encontrada con ID: " + updateRequest.getServiceLineId()));
            System.out.println("Por aqui pasa el codigo despues de buscar el id de serviceLine");

            System.out.println("este es el valor de ServiceLine: "+serviceLine.getId());

            // Step 1d: Apply updates from request
            ResourceMapper.mapUpdateRequestToEntity(updateRequest, newResource, serviceLine);

            // Step 1e: Persist the populated entity to database
            Resource savedResource = resourceRepository.save(newResource);

            // Step 1f: Find existing service line by id
            Optional<ServiceLine> serviceLineByResource = serviceLineRepository.findById(savedResource.getServiceLine().getId());

            // Step 1g: Transform the update entity back to response DTO and return
            return ResourceMapper.mapEntityToUpdateResponse(serviceLineByResource,savedResource);
            
        } else {
            // Step 2: Standard update without type change
            Resource existingResource = resourceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Resource no encontrado con ID: " + id));

            ServiceLine serviceLine = serviceLineRepository.findById(existingResource.getServiceLine().getId())
                    .orElseThrow(() -> new RuntimeException("ServiceLine no encontrada con ID: " + updateRequest.getServiceLineId()));
            
            // Step 2a: Apply partial updates to existing resource
            ResourceMapper.mapUpdateRequestToEntity(updateRequest, existingResource,serviceLine);
            
            // Step 2b: Save updated resource
            Resource savedResource = resourceRepository.save(existingResource);
            Optional<ServiceLine> serviceLineByResource = serviceLineRepository.findById(savedResource.getServiceLine().getId());
            
            return ResourceMapper.mapEntityToUpdateResponse(serviceLineByResource,savedResource);
        }
    }

    @Override
    public void delete(Long id) {

    }
}
