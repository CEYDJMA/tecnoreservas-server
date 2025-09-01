package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.repository.ResourceRepository;
import com.farukgenc.boilerplate.springboot.service.interfaces.IResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResourceService implements IResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public Resource create(Resource resource) {
        return null;
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
}
