package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.UserStatus;
import com.farukgenc.boilerplate.springboot.repository.ExpertRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentProjectDetailRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TalentService {

    @Autowired
    private TalentProjectDetailService talentProjectDetailService;

    @Autowired
    private TalentProjectDetailRepository talentProjectDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private ReservationService reservationService;

    final private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TalentService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public String createTalent(TalentDto talentDto, ProjectDetailDto projectDetailDto){
        try {
            Talent talent = new Talent();
            if (!userRepository.existsByUsername(talentDto.getUsername())) {
                talent.setName(talentDto.getName());
                talent.setLastname(talentDto.getLastname());
                talent.setEmail(talentDto.getEmail());
                talent.setUsername(talentDto.getUsername());
                talent.setPassword(bCryptPasswordEncoder.encode(talentDto.getPassword()));
                talent.setUserRole(UserRole.TALENT);
                talent.setUserStatus(UserStatus.ACTIVO);
                talentRepository.save(talent);

                TalentProjectDetail projectDetail = talentProjectDetailService.assignDetails(projectDetailDto, talent);
                return "Talento creado exitosamente y datos de proyecto asignados." +
                        "Fase de proyecto: " + projectDetail.getProjectPhase() +
                        " | TRL asignado: " + projectDetail.getNameTrl();
            } else {
                return "El talento " + talentDto.getUsername() + " ya existe.";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TalentResponseDto> getTalents(){
        List<Talent> talentList = talentRepository.findAll();
        List<TalentResponseDto> talentDtoList = new ArrayList<>();
        for (Talent talent: talentList){
            TalentResponseDto talentDto = new TalentResponseDto();
            talentDto.setId(talent.getId());
            talentDto.setName(talent.getName() + " " + talent.getLastname());
            talentDto.setEmail(talent.getEmail());
            talentDto.setUsername(talent.getUsername());
            talentDto.setLineProjectId(talentProjectDetailService.getLineProjectTalentId(talent.getId()));
            talentDtoList.add(talentDto);
        }
        return talentDtoList;
    }

    public TalentResponseDto getTalent(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        Talent talent = talentRepository.findById(user.getId()).orElseThrow();

        TalentResponseDto talentResponseDto = new TalentResponseDto();
        talentResponseDto.setId(talent.getId());
        talentResponseDto.setName(talent.getName() + " " + talent.getLastname());
        talentResponseDto.setEmail(talent.getEmail());
        talentResponseDto.setLineProjectId(talent.getTalentProjectDetails().get(0).getServiceLine().getId());
        talentResponseDto.setUsername(talent.getUsername());
        return talentResponseDto;
    }

    public String updateEmail(Long id, String email){
        Talent talent = talentRepository.findById(id).orElseThrow();
        talent.setEmail(email);
        talentRepository.save(talent);
        return "talent's email successfull change";
    }

    public String changePassword (String newPassword){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password changed";
    }

    @Transactional
    public String talentActive(Long id){
        Talent talent = talentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado con id: " + id));

        talent.setUserStatus(UserStatus.ACTIVO);
        talentRepository.save(talent);

        return "Active aalent";
    }

    @Transactional
    public String talentInactive(Long id){
        Talent talent = talentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado con id: " + id));

        talent.setUserStatus(UserStatus.INACTIVO);
        talentRepository.save(talent);

        return "Inactive talent";
    }

    @Transactional
    public String talentSuspended(Long id){
        Talent talent = talentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado con id: " + id));

        talent.setUserStatus(UserStatus.SUSPENDIDO);
        talentRepository.save(talent);

        return "Suspended talent";
    }

    public ReservationResponse createReservation(ReservationRequest request){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        int flag = 1;
        User user = userRepository.findByUsername(userDetails.getUsername());
        Expert expert = expertRepository.findByServiceLine_Id(request.getServiceLine());
        UserRole userRole = user.getUserRole();

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setDateTimeStart(request.getStartDate());
        reservationDto.setEndDateTime(request.getEndDate());
        reservationDto.setExpert(expert.getId());
        if (user.getUserRole().equals(UserRole.TALENT)){
            reservationDto.setTalent(user.getId());
        }
        return reservationService.createReservation(reservationDto, userRole, flag);
    }

    public List<TalentAndProjectDto> getTalentsByServiceLine(Long serviceLineId){
        List<TalentProjectDetail> talentProjects = talentProjectDetailRepository.findAllByServiceLine_Id(serviceLineId);
        List<TalentAndProjectDto> talents = new ArrayList<>();
        for (TalentProjectDetail projects : talentProjects){
            Talent talent = talentRepository.findById(projects.getTalent().getId()).orElseThrow();
            TalentAndProjectDto response = new TalentAndProjectDto();

            TalentDto talentDto = new TalentDto();
            talentDto.setName(talent.getName());
            talentDto.setUsername(talent.getUsername());
            talentDto.setEmail(talent.getEmail());

            ProjectDetailDto projectDetailDto = new ProjectDetailDto();
            projectDetailDto.setAssociatedProject(projects.getAssociatedProject());
            projectDetailDto.setProjectPhase(projects.getProjectPhase().toString());

            response.setTalentDto(talentDto);
            response.setProjectDetailDto(projectDetailDto);
            talents.add(response);
        }
        return talents;
    }

    public ReservationResponse createReservationWithResources(ReservationWithResourcesRequest reservationWithResourcesRequest){
        if (reservationWithResourcesRequest.getResourceIds().isEmpty() || reservationWithResourcesRequest.getResourceIds().get(0) == 0){
            reservationWithResourcesRequest.setResourceIds(null);
        }
        return reservationService.createReservationWithResource(reservationWithResourcesRequest);
    }
}
