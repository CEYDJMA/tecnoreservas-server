package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.repository.ServiceLineRepository;
import com.farukgenc.boilerplate.springboot.security.dto.LineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceLineService {

    @Autowired
    private ServiceLineRepository serviceLineRepository;

    public LineDto getLineById(Long id){
        ServiceLine serviceLine = serviceLineRepository.findById(id).orElseThrow();
        LineDto lineDto = new LineDto();
        lineDto.setId(serviceLine.getId());
        lineDto.setName(serviceLine.getServiceLineName().toString());
        return lineDto;
    }
}
