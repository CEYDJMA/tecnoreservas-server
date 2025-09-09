package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.EquipmentHistory;
import com.farukgenc.boilerplate.springboot.model.Resource;
import com.farukgenc.boilerplate.springboot.repository.EquipmentHistoryRepository;
import com.farukgenc.boilerplate.springboot.repository.ResourceRepository;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.CreateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.UpdateEquipmentHistoryRequest;
import com.farukgenc.boilerplate.springboot.security.dto.equipmenthistory.EquipmentHistoryResponse;
import com.farukgenc.boilerplate.springboot.security.mapper.equipmenthistory.EquipmentHistoryMapper;
import com.farukgenc.boilerplate.springboot.service.interfaces.IEquipmentHistoryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EquipmentHistoryService implements IEquipmentHistoryService {
	private final EquipmentHistoryRepository equipmentHistoryRepository;
	private final ResourceRepository resourceRepository;

	public EquipmentHistoryService(EquipmentHistoryRepository equipmentHistoryRepository, ResourceRepository resourceRepository) {
		this.equipmentHistoryRepository = equipmentHistoryRepository;
		this.resourceRepository = resourceRepository;
	}

	@Override
	public EquipmentHistoryResponse create(CreateEquipmentHistoryRequest request) {
		// Buscar el recurso asociado
		Resource resource = resourceRepository.findById(request.getResourceId())
			.orElseThrow(() -> new RuntimeException("Resource not found with ID: " + request.getResourceId()));

		// Crear el historial y asociarlo al recurso
		EquipmentHistory history = EquipmentHistoryMapper.toEntity(resource,request);

		// Guardar el historial
		EquipmentHistory saved = equipmentHistoryRepository.save(history);

		// Mapear a DTO de respuesta
		EquipmentHistoryResponse response = EquipmentHistoryMapper.toResponse(saved);
		return response;
	}

	@Override
	public EquipmentHistoryResponse findById(Long id) {
		return null;
	}

	@Override
	public List<EquipmentHistoryResponse> findAllByResourceId(Long resourceId) {
		return null;
	}

	@Override
	public EquipmentHistoryResponse update(Long id, UpdateEquipmentHistoryRequest request) {
		return null;
	}

	@Override
	public void delete(Long id) {
		// void, no retorna nada
	}
}
