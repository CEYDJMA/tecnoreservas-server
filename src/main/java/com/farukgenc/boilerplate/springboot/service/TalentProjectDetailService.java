package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.repository.*;
import com.farukgenc.boilerplate.springboot.security.dto.ProjectDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TalentProjectDetailService {

    @Autowired
    ServiceLineRepository serviceLineRepository;

    @Autowired
    TalentRepository talentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TrlOfProjectRepository trlOfProjectRepository;

    @Autowired
    TalentProjectDetailRepository talentProjectDetailRepository;

    public String assignDetails(ProjectDetailDto projectDetailDto){
        Long trlOfProjectId = projectDetailDto.getTrlOfProject();
        Optional<TrlOfProject> trlOfProject = trlOfProjectRepository.findById(trlOfProjectId);
        Long serviceLineId = projectDetailDto.getServiceLine();
        Optional<ServiceLine> serviceLine = serviceLineRepository.findById(serviceLineId);
        Long talentId = projectDetailDto.getTalent();
        Optional<Talent> talent = talentRepository.findById(talentId);

        TalentProjectDetail projectDetail = new TalentProjectDetail();
        projectDetail.setAssociatedProject(projectDetailDto.getAssociatedProject());
        projectDetail.setTrlOfProject(trlOfProject.get());
        projectDetail.setServiceLine(serviceLine.get());
        projectDetail.setTalent(talent.get());
        talentProjectDetailRepository.save(projectDetail);

        return "Informacion de proyecto guardada.";
    }
}
