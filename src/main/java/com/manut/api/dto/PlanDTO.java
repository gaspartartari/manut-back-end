package com.manut.api.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Plan;
import com.manut.api.entities.Task;
import com.manut.api.entities.Vehicle;
import com.manut.api.enums.RecurrenceType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanDTO {

    private Long id;

    
    @NotBlank(message = "Name canot be blank")
    @Size(min = 5, message = "Name must have at least 5 characters")
    private String name;

    @NotNull(message = "RecurrenceInterval canot be null")
    @Min(value = 1, message = "RecurrenceInterval must be greater than 0")
    @JsonProperty("recurrenceInterval")
    private Integer recurrenceInterval;

    @NotNull(message = "RecurrenceType cannot be null")
    private RecurrenceType recurrenceType;

    @NotNull(message = "Tolerance canot be null")
    @Min(value = 1, message = "Tolerance must be greater than 0")
    private Integer tolerance;

    @NotNull(message = "isActive cannot be null")
    @JsonProperty("isActive")
    private Boolean isActive;
    
    @NotNull(message = "isRecurrent cannot be blank")
    @JsonProperty("isRecurrent")
    private Boolean isRecurrent;

    private Set<Long> vehicles = new HashSet<>();
    private Set<Long> tasks = new HashSet<>();

    public PlanDTO(Plan entity) {
        id = entity.getId();
        name = entity.getName();
        recurrenceInterval = entity.getRecurrenceInterval();
        recurrenceType = entity.getRecurrenceType();
        tolerance = entity.getTolerance();
        isActive = entity.getIsActive();
        isRecurrent = entity.getIsRecurrent();
        if(entity.getVehicles() != null){
            for(Vehicle v : entity.getVehicles()){
                vehicles.add(v.getExternalId());
            }
        }

        if(entity.getTasks() != null){
            for(Task t : entity.getTasks()){
                tasks.add(t.getId());
            }
        }
    }

}
