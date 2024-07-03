
package com.manut.api.controllers;

import java.net.URI;
import java.util.List;

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

import com.manut.api.dto.MapTasksDTO;
import com.manut.api.dto.MapVehicleDTO;
import com.manut.api.dto.PlanDTO;
import com.manut.api.projections.PlanVehicleProjection;
import com.manut.api.services.PlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PlanDTO> insertPlan(@Valid @RequestBody PlanDTO planDTO) {
        PlanDTO result = planService.insertPlan(planDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PlanDTO>> findAll(Pageable pageable) {
        Page<PlanDTO> result = planService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PlanDTO> findById(@PathVariable Long id) {
        PlanDTO result = planService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<PlanDTO> update(@PathVariable Long id, @Valid @RequestBody PlanDTO planDTO) {
        PlanDTO result = planService.update(id, planDTO);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}/map-vehicle")
    public ResponseEntity<PlanDTO> mapVehicleToPlan(
            @PathVariable Long id,
            @Valid @RequestBody MapVehicleDTO vehicleInfo) {
        PlanDTO result = planService.mapVehicleToPlan(id, vehicleInfo);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}/map-tasks")
    public ResponseEntity<PlanDTO> mapTasksToPlan(
            @PathVariable Long id,
            @Valid @RequestBody MapTasksDTO tasksId) {
        PlanDTO result = planService.mapTasksToPlan(id, tasksId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/dashboard")
    public ResponseEntity<List<PlanVehicleProjection>> findAllPlanVehicle() {
        List<PlanVehicleProjection> result = planService.dashobard();
        return ResponseEntity.ok(result);
    }

}
