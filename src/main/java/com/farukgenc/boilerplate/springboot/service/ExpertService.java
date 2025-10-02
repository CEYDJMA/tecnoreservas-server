package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.UserStatus;
import com.farukgenc.boilerplate.springboot.repository.ExpertRepository;
import com.farukgenc.boilerplate.springboot.repository.ServiceLineRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import com.farukgenc.boilerplate.springboot.security.dto.ExpertDto;
import com.farukgenc.boilerplate.springboot.security.dto.ReservationDto;
import com.farukgenc.boilerplate.springboot.security.utils.SecurityConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpertService {

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceLineRepository serviceLineRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ReservationService reservationService;

    public String createExpert(ExpertDto expertDto){
        Expert expert = new Expert();
        Long idService = expertDto.getLine();
        ServiceLine service = serviceLineRepository.getReferenceById(idService);
        expert.setName(expertDto.getName());
        expert.setLastname(expertDto.getLastname());
        expert.setEmail(expertDto.getEmail());
        expert.setUsername(expertDto.getUsername());
        expert.setServiceLine(service);
        expert.setUserRole(UserRole.EXPERT);
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
            expertDto.setLine(expert.getServiceLine().getId());
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

    public String changePassword(String newPassword){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password changed";
    }

    @Transactional
    public String expertActive(Long id){
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado con id: " + id));

        expert.setUserStatus(UserStatus.ACTIVO);
        expertRepository.save(expert);

        return "Active expert";
    }

    @Transactional
    public String expertInactive(Long id){
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado con id: " + id));

        expert.setUserStatus(UserStatus.INACTIVO);
        expertRepository.save(expert);

        return "Inactive expert";
    }
}
