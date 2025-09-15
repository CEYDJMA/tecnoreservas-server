package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.ExpertDto;
import com.farukgenc.boilerplate.springboot.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.http.client.HttpComponentsHttpAsyncClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experts")
public class ExpertController {

    @Autowired
    private ExpertService expertService;

    @PostMapping("/create")
    public ResponseEntity<String> createExpert(@RequestBody ExpertDto expertDto){
        return ResponseEntity.ok(expertService.createExpert(expertDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpertDto>> getAllExperts(){
        return ResponseEntity.ok(expertService.getAllExperts());
    }

    @PatchMapping("/update/email/{id}")
    public ResponseEntity<String> updateEmail(@PathVariable Long id, @RequestBody String email){
        return ResponseEntity.ok(expertService.updateEmail(id,email));
    }
}
