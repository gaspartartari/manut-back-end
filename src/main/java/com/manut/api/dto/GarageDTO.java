package com.manut.api.dto;

import com.manut.api.entities.Garage;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GarageDTO {
    
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    public GarageDTO(Garage entity){
        id = entity.getId();
        name = entity.getName();
    }
}
