package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.NameTrl;
import com.farukgenc.boilerplate.springboot.model.enums.ProjectPhase;
import com.farukgenc.boilerplate.springboot.repository.*;
import com.farukgenc.boilerplate.springboot.security.dto.ProjectDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TalentProjectDetailService {

    @Autowired
    private ServiceLineRepository serviceLineRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TalentProjectDetailRepository talentProjectDetailRepository;

    @Autowired
    private ExpertRepository expertRepository;

    public TalentProjectDetail assignDetails(ProjectDetailDto projectDetailDto, Talent talent) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        Expert expert = expertRepository.findById(user.getId()).orElseThrow();
        Long serviceLineId = expert.getServiceLine().getId();
        ServiceLine serviceLine = serviceLineRepository.findById(serviceLineId).orElseThrow();

        TalentProjectDetail projectDetail = new TalentProjectDetail();

        if (projectDetailDto.getProjectPhase() == null) {
            throw new IllegalArgumentException("La fase no puede ser nula.");
        }
        //convertir texto a mayusculas
        String phase = projectDetailDto.getProjectPhase().toUpperCase();

        switch (phase) {
            case "INICIO":
            case "PLANEACION":
                projectDetail.setNameTrl(NameTrl.TRL6);
                break;

            case "EJECUCION":
            case "CIERRE":
                projectDetail.setNameTrl(NameTrl.TRL7);
                break;

            default:
                throw new IllegalArgumentException("Fase no valida: " + projectDetailDto.getProjectPhase());
        }

        projectDetail.setProjectPhase(ProjectPhase.valueOf(phase));
        projectDetail.setAssociatedProject(projectDetailDto.getAssociatedProject());
        projectDetail.setServiceLine(serviceLine);
        projectDetail.setTalent(talent);
        talentProjectDetailRepository.save(projectDetail);
        return projectDetail;
    }

    public Long getLineProjectTalentId(Long talentId){
        TalentProjectDetail projectDetail = talentProjectDetailRepository.findByTalentId(talentId);
        return projectDetail.getId();
    }
}
