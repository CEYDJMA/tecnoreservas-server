package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.Data;

@Data
public class ForUserRoleRequest {

    private Long idUser;

    private Long idServiceLine;

    private String projectName;

}
