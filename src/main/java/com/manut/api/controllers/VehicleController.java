package com.manut.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manut.api.dto.VehicleDTO;
import com.manut.api.services.VehicleService;

@RestController
@RequestMapping(value = "/vehicles")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<VehicleDTO>> findAll(Pageable pageable){
        Page<VehicleDTO> result = vehicleService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity <VehicleDTO> findById(@PathVariable Long id ){
        VehicleDTO result = vehicleService.findById(id);
        return ResponseEntity.ok(result);
    }
}
