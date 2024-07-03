package com.manut.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDTO {

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("mensagem")
    private String message;

    public StatusDTO() {

    }

    public StatusDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
