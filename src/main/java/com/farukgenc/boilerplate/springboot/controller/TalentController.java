package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.TalentDto;
import com.farukgenc.boilerplate.springboot.service.TalentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/talents")
public class TalentController {

    @Autowired
    private TalentService talentService;

    @GetMapping("/all")
    @Operation(
            summary = "Obtener todos los talentos",
            description = "Devuelve la lista completa de talentos registrados en el sistema.",
            tags = "Talent"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de talentos obtenida correctamente.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TalentDto.class)
            )
    )
    public ResponseEntity<List<TalentDto>> getAll() {
        return ResponseEntity.ok(talentService.getTalents());
    }

    @PostMapping("/create")
    @Operation(
            summary = "Crear nuevo talento",
            description = "Permite registrar un nuevo talento en el sistema enviando su información en el cuerpo de la solicitud.",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Talento creado exitosamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos del talento son inválidos o incompletos."
            )
    })
    public ResponseEntity<String> createTalent(
            @Parameter(description = "Información del talento a crear.", required = true)
            @RequestBody TalentDto talentDto) {

        return ResponseEntity.ok(talentService.createTalent(talentDto));
    }

    @PatchMapping("/update/email/{id}")
    public ResponseEntity<String> updateEmail(@PathVariable Long id, @RequestBody String email){
        return ResponseEntity.ok(talentService.updateEmail(id, email));
    }

    @PatchMapping("/active/{id}")
    public ResponseEntity<String> activeTalent(@PathVariable Long id){
        return ResponseEntity.ok(talentService.talentActive(id));
    }

    @PatchMapping("/inactive/{id}")
    public ResponseEntity<String> inactiveExpert(@PathVariable Long id){
        return ResponseEntity.ok(talentService.talentInactive(id));
    }

    @PatchMapping("/suspended/{id}")
    public ResponseEntity<String> suspendedExpert(@PathVariable Long id){
        return ResponseEntity.ok(talentService.talentSuspended(id));
    }
}
