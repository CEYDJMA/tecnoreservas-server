package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.LineDto;
import com.farukgenc.boilerplate.springboot.service.ServiceLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service/lines")
@CrossOrigin("*")
public class ServiceLineController {

    @Autowired
    private ServiceLineService serviceLineService;

    @GetMapping("/{id}")
    public ResponseEntity<LineDto> getServiceLineById(@PathVariable Long id){
        return ResponseEntity.ok().body(serviceLineService.getLineById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LineDto>> getServiceLines(){
        return ResponseEntity.ok().body(serviceLineService.getLines());
    }
}
