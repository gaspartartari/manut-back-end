package com.manut.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MapVehicleDTO {

    @NotNull(message = "Vehicle cannot be null")
    private Long vehicleId;
    private Integer lastServiceOdometer;
    private BigDecimal lastServiceHourmeter;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate lastServiceDate;
    
}
