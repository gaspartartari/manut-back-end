package com.manut.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.manut.api.enums.MaintenanceStatus;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_plan_vehicle")
public class PlanVehicle {

    @EmbeddedId
    private PlanVehiclePK id = new PlanVehiclePK();
    private Integer lastServiceOdometer;
    private Integer nextServiceOdometer;
    private BigDecimal lastServiceHourmeter;
    private BigDecimal nextServiceHourmeter;
    private LocalDate lastServiceDate;
    private LocalDate nextServiceDate;
    private MaintenanceStatus status;

    public PlanVehicle() {

    }

    public PlanVehicle(Plan plan, Vehicle vehicle, Integer lastServiceOdometer, Integer nextServiceOdometer,  BigDecimal lastServiceHourmeter, BigDecimal nextServiceHourmeter,
                                LocalDate lastServiceDate, LocalDate nextServiceDate, MaintenanceStatus status){
        id.setPlan(plan);
        id.setVehicle(vehicle);
        this.lastServiceOdometer = lastServiceOdometer;
        this.nextServiceOdometer = nextServiceOdometer;
        this.lastServiceHourmeter = lastServiceHourmeter;
        this.nextServiceHourmeter = nextServiceHourmeter;
        this.lastServiceDate = lastServiceDate;
        this.nextServiceDate = nextServiceDate;
        this.status = status;
        
    }
}
