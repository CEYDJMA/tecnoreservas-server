package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.BiotechnologyResource;
import com.farukgenc.boilerplate.springboot.model.GenericResource;
import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.repository.ResourceRepository;
import com.farukgenc.boilerplate.springboot.repository.ServiceLineRepository;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
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
        // Step 1: Declare variable to hold the resource instance
        Resource resource;
        
        // Step 2: Create appropriate entity instance based on ResourceType discriminator
        // This leverages JPA JOINED inheritance strategy with @DiscriminatorValue
        switch(request.getResourceType()) {
            case BIOTECHNOLOGY:
                // Creates BiotechnologyResource instance for specialized biotechnology equipment
                resource= new BiotechnologyResource();
                break;
            case GENERIC:
            default:
                // Creates GenericResource instance for standard equipment (default case)
                resource = new GenericResource();
                break;
        }

        // Step 3: Populate the empty entity with data from the request DTO
        // Uses void method that modifies resource object by reference
        mapRequestToEntity(request, resource);
        
        // Step 4: Persist the populated entity to database
        // JPA automatically sets @DiscriminatorColumn value and generates ID
        // Returns the saved entity with ID and any database-generated fields
        Resource savedResource = resourceRepository.save(resource);

        // Step 5: Transform the saved entity back to response DTO
        // Returns minimal response containing only id and name fields
        return mapEntityToResponse(savedResource);
    }

    @Override
    public Optional<Resource> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Page<Resource> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Resource update(Long id, Resource resourceDetails) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    // =====================================================
    // MÉTODOS PRIVADOS DE MAPEO
    // =====================================================

    /**
     * Maps CreateResourceRequest DTO to Resource entity.
     * Handles common fields for all resource types.
     * 
     * @param request The DTO with resource data from client
     * @param resource The entity to populate (BiotechnologyResource or GenericResource)
     */
    private void mapRequestToEntity(CreateResourceRequest request, Resource resource) {
        // Map common fields
        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setPlate(request.getPlate());
        resource.setModel(request.getModel());
        resource.setBrand(request.getBrand());
        
        // Buscar ServiceLine por ID y asignarlo
        ServiceLine serviceLine = serviceLineRepository.findById(request.getServiceLineId())
                .orElseThrow(() -> new RuntimeException("ServiceLine no encontrada con ID: " + request.getServiceLineId()));
        resource.setServiceLine(serviceLine);
        
        // Set default status for new resources
        resource.setStatus(com.farukgenc.boilerplate.springboot.model.enums.ResourceStatus.DISPONIBLE);
        
        // Save the resource
        // ServiceLine serviceLine = serviceLineRepository.findById(request.getServiceLineId())...
        // resource.setServiceLine(serviceLine);
        
        // Handle specific fields for BiotechnologyResource
        if (resource instanceof BiotechnologyResource biotechResource) {
            // TODO: Map biotechnology-specific fields when they are added to DTO
            // biotechResource.setMaxUsuariosSimultaneos(request.getMaxUsuariosSimultaneos());
            // biotechResource.setCondicionesDeUso(request.getCondicionesDeUso());
        }
    }

    /**
     * Maps Resource entity to CreateResourceResponse DTO.
     * Returns minimal response with only id and name.
     * 
     * @param resource The saved resource entity
     * @return CreateResourceResponse with id and name
     */
    private CreateResourceResponse mapEntityToResponse(Resource resource) {
        CreateResourceResponse response = new CreateResourceResponse();
        response.setId(resource.getId());
        response.setName(resource.getName());
        return response;
    }
}
