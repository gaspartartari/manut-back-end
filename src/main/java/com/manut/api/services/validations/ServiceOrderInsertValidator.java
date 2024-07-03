package com.manut.api.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.manut.api.dto.CreateServiceOrderDTO;
import com.manut.api.dto.exceptions.FieldMessage;
import com.manut.api.entities.Garage;
import com.manut.api.entities.Plan;
import com.manut.api.entities.Task;
import com.manut.api.entities.Vehicle;
import com.manut.api.projections.PlanVehicleProjection;
import com.manut.api.repositories.GarageRepository;
import com.manut.api.repositories.PlanRepository;
import com.manut.api.repositories.PlanVehicleRepository;
import com.manut.api.repositories.TaskRepository;
import com.manut.api.repositories.VehicleRepository;
import com.manut.api.services.AuthService;

public class ServiceOrderInsertValidator
        implements ConstraintValidator<ServiceOrderInsertValid, CreateServiceOrderDTO> {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanVehicleRepository planVehicleRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Override
    public void initialize(ServiceOrderInsertValid ann) {
    }

    @Override
    public boolean isValid(CreateServiceOrderDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();
        Long clientId = authService.getClientId();
        Optional<Vehicle> vehicle = vehicleRepository.searchById(dto.getVehicleId(), clientId);
        List<PlanVehicleProjection> planVehicle = planVehicleRepository.searchPlansByVehicleId(dto.getVehicleId(), clientId);
        List<Long> planIds = planVehicle.stream().map(x -> x.getPlanId()).toList();

        if (vehicle.isEmpty())
            list.add(new FieldMessage("Vehicle", "Vehicle not found"));

        if (!planIds.contains(dto.getPlanId()))
            list.add(new FieldMessage("Plan",
                    "Plan id " + dto.getPlanId() + " not mapped to vehicle id " + dto.getVehicleId()));

        Optional<Plan> plan = planRepository.findById(dto.getPlanId());
        if (plan.isEmpty())
            list.add(new FieldMessage("Plan", "Plan id " + dto.getPlanId() + " not found"));
        if(clientId != plan.get().getClient().getId())
            list.add(new FieldMessage("Plan", "Plan id " + dto.getPlanId() + " not mapped to client id " + clientId));

        Optional <Garage> garage = garageRepository.findById(dto.getGarageId());
        if(garage.isEmpty())
            list.add(new FieldMessage("Garage", "Garage not found"));

        if(clientId != garage.get().getClient().getId())
            list.add(new FieldMessage("Garage", "Garage id " + dto.getGarageId() + " not mapped to client id " + clientId));


        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}