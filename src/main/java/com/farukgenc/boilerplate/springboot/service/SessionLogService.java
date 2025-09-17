package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.SessionLog;
import com.farukgenc.boilerplate.springboot.model.User;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionLogService {

    @Autowired
    private UserRepository userRepository;

    public String registerLog(HttpServletRequest request){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        SessionLog sessionLog = new SessionLog();
        sessionLog.setUser(user);

        sessionLog.setLoginAt(LocalDateTime.now());

        sessionLog.setIpAddress(request.getRemoteAddr());

        return user.getUsername() + " se ha conectado el " + sessionLog.getLoginAt() + " desde " + request.getRemoteAddr();
    }

}
