package com.manut.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.manut.api.entities.PlanVehicle;
import com.manut.api.entities.PlanVehiclePK;
import com.manut.api.enums.MaintenanceStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlanVehicleDTO {
    
    private PlanVehiclePK id = new PlanVehiclePK();
    private Integer lastServiceOdometer;
    private Integer nextServiceOdometer;
    private BigDecimal lastServiceHourmeter;
    private BigDecimal nextServiceHourmeter;
    private LocalDate lastServiceDate;
    private LocalDate nextServiceDate;
    private MaintenanceStatus status;

    public PlanVehicleDTO(PlanVehicle pv){
        id = pv.getId();
        lastServiceOdometer = pv.getLastServiceOdometer();
        nextServiceOdometer = pv.getNextServiceOdometer();
        lastServiceHourmeter = pv.getLastServiceHourmeter();
        nextServiceHourmeter = pv.getNextServiceHourmeter();
        lastServiceDate = pv.getLastServiceDate();
        nextServiceDate = pv.getNextServiceDate();
        status = pv.getStatus();
    }
}
