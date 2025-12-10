package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.ProjectPhase;
import com.farukgenc.boilerplate.springboot.repository.ExpertRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentProjectDetailRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.ForUserRoleRequest;
import com.farukgenc.boilerplate.springboot.security.dto.ProjectDetailDto;
import com.farukgenc.boilerplate.springboot.security.dto.ResponseForUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class SuperAdminService {

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private TalentProjectDetailRepository talentProjectDetailRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TalentProjectDetailService talentProjectDetailService;

    @Autowired
    private UserRepository userRepository;

    public String assignProjectAndServiceline (ForUserRoleRequest forUserRoleRequest) {
        try {
            UserRole userRole = userRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow().getUserRole();

            if (userRole != null) {
                Optional<TalentProjectDetail> talentProjectDetail = Optional.ofNullable(talentProjectDetailRepository.findFirstByAssociatedProject(forUserRoleRequest.getProjectName()));
                //buscar el que es
                if (userRole == UserRole.TALENT) {
                    if (talentProjectDetail.isPresent() && !talentProjectDetail.map(TalentProjectDetail::getServiceLine).get().getId().equals(forUserRoleRequest.getIdServiceLine())) {

                        Talent talent = talentRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow();
                        ProjectDetailDto projectDetailDto = new ProjectDetailDto();
                        projectDetailDto.setAssociatedProject(forUserRoleRequest.getProjectName());
                        projectDetailDto.setProjectPhase(ProjectPhase.INICIO.toString());
                        TalentProjectDetail talentProjectDetail1 = talentProjectDetailService.assignDetails(projectDetailDto, talent, forUserRoleRequest.getIdServiceLine());
                    }
                    //User user = userRepository.findById(forUserRoleRequest.getIdUser());-
                    //Expert expert = expertRepository.findById(user.get().getId());
                    //Talent talent = talentRepository.findById(user.get().getId());
                    //if (!talentProjectDetail.isPresent()){}
                }

            }

        /*if (talent.isPresent()) {

            TalentProjectDetail projectDetail = talentProjectDetailService.assignDetails(projectDetailDto, talent);
        }*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "loka";
    };
}
