package com.farukgenc.boilerplate.springboot.service.interfaces;

import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.CreateResourceResponse;
import com.farukgenc.boilerplate.springboot.security.dto.resource.UpdateResourceRequest;
import com.farukgenc.boilerplate.springboot.security.dto.resource.UpdateResourceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IResourceService {
    CreateResourceResponse create(CreateResourceRequest request);
    Optional<Resource> findById(Long id);
    Page<Resource> findAll(Pageable pageable);
    UpdateResourceResponse update(Long id, UpdateResourceRequest resourceDetails);
    void delete(Long id);
}
