package com.manut.api.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface MainTableProjection {
    
    Long getPlanId();
    String getPlanName();
    String getLicensePlate();
    Long getType();
    Long getRecurrenceInterval();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate getLastServiceDate();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate getNextServiceDate();
    
    Integer getLastServiceOdometer();
    Integer getNextServiceOdometer();
    BigDecimal getLastServiceHourmeter();
    BigDecimal getNextServiceHourmeter();
    Integer getCurrentOdometer();
    BigDecimal getCurrentHourmeter();
    Long getStatus();
}
