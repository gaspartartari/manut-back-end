package com.manut.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({ "volume_max", "meta_km", "meta_hr", "casas_decimais_horimetro" })
public class VehicleDTO {

    // externalId
    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("placa")

    private String licensePlate;

    @JsonProperty("frota")
    private String fleet;

    @JsonProperty("bypass")
    private boolean bypass; // conferir o que é bypass e qual o tipo de dado correto

    @JsonProperty("categoria")
    private String category;

    @JsonProperty("modelo")
    private String model;

    @JsonProperty("numero_rfid")
    private String rfid;

    private Integer currentOdometer;
    private BigDecimal currentHourmeter;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate lastFuelingDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime lastFuelingTime;

    public VehicleDTO() {

    }

    public VehicleDTO(Vehicle entity) {
        this.externalId = entity.getExternalId();
        this.code = entity.getCode();
        this.name = entity.getName();
        this.licensePlate = entity.getLicensePlate();
        this.fleet = entity.getFleet();
        this.bypass = entity.isBypass();
        this.category = entity.getCategory();
        this.model = entity.getModel();
        this.rfid = entity.getRfid();
        this.currentOdometer = entity.getCurrentOdometer();
        this.currentHourmeter = entity.getCurrentHourmeter();
        this.lastFuelingDate = entity.getLastFuelingDate();
        this.lastFuelingTime = entity.getLastFuelingTime();
    }

}
