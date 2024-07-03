package com.manut.api.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.manut.api.entities.ServiceOrder;
import com.manut.api.entities.ServiceTask;
import com.manut.api.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceOrderDTO {

    private Long id;
    private Long vehicleId;
    private Long planId;
    private List<ServiceTaskDTO> serviceTasks = new ArrayList<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate conclusionDate;
    private Integer conclusionCounter;
    private OrderStatus status;
    private GarageDTO garage;
    private Double serviceCost;
    private Double materialCost;
    private Double totalCost;

    public ServiceOrderDTO() {

    }

    public ServiceOrderDTO(ServiceOrder entity) {
        id = entity.getId();
        this.vehicleId = entity.getVehicle().getExternalId();
        this.planId = entity.getPlan().getId();
        conclusionDate = entity.getConclusionDate() == null ? null : entity.getConclusionDate();
        conclusionCounter = entity.getConclusionCounter() == null ? null : entity.getConclusionCounter();
        serviceCost = entity.getServiceCost() == null ? null : entity.getServiceCost();
        materialCost = entity.getMaterialCost() == null ? null : entity.getMaterialCost();
        totalCost = entity.getTotalCost() == null ? null : entity.getTotalCost();
        garage = new GarageDTO(entity.getGarage());
        this.status = entity.getStatus();
        if (entity.getServiceTasks() != null) {
            for ( ServiceTask st : entity.getServiceTasks()){
                serviceTasks.add(new ServiceTaskDTO(st));
            }
        }

    }
}
