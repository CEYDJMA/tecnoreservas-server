package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.CreateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.EquipmentHistoryResponse;
import com.farukgenc.boilerplate.springboot.service.EquipmentHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment/histories")
public class EquipmentHistoryController {

	private final EquipmentHistoryService equipmentHistoryService;

	@Autowired
	public EquipmentHistoryController(EquipmentHistoryService equipmentHistoryService) {
		this.equipmentHistoryService = equipmentHistoryService;
	}

	/**
	 * Crea un nuevo registro de historial de mantenimiento para un recurso.
	 * @param request Datos del mantenimiento
	 * @return Registro creado
	 */

    @Operation(
            summary = "Crear historial de mantenimiento",
            description = "Crea un nuevo registro de historial de mantenimiento para un recurso."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Historial creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EquipmentHistoryResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
	@PostMapping
	public ResponseEntity<EquipmentHistoryResponse> createEquipmentHistory(@RequestBody CreateEquipmentHistoryRequest request) {
		EquipmentHistoryResponse response = equipmentHistoryService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
