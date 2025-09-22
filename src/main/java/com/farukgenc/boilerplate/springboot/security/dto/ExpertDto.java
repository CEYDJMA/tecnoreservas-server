package com.farukgenc.boilerplate.springboot.security.dto;

import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.model.enums.ProjectLine;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
@Data
public class ExpertDto {

    @NotEmpty(message = "{registration_name_not_empty}")
    private String name;

    @NotEmpty(message = "{registration_lastname_not_empty}")
    private String lastname;

    @Email(message = "{registration_email_is_not_valid}")
    @NotEmpty(message = "{registration_email_not_empty}")
    private String email;

    @NotEmpty(message = "{registration_username_not_empty}")
    private String username;

    @NotEmpty(message = "{registration_password_not_empty}")
    private String password;

    @NotEmpty(message = "{registration_status_not_empty")
    private String userStatus;

    @NotEmpty(message = "{registration_role_not_empty}")
    private String userRole;

    private ServiceLine line;
}
