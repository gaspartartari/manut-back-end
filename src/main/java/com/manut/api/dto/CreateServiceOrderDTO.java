package com.manut.api.dto;



import com.manut.api.services.validations.ServiceOrderInsertValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ServiceOrderInsertValid
public class CreateServiceOrderDTO {

    private Long id;

    @NotNull(message = "Vehicle canot be null")
    private Long vehicleId;

    @NotNull(message = "Plan canot be null")
    private Long planId;

    private Long garageId;

    private Double cost;

}

