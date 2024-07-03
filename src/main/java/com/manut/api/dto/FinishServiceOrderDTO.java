package com.manut.api.dto;

import java.util.Map;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FinishServiceOrderDTO {
    
    @NotNull(message = "Task list cannot be null")
    private Map<String, Boolean> tasks;

    @NotNull(message = "FinishCounter cannot be null")
    @Min(value = 1, message = "FinishCounter must be greater than 0")
    private Integer finishCounter;

    @NotNull(message = "Service cost cannot be null")
    @Min(value = 1, message = "Cost must be greater than 0")
    private Double serviceCost;

    @NotNull(message = "Material cost cannot be null")
    @Min(value = 1, message = "Cost must be greater than 0")
    private Double materialCost;
}
