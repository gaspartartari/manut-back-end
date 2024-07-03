package com.manut.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manut.api.entities.Attendant;
import lombok.Getter;
import lombok.Setter;

//frentista
@Getter
@Setter
@JsonIgnoreProperties({ "excluido" })
public class AttendantDTO {

    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("ativo")
    private boolean active;

    @JsonProperty("numero_rfid")
    private String rfid;
    
    public AttendantDTO() {

    }

    public AttendantDTO(Attendant entity) {
        externalId = entity.getExternalId();
        code = entity.getCode();
        name = entity.getName();
        cpf = entity.getCpf();
        active = entity.isActive();
        rfid = entity.getRfid();
    }
}
