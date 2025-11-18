package com.farukgenc.boilerplate.springboot.security.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TalentResponseDto {
    private Long id;
    private String name;
    private String username;
    private Long lineProjectId;
    private String email;
}
