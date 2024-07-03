package com.manut.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Tank;

import lombok.Getter;
import lombok.Setter;

//tanque
@Getter
@Setter
public class TankDTO {

    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("codigo")
    private String code; // check for correct data type

    @JsonProperty("nome")
    private String name;



    public TankDTO() {

    }

    public TankDTO(Tank entity) {
        externalId = entity.getExternalId();
        this.code = entity.getCode();
        this.name = entity.getName();
    
    }

    
}
