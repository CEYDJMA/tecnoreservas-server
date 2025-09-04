package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.Talent;
import com.farukgenc.boilerplate.springboot.model.UserRole;
import com.farukgenc.boilerplate.springboot.repository.TalentRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.TalentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TalentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TalentRepository talentRepository;

    final private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TalentService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String createTalent(TalentDto talentDto){
        Talent talent = new Talent();
        if (!userRepository.existsByUsername(talentDto.getUsername())){
            talent.setName(talentDto.getName());
            talent.setLastname(talentDto.getLastname());
            talent.setEmail(talentDto.getEmail());
            talent.setUsername(talentDto.getUsername());
            talent.setPassword(bCryptPasswordEncoder.encode(talentDto.getPassword()));
            talent.setAssociatedProject(talentDto.getAssociatedProject());
            talent.setProjectLines(talentDto.getProjectLines());
            talent.setUserRole(UserRole.TALENT);
            talentRepository.save(talent);
        }
        return "Talento creado exitosamente";
    }

    public List<TalentDto> getTalents(){
        List<Talent> talentList = talentRepository.findAll();
        List<TalentDto> talentDtoList = new ArrayList<>();
        for (Talent talent: talentList){
            TalentDto talentDto = new TalentDto();
            talentDto.setName(talent.getName());
            talentDto.setLastname(talent.getLastname());
            talentDto.setEmail(talent.getEmail());
            talentDto.setUsername(talent.getUsername());
            talentDto.setAssociatedProject(talent.getAssociatedProject());
            talentDto.setProjectLines(talent.getProjectLines());
            talentDtoList.add(talentDto);
        }
        return talentDtoList;
    }

    public String deleteTalents(Long id){
        Talent talent = talentRepository.findById(id).orElseThrow();
        talentRepository.delete(talent);
        return "Talento Eliminado ";
    }
}
