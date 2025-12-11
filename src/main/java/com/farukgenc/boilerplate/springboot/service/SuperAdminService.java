package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.ProjectPhase;
import com.farukgenc.boilerplate.springboot.repository.*;
import com.farukgenc.boilerplate.springboot.security.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class SuperAdminService {

    @Autowired
    private ExpertService expertService;

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

    @Autowired
    private ServiceLineRepository serviceLineRepository;

    public ForUserRoleResponse<?> assignProjectAndServiceline (ForUserRoleRequest forUserRoleRequest) {
        try {
            UserRole userRole = userRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow().getUserRole();
            if (userRole != null) {
                //buscar el que es
                if (userRole == UserRole.TALENT) {
                    Optional<TalentProjectDetail> talentProjectDetail = Optional.ofNullable(talentProjectDetailRepository.findFirstByAssociatedProject(forUserRoleRequest.getProjectName()));
                    if (talentProjectDetail.isPresent() && !talentProjectDetail.map(TalentProjectDetail::getServiceLine).get().getId().equals(forUserRoleRequest.getIdServiceLine())) {
                        Talent talent = talentRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow();
                        ProjectDetailDto projectDetailDto = new ProjectDetailDto();
                        projectDetailDto.setAssociatedProject(forUserRoleRequest.getProjectName());
                        projectDetailDto.setProjectPhase(ProjectPhase.INICIO.toString());
                        TalentProjectDetail talentProjectDetail1 = talentProjectDetailService.assignDetails(projectDetailDto, talent, forUserRoleRequest.getIdServiceLine());
                    }
                } else if (userRole == UserRole.EXPERT) {
                    ServiceLine serviceLine = serviceLineRepository.findById(forUserRoleRequest.getIdServiceLine()).orElseThrow();
                    Expert expert = expertRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow();
                    expert.setServiceLine(serviceLine);
                    expertRepository.save(expert);
                    ExpertDto expertDto = new ExpertDto();
                    expertDto.setName(expert.getName());
                    expertDto.setUsername(expert.getUsername());

                    expertDto.s


                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ;

    };
}
