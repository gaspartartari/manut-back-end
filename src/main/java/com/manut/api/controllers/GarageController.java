package com.manut.api.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.manut.api.dto.GarageDTO;
import com.manut.api.services.GarageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/garage")
public class GarageController {

    @Autowired
    private GarageService garageService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Page<GarageDTO> findAll(Pageable pageable) {
        return garageService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<GarageDTO> insertGarage(@Valid @RequestBody GarageDTO garageDTO) {
        GarageDTO result = garageService.insertGarage(garageDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<GarageDTO> findById(@PathVariable Long id) {
        GarageDTO result = garageService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<GarageDTO> update(@PathVariable Long id, @Valid @RequestBody GarageDTO garageDTO) {
        GarageDTO result = garageService.update(id, garageDTO);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        garageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
