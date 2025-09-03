package com.farukgenc.boilerplate.springboot.service.interfaces;

import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IResourceService {
    CreateResourceResponse create(CreateResourceRequest request);
    Optional<Resource> findById(Long id);
    Page<Resource> findAll(Pageable pageable);
    Resource update(Long id, Resource resourceDetails);
    void delete(Long id);
}
