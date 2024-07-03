package com.manut.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manut.api.repositories.PlanVehicleRepository;

@Service
public class PlanVehicleService {
    
    @Autowired
    private PlanVehicleRepository planVehicleRepository;
}
