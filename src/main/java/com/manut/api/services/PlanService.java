package com.manut.api.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.manut.api.dto.MapTasksDTO;
import com.manut.api.dto.MapVehicleDTO;
import com.manut.api.dto.PlanDTO;
import com.manut.api.dto.PlanVehicleDTO;
import com.manut.api.entities.FuelingSyncedEvent;
import com.manut.api.entities.Plan;
import com.manut.api.entities.PlanVehicle;
import com.manut.api.entities.PlanVehiclePK;
import com.manut.api.entities.Task;
import com.manut.api.entities.Vehicle;
import com.manut.api.enums.MaintenanceStatus;
import com.manut.api.enums.RecurrenceType;
import com.manut.api.projections.PlanVehicleProjection;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.PlanRepository;
import com.manut.api.repositories.PlanVehicleRepository;
import com.manut.api.repositories.TaskRepository;
import com.manut.api.repositories.VehicleRepository;
import com.manut.api.services.exceptions.DatabaseException;
import com.manut.api.services.exceptions.ResourceNotFoundException;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PlanVehicleRepository planVehicleRepository;

    private static final Logger logger = LoggerFactory.getLogger(PlanService.class);

    @Transactional
    public PlanDTO insertPlan(PlanDTO planDTO) {
        logger.info("Insert plan with name: {}", planDTO.getName());
        Boolean isValidTolerance = planDTO.getTolerance() >= planDTO.getRecurrenceInterval() ? false : true;    
        if(!isValidTolerance) throw new DatabaseException("Tolerance cannot be greater than recurrence interval");
        Plan plan = new Plan();
        plan.setName(planDTO.getName());
        plan.setTolerance(planDTO.getTolerance());
        plan.setIsActive(planDTO.getIsActive());
        plan.setRecurrenceInterval(planDTO.getRecurrenceInterval());
        plan.setRecurrenceType(planDTO.getRecurrenceType());
        plan.setIsRecurrent(planDTO.getIsRecurrent());
        Long clientId = authService.getClientId();
        plan.setClient(clientRepository.findById(clientId).get());
        logger.debug("Client id: {}", clientId);
        planRepository.save(plan);
        logger.debug("Plan id: {}", plan.getId());
        return new PlanDTO(plan);
    }

    @Transactional(readOnly = true)
    public Page<PlanDTO> findAll(Pageable pageable) {
        Long clientId = authService.getClientId();
        logger.debug("Client id: {}", clientId);
        Page<Plan> result = planRepository.findAllPlans(clientId, pageable);
        return result.map(x -> new PlanDTO(x));
    }

    @Transactional(readOnly = true)
    public PlanDTO findById(Long id) {
        logger.debug("Find plan with id: {}", id);
        Plan result = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan " + id + " not found"));
        authService.validateClient(result.getClient().getId());
        return new PlanDTO(result);
    }

    @Transactional
    public PlanDTO update(Long id, PlanDTO planDTO) {
        logger.debug("Update plan with id: {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity " + id + " not found"));
        authService.validateClient(plan.getClient().getId());
        plan.setName(planDTO.getName());
        plan.setTolerance(planDTO.getTolerance());
        plan.setIsActive(planDTO.getIsActive());
        plan.setRecurrenceInterval(planDTO.getRecurrenceInterval());
        plan.setRecurrenceType(planDTO.getRecurrenceType());
        plan.setIsRecurrent(planDTO.getIsRecurrent());

        planRepository.save(plan);
        return new PlanDTO(plan);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        logger.debug("Delete plan with id: {}", id);
        if (!planRepository.existsById(id))
            throw new ResourceNotFoundException("Resource not found: " + id); 
        try {
            Plan plan = planRepository.findById(id).get();
            if (plan.getVehicles() != null) throw new DatabaseException("Plan has associated vehicles");
            authService.validateClient(plan.getClient().getId());
            planRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential constraint violation");
        }
    }

    @Transactional
    public PlanDTO mapTasksToPlan(Long planId, MapTasksDTO tasksDTO) {
        logger.debug("Map tasks to plan with id: {}", planId);
        if (!planRepository.existsById(planId))
            throw new ResourceNotFoundException("Resource not found:" + planId);

        Long clientId = authService.getClientId();
        Plan plan = planRepository.findById(planId).get();
        authService.validateClient(plan.getClient().getId());
        List<Long> ids = new ArrayList<>();
        ids.addAll(tasksDTO.getTaskIds());
        List<Task> existingTasks = taskRepository.searchTasksById(ids, clientId);
        Set<Task> tasks = new HashSet<>();
        tasks.addAll(existingTasks);
        plan.setTasks(tasks);
        planRepository.save(plan);
        return new PlanDTO(plan);
    }

    @Transactional
    public PlanDTO mapVehicleToPlan(Long planId, MapVehicleDTO vehicleInfo) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + planId));
        authService.validateClient(plan.getClient().getId());

        Set<Vehicle> mappedVehicles = plan.getVehicles();
        List<Long> mappedExternalIds = mappedVehicles.stream()
                .map(Vehicle::getExternalId)
                .toList();

        if (mappedExternalIds.contains(vehicleInfo.getVehicleId()))
            throw new DatabaseException("Vehicle is already mapped to the plan");

        Vehicle vehicle = vehicleRepository.searchById(vehicleInfo.getVehicleId(), authService.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle " + vehicleInfo.getVehicleId() + " not found"));
        plan.addVehicle(vehicle);

        planRepository.saveAndFlush(plan);
        calculateFirstNextInterval(vehicle, plan, vehicleInfo);

        return new PlanDTO(plan);

    }

    private void calculateFirstNextInterval(Vehicle vehicle, Plan plan, MapVehicleDTO vehicleInfo) {
        PlanVehiclePK pk = new PlanVehiclePK(plan, vehicle);
        Optional<PlanVehicle> planVehicle = planVehicleRepository.findById(pk);
        if (planVehicle.isPresent()) {
            RecurrenceType type = plan.getRecurrenceType();
            switch (type) {
                case ODOMETER:
                    calculateOdometerInterval(vehicleInfo, plan, vehicle, planVehicle.get());
                    break;
                case HOURMETER:
                    calculateHourmeterInterval(vehicleInfo, plan, vehicle, planVehicle.get());
                    break;
                case TIME:
                    calculateTimeInterval(vehicleInfo, plan, vehicle, planVehicle.get());
                    break;
            }
        }
    }

    private void calculateOdometerInterval(MapVehicleDTO vehicleInfo, Plan plan, Vehicle vehicle,
            PlanVehicle planVehicle) {
        Integer currentOdometer = vehicle.getCurrentOdometer() != null ? vehicle.getCurrentOdometer() : 0;
        Integer lastServiceOdometer = vehicleInfo.getLastServiceOdometer() != null
                ? vehicleInfo.getLastServiceOdometer()
                : 0;
        Integer nextServiceOdometer = lastServiceOdometer + plan.getRecurrenceInterval();
        planVehicle.setNextServiceOdometer(nextServiceOdometer);
        planVehicle.setLastServiceOdometer(lastServiceOdometer);
        Integer executeCounter = nextServiceOdometer - currentOdometer;
        planVehicle.setStatus(determineStatus(plan, executeCounter));
        planVehicleRepository.save(planVehicle);
    }

    private void calculateHourmeterInterval(MapVehicleDTO vehicleInfo, Plan plan, Vehicle vehicle,
            PlanVehicle planVehicle) {
        BigDecimal currentHourmeter = vehicle.getCurrentHourmeter() != null ? vehicle.getCurrentHourmeter()
                : BigDecimal.ZERO;
        BigDecimal lastServiceHourmeter = vehicleInfo.getLastServiceHourmeter() != null
                ? vehicleInfo.getLastServiceHourmeter()
                : BigDecimal.ZERO;
        BigDecimal nextServiceHourmeter = lastServiceHourmeter.add(new BigDecimal(plan.getRecurrenceInterval()));
        planVehicle.setNextServiceHourmeter(nextServiceHourmeter);
        planVehicle.setLastServiceHourmeter(lastServiceHourmeter);
        BigDecimal executeCounter = nextServiceHourmeter.subtract(currentHourmeter);
        planVehicle.setStatus(determineStatus(plan, executeCounter));
        planVehicleRepository.save(planVehicle);
    }

    private void calculateTimeInterval(MapVehicleDTO vehicleInfo, Plan plan, Vehicle vehicle, PlanVehicle planVehicle) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastServiceDate = vehicleInfo.getLastServiceDate();
        if (lastServiceDate == null) {
            lastServiceDate = currentDate;
        }
        LocalDate nextServiceDate = lastServiceDate.plusDays(plan.getRecurrenceInterval());
        planVehicle.setNextServiceDate(nextServiceDate);
        planVehicle.setLastServiceDate(lastServiceDate);
        long daysBetween = ChronoUnit.DAYS.between(currentDate, nextServiceDate);
        planVehicle.setStatus(determineStatus(plan, daysBetween));
        planVehicleRepository.save(planVehicle);
    }

    private MaintenanceStatus determineStatus(Plan plan, long executeCounter) {
        if (executeCounter <= plan.getTolerance() && executeCounter > 0) {
            return MaintenanceStatus.ATTENTION;
        } else if (executeCounter <= 0) {
            return MaintenanceStatus.LATE;
        } else {
            return MaintenanceStatus.ON_TIME;
        }
    }

    private MaintenanceStatus determineStatus(Plan plan, BigDecimal executeCounter) {
        if (executeCounter.compareTo(new BigDecimal(plan.getTolerance())) <= 0
                && executeCounter.compareTo(BigDecimal.ZERO) > 0) {
            return MaintenanceStatus.ATTENTION;
        } else if (executeCounter.compareTo(BigDecimal.ZERO) <= 0) {
            return MaintenanceStatus.LATE;
        } else {
            return MaintenanceStatus.ON_TIME;
        }
    }

    @EventListener
    @Transactional
    public void onFuelingSynced(FuelingSyncedEvent event) {
        for (List<Long> vehicles : event.getVehicleIds()) {
            List<PlanVehicleProjection> projection = planVehicleRepository.searchPlansByVehicleId(vehicles.get(0),
                    vehicles.get(1));
            if (projection != null && projection.size() != 0)
                for (PlanVehicleProjection p : projection) {
                    Integer recurrenceType = Integer.parseInt(p.getRecurrenceType().toString());
                    switch (recurrenceType) {
                        case 0:
                            checkOdometerPlan(p);
                            break;

                        case 1:
                            checkHourmeterPlan(p);

                        case 2:
                            checkTimePlan(p);
                        default:
                            break;
                    }
                }

        }
    }

    private void checkTimePlan(PlanVehicleProjection projection) {
        PlanVehiclePK pk = new PlanVehiclePK(planRepository.findById(projection.getPlanId()).get(),
                vehicleRepository.searchById(projection.getExternalId(), projection.getClientId()).get());
        PlanVehicle planVehicle = planVehicleRepository.findById(pk).get();
        LocalDate currentDate = LocalDate.now();
        LocalDate dueDate = projection.getNextServiceDate();
        long daysBetween = ChronoUnit.DAYS.between(currentDate, dueDate);
        planVehicle.setStatus(determineStatus(planRepository.findById(projection.getPlanId()).get(), daysBetween));
        planVehicleRepository.save(planVehicle);
    }

    private void checkHourmeterPlan(PlanVehicleProjection projection) {
        PlanVehiclePK pk = new PlanVehiclePK(planRepository.findById(projection.getPlanId()).get(),
                vehicleRepository.searchById(projection.getExternalId(), projection.getClientId()).get());
        PlanVehicle planVehicle = planVehicleRepository.findById(pk).get();
        BigDecimal executionCounter = projection.getNextServiceHourmeter().subtract(projection.getCurrentHourmeter());
        planVehicle.setStatus(determineStatus(planRepository.findById(projection.getPlanId()).get(), executionCounter));
        planVehicleRepository.save(planVehicle);
    }

    private void checkOdometerPlan(PlanVehicleProjection projection) {
        PlanVehiclePK pk = new PlanVehiclePK(planRepository.findById(projection.getPlanId()).get(),
                vehicleRepository.searchById(projection.getExternalId(), projection.getClientId()).get());
        PlanVehicle planVehicle = planVehicleRepository.findById(pk).get();

        long executionCounter = projection.getNextServiceOdometer() - projection.getCurrentOdometer();
        planVehicle.setStatus(determineStatus(planRepository.findById(projection.getPlanId()).get(), executionCounter));
        planVehicleRepository.save(planVehicle);
    }

    @Transactional(readOnly = true)
    public List<PlanVehicleProjection> dashobard() {
        List<PlanVehicleProjection> result = planVehicleRepository.dashoardList(authService.getClientId());
        return result;
    }
}
