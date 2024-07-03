package com.manut.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manut.api.dto.CardsDTO;
import com.manut.api.entities.PlanVehicle;
import com.manut.api.enums.MaintenanceStatus;
import com.manut.api.projections.MainTableProjection;
import com.manut.api.repositories.PlanRepository;
import com.manut.api.repositories.PlanVehicleRepository;

@Service
public class DashboardService {
    

    @Autowired
    private PlanVehicleRepository planVehicleRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public CardsDTO mainCards(){
         
        List<PlanVehicle> result =planVehicleRepository.findPlanVehicleListByClientId(authService.getClientId());
        int countOnTime = 0;
        int countInProgress= 0;
        int countLate = 0;
        for (PlanVehicle pv : result){
            if (pv.getStatus().equals(MaintenanceStatus.ON_TIME)){
                countOnTime++;
            }else if (pv.getStatus().equals(MaintenanceStatus.IN_PROGRESS)){
                countInProgress++;
            }else if (pv.getStatus().equals(MaintenanceStatus.LATE)){
                countLate++;
            }
        }

        CardsDTO cardsDTO = new CardsDTO();
        cardsDTO.setTotal(result.size());
        double onTimePercentage = countOnTime*100/result.size();
        double latePercentage = countLate*100/result.size();
        double inProgressPercentage = countInProgress*100/result.size();
        cardsDTO.setOnTimePercentage(onTimePercentage);
        cardsDTO.setLatePercentage(latePercentage);
        cardsDTO.setInProgressPercentage(inProgressPercentage);

        return cardsDTO;
    }

    @Transactional(readOnly = true)
    public Page<MainTableProjection> mainTable(Pageable pageable, String planId, String maintenanceStatus, String licensePlate) {
        Long pId = null;
        if(!"0".equals(planId))
            pId = Long.parseLong(planId);
        Long mStatus = null;
        if(!"0".equals(maintenanceStatus)){
            if(maintenanceStatus.equalsIgnoreCase("onTime"))
                mStatus = 0L;
            else if(maintenanceStatus.equalsIgnoreCase("attention"))
                mStatus = 1L;
            else if(maintenanceStatus.equalsIgnoreCase("inprogress"))
                mStatus = 2L;
            else if(maintenanceStatus.equalsIgnoreCase("late"))
                mStatus = 3L;
        }

        Page<MainTableProjection> result = planRepository.mainTable(authService.getClientId(),pageable, pId, mStatus, licensePlate);
        return result;

    }

}
