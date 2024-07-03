package com.manut.api.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PlanVehicleProjection {
    
    //SELECT pv.plan_id, pv.vehicle_id, v.current_odometer, pv.last_service_odometer, pv.next_service_odometer, pv.status, p.is_recurrent, p.tolerance, p.recurrence_interval 
    Long getPlanId();
    Long getVehicleId();
    Long getExternalId();
    Long getRecurrenceType();
    BigDecimal getCurrentHourmeter();
    BigDecimal getLastServiceHourmeter();
    BigDecimal getNextServiceHourmeter();
    Integer getCurrentOdometer();
    Integer getLastServiceOdometer();
    Integer getNextServiceOdometer();
    LocalDate getLastServiceDate();
    LocalDate getNextServiceDate();
    Long getStatus();
    Boolean getIsRecurrent();
    Integer getTolerance();
    Integer getRecurrenceInterval();
    Long getClientId();
}
