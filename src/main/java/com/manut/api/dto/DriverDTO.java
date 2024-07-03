package com.manut.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Driver;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@JsonIgnoreProperties({ "codigo_alfanumerico" })
public class DriverDTO {

    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("codigo")
    private String code; // check for correct variable type

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("cnh")
    private String cnh;


    public DriverDTO() {

    }

    public DriverDTO(Driver entity){
        externalId = entity.getExternalId();
        this.name = entity.getName();
        this.code = entity.getCode();
        this.cpf = entity.getCpf();
        this.cnh = entity.getCnh();
    }

}
