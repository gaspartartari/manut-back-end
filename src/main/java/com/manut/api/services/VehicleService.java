package com.manut.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manut.api.dto.VehicleDTO;
import com.manut.api.entities.Vehicle;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.VehicleRepository;
import com.manut.api.services.exceptions.DatabaseException;
import com.manut.api.services.exceptions.ResourceNotFoundException;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthService authService;


    @Transactional(readOnly = true)
    public Page<VehicleDTO> findAll(Pageable pageable) {
        Long clientId = authService.getClientId();
        Page<Vehicle> result = vehicleRepository.findAllVehicles(clientId, pageable);
        return result.map(x -> new VehicleDTO(x));
    }

    @Transactional(readOnly = true)
    public VehicleDTO findById(Long id) {
        Long clientId = authService.getClientId();
        Vehicle result = vehicleRepository.searchById(id, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found"));
        authService.validateClient(result.getClient().getId());
        return new VehicleDTO(result);
    }

    @Transactional
    public VehicleDTO update(Long id, VehicleDTO vehicleDTO) {
        Long clientId = authService.getClientId();
        Vehicle vehicle = vehicleRepository.searchById(id, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found"));
        authService.validateClient(vehicle.getClient().getId());
        vehicle.setName(vehicleDTO.getName());
        vehicle.setBypass(vehicleDTO.isBypass());
        vehicle.setCategory(vehicleDTO.getCategory());
        vehicle.setCode(vehicleDTO.getCode());
        vehicle.setFleet(vehicleDTO.getFleet());
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setRfid(vehicleDTO.getRfid());
        vehicleRepository.save(vehicle);
        return new VehicleDTO(vehicle);
    }

    @Transactional
    public void delete(Long id) {
        Long clientId = authService.getClientId();
        Optional<Vehicle> result = vehicleRepository.searchById(id, clientId);
        if (result.isEmpty())
            throw new ResourceNotFoundException("Resource not found: " + id);
        try {
            Vehicle vehicle = result.get();
            authService.validateClient(vehicle.getClient().getId());
            vehicleRepository.deleteById(vehicle.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential constraint violation");
        }
    }

}
