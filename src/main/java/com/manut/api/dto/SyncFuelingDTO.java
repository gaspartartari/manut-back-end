package com.manut.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

//class for SincronizaAbastecimento API call
@Getter
@Setter
public class SyncFuelingDTO {

    @JsonProperty("status")
    private StatusDTO status;

    @JsonProperty("abastecimentos")
    private List<FuelingDTO> fuellings;

    public SyncFuelingDTO() {

    }

    public SyncFuelingDTO(StatusDTO status, List<FuelingDTO> fuellings) {
        this.status = status;
        this.fuellings = fuellings;
    }

}
