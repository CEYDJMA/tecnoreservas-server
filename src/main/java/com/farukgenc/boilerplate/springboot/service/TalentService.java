package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.ProjectLine;
import com.farukgenc.boilerplate.springboot.model.enums.UserStatus;
import com.farukgenc.boilerplate.springboot.repository.ExpertRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationDto;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationRequest;
import com.farukgenc.boilerplate.springboot.security.dto.TalentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TalentService {

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
    public String createTalent(TalentDto talentDto){
        try {

            Talent talent = new Talent();
            if (!userRepository.existsByUsername(talentDto.getUsername())) {
                talent.setName(talentDto.getName());
                talent.setLastname(talentDto.getLastname());
                talent.setEmail(talentDto.getEmail());
                talent.setUsername(talentDto.getUsername());
                talent.setPassword(bCryptPasswordEncoder.encode(talentDto.getPassword()));
                talent.setAssociatedProject(talentDto.getAssociatedProject());
                talent.setProjectLines(talentDto.getProjectLines());
                talent.setUserRole(UserRole.TALENT);
                talent.setUserStatus(UserStatus.ACTIVO);

                talentRepository.save(talent);
                return "Talento creado exitosamente";
            } else {
                return "El talento " + talentDto.getUsername() + " ya existe.";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public String createReservation(ReservationRequest request){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        int flag = 1;
        User user = userRepository.findByUsername(userDetails.getUsername());
        Expert expert = expertRepository.findByServiceLine_Id(request.getServiceLine());

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setDateTimeStart(request.getStartDate());
        reservationDto.setEndDateTime(request.getEndDate());
        reservationDto.setExpert(expert.getId());
        if (user.getUserRole().equals(UserRole.TALENT)){
            reservationDto.setTalent(user.getId());
        }
        System.out.println("La reserva del talento es: \n"
                            + reservationDto.getDateTimeStart() + "\n"+
                reservationDto.getEndDateTime() + "\n" +
                reservationDto.getTalent() + "\n" +
                reservationDto.getExpert());
        return reservationService.createReservation(reservationDto, flag);
    }
}
