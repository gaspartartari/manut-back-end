package com.manut.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Fuel;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@JsonIgnoreProperties({ "codigo_exportacao" })
public class FuelDTO {


    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("codigo")
    private String code;


    public FuelDTO() {

    }

    public FuelDTO(Fuel entity){
        externalId = entity.getExternalId();
        this.description = entity.getDescription();
        this.code = entity.getCode();
        
    }

    
}
