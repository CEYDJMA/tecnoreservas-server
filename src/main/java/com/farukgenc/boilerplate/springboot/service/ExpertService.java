package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Expert;
import com.farukgenc.boilerplate.springboot.model.UserRole;
import com.farukgenc.boilerplate.springboot.repository.ExpertRepository;
import com.farukgenc.boilerplate.springboot.security.dto.ExpertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpertService {

    @Autowired
    private ExpertRepository expertRepository;

    public String createExpert(ExpertDto expertDto){
        Expert expert = new Expert();
        expert.setName(expertDto.getName());
        expert.setLastname(expertDto.getLastname());
        expert.setEmail(expertDto.getEmail());
        expert.setUsername(expertDto.getUsername());
        expert.setServiceLine(expertDto.getLine());
        expert.setUserRole(UserRole.EXPERT);
        expert.setServiceLine(expertDto.getLine());
        expert.setPassword(expertDto.getPassword());
        expertRepository.save(expert);
        return "Expert created";
    }

    public List<ExpertDto> getAllExperts(){
        List<Expert> experts = expertRepository.findAll();
        List<ExpertDto> expertDtoList = new ArrayList<>();
        for (Expert expert: experts){
            ExpertDto expertDto = new ExpertDto();
            expertDto.setName(expert.getName());
            expertDto.setLastname(expert.getLastname());
            expertDto.setUsername(expert.getUsername());
            expertDto.setEmail(expert.getEmail());
            expertDto.setLine(expert.getServiceLine());
            expertDtoList.add(expertDto);
        }
        return expertDtoList;
    }

    public String updateEmail(Long id, String email){
        Expert expert = expertRepository.findById(id).orElseThrow();
        expert.setEmail(email);
        expertRepository.save(expert);
        return "expert's email successfull change";
    }


}
