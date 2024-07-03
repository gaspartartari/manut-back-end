package com.manut.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manut.api.dto.SyncFuelingDTO;
import com.manut.api.services.FuelingService;

@RestController
@RequestMapping(value = "/fuelings")
public class FuelingController {

    @Autowired
    private FuelingService fuelingService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<SyncFuelingDTO> insertFuellings(@RequestBody SyncFuelingDTO syncFuelingDTO) {
        SyncFuelingDTO result = fuelingService.saveFueling(syncFuelingDTO);
        return ResponseEntity.ok(result);
    }

}
