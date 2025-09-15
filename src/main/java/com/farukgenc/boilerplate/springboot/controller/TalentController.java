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

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar talento",
            description = "Elimina un talento existente en el sistema según el ID proporcionado.",
            tags = "Talent"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Talento eliminado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró un talento con el ID proporcionado."
            )
    })
    public ResponseEntity<String> killTalent(
            @Parameter(description = "ID del talento a eliminar.", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(talentService.deleteTalents(id));
    }
}
