package com.manut.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Station;

import lombok.Getter;
import lombok.Setter;

//posto
@Getter
@Setter
@JsonIgnoreProperties({ "codigo_alfanumerico", "uf" })
public class StationDTO {

    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("codigo")
    private String code; // check for correct data type

    @JsonProperty("posto_comercial")
    private boolean commercialStation;

    @JsonProperty("cnpj")
    private String cnpj;


    public StationDTO() {

    }

    public StationDTO(Station entity) {
        externalId = entity.getExternalId();
        this.name = entity.getName();
        this.code = entity.getCode();
        this.commercialStation = entity.isCommercialStation();
        this.cnpj = entity.getCnpj();
    }
}
