package com.manut.api.entities;
import java.util.List;

import org.springframework.context.ApplicationEvent;

public class FuelingSyncedEvent extends ApplicationEvent {
    private List<List<Long>> vehicleIds;

    public FuelingSyncedEvent(Object source, List<List<Long>> vehicleIds) {
        super(source);
        this.vehicleIds = vehicleIds;
    }

    public List<List<Long>> getVehicleIds() {
        return vehicleIds;
    }
}

