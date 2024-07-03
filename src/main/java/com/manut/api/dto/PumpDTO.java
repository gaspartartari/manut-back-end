package com.manut.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Pump;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//bomba
public class PumpDTO {

    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("codigo")
    private String code; // check for correct data type

    @JsonProperty("nome")
    private String name;



    public PumpDTO() {

    }

    public PumpDTO(Pump entity){
        externalId = entity.getExternalId();
        this.code = entity.getCode();
        this.name = entity.getName();
    
    }
}
