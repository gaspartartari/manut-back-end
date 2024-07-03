package com.manut.api.dto;

import com.manut.api.enums.SyncStatus;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SyncInformDTO {

    private Long id;
    private SyncStatus status;
    private String motivoErro;

    public SyncInformDTO() {

    }

    public SyncInformDTO(Long id, SyncStatus status, String motivoErro) {
        this.id = id;
        this.status = status;
        this.motivoErro = motivoErro;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SyncInformDTO other = (SyncInformDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
