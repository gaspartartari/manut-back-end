package com.manut.api.controllers;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.manut.api.dto.CreateServiceOrderDTO;
import com.manut.api.dto.FinishServiceOrderDTO;
import com.manut.api.dto.MapTasksDTO;
import com.manut.api.dto.ServiceOrderDTO;
import com.manut.api.services.ServiceOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/service-order")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ServiceOrderDTO>> findAll(Pageable pageable) {
        Page<ServiceOrderDTO> result = serviceOrderService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceOrderDTO> createServiceOrder(
            @Valid @RequestBody CreateServiceOrderDTO serviceOrderDTO) {
        ServiceOrderDTO result = serviceOrderService.createServiceOrder(serviceOrderDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(serviceOrderDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}/insert-task")
    public ResponseEntity<ServiceOrderDTO> insertTask(
            @PathVariable Long id,
            @Valid @RequestBody MapTasksDTO taskList) {
        ServiceOrderDTO result = serviceOrderService.insertTask(taskList, id);
        return ResponseEntity.ok(result);
    }

    /*
     * // talvez deletar
     * 
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     * 
     * @PutMapping(value = "/{id}")
     * public ResponseEntity<ServiceOrderDTO> updateServiceOrder(
     * 
     * @PathVariable Long id,
     * 
     * @Valid @RequestBody InsertTaskServiceOrderDTO updateServiceOrderDTO) {
     * ServiceOrderDTO result =
     * serviceOrderService.updateServiceOrder(updateServiceOrderDTO, id);
     * return ResponseEntity.ok(result);
     * }
     */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}/finish")
    public ResponseEntity<ServiceOrderDTO> finishServiceOrder(@PathVariable Long id,
            @Valid @RequestBody FinishServiceOrderDTO finishOrderServiceTaskDTO) {
        ServiceOrderDTO result = serviceOrderService.finishServiceOrder(id, finishOrderServiceTaskDTO);
        return ResponseEntity.ok(result);
    }

}
