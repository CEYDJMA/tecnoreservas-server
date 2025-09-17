package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.service.SessionLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session/logs")
public class SessionLogController {

    @Autowired
    private SessionLogService sessionLogService;

    @GetMapping("/information")
    public ResponseEntity<String> getInformation(HttpServletRequest request){
        return ResponseEntity.ok(sessionLogService.registerLog(request));
    }
}
