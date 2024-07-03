package com.manut.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Company;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties({ "codigo_alfanumerico" , "uf" })
public class CompanyDTO {

    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("filial")
    private String branch;

    @JsonProperty("cnpj")
    private String cnpj;

    public CompanyDTO() {

    }

    public CompanyDTO(Company entity){
        externalId = entity.getExternalId();
        this.code = entity.getCode();
        this.name = entity.getName();
        this.branch = entity.getBranch();
        this.cnpj = entity.getCnpj();

    }
}
