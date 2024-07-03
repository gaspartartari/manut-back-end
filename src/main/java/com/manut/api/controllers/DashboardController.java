package com.manut.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manut.api.dto.CardsDTO;
import com.manut.api.projections.MainTableProjection;
import com.manut.api.services.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    

    @Autowired
    private DashboardService dashboardService;

    @GetMapping(value = "/main-cards")
    public ResponseEntity<CardsDTO> mainCards() {
        CardsDTO result = dashboardService.mainCards();
        return ResponseEntity.ok(result);
    }



    @GetMapping(value = "/main-table")
    public ResponseEntity<Page<MainTableProjection>> mainTable(Pageable pageable,
        @RequestParam(name = "planId", defaultValue = "0") String planId,
        @RequestParam(name = "maintenanceStatus", defaultValue = "0") String maintenanceStatus,
        @RequestParam(name = "licensePlate", defaultValue = "") String licensePlate) {
        Page<MainTableProjection> result = dashboardService.mainTable(pageable, planId, maintenanceStatus, licensePlate);
        return ResponseEntity.ok(result);
    }
    
}
