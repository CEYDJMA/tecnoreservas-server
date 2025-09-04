package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.TalentDto;
import com.farukgenc.boilerplate.springboot.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/talents")
public class TalentController {

    @Autowired
    private TalentService talentService;

    @GetMapping("/all")
    public ResponseEntity<List<TalentDto>> getAll(){
        return ResponseEntity.ok(talentService.getTalents());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTalent(@RequestBody TalentDto talentDto){
        return ResponseEntity.ok(talentService.createTalent(talentDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> killTalent(@PathVariable Long id){
        return ResponseEntity.ok(talentService.deleteTalents(id));
    }
}
