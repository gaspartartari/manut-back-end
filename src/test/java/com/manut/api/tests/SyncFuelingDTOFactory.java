package com.manut.api.tests;

import java.util.List;

import com.manut.api.dto.FuelingDTO;
import com.manut.api.dto.StatusDTO;
import com.manut.api.dto.SyncFuelingDTO;

public class SyncFuelingDTOFactory {
    
    public static SyncFuelingDTO createSyncFuelingDTO(List<FuelingDTO> fuelings){

        StatusDTO statusDTO = new StatusDTO("001", "success");
        SyncFuelingDTO result = new SyncFuelingDTO(statusDTO, fuelings);
        return result;
    }
}
