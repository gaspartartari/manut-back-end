package com.manut.api.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.manut.api.dto.MapTasksDTO;
import com.manut.api.dto.MapVehicleDTO;
import com.manut.api.dto.PlanDTO;
import com.manut.api.entities.Client;
import com.manut.api.entities.Plan;
import com.manut.api.entities.PlanVehicle;
import com.manut.api.entities.PlanVehiclePK;
import com.manut.api.entities.Task;
import com.manut.api.entities.Vehicle;
import com.manut.api.enums.RecurrenceType;
import com.manut.api.projections.PlanVehicleProjection;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.PlanRepository;
import com.manut.api.repositories.PlanVehicleRepository;
import com.manut.api.repositories.TaskRepository;
import com.manut.api.repositories.VehicleRepository;
import com.manut.api.services.exceptions.DatabaseException;
import com.manut.api.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class PlanServiceTests {

    @InjectMocks
    private PlanService planService;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AuthService authService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PlanVehicleRepository planVehicleRepository;

    private Plan plan;
    private PlanDTO planDTO;
    private Long existingClientId;
    private Long existingPlanId;
    private Client client;

    @BeforeEach
    void setup() throws Exception {
        existingClientId = 1L;
        existingPlanId = 1L;
        client = new Client();
        client.setId(existingClientId);

        plan = new Plan();
        plan.setId(existingPlanId);
        plan.setClient(client);

        planDTO = new PlanDTO();
        planDTO.setName("Test Plan");
        planDTO.setTolerance(10);
        planDTO.setRecurrenceInterval(5);
        planDTO.setIsActive(true);
        planDTO.setRecurrenceType(RecurrenceType.TIME);
        planDTO.setIsRecurrent(true);

        when(authService.getClientId()).thenReturn(existingClientId);
        when(clientRepository.findById(existingClientId)).thenReturn(Optional.of(client));
        when(planRepository.findById(existingPlanId)).thenReturn(Optional.of(plan));
    }

    @Test
    public void insertPlanShouldSavePlan() {
        Plan savedPlan = new Plan();
        savedPlan.setId(1L);
        when(planRepository.save(any(Plan.class))).thenReturn(savedPlan);

        PlanDTO result = planService.insertPlan(planDTO);

        Assertions.assertNotNull(result);
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    public void insertPlanShouldThrowDatabaseExceptionWhenToleranceIsInvalid() {
        planDTO.setTolerance(10);
        planDTO.setRecurrenceInterval(20);

        Assertions.assertThrows(DatabaseException.class, () -> {
            planService.insertPlan(planDTO);
        });
    }

    @Test
    public void findAllShouldReturnPlanDTOPage() {
        Page<Plan> page = Mockito.mock(Page.class);
        when(planRepository.findAllPlans(anyLong(), any(Pageable.class))).thenReturn(page);

        Pageable pageable = Mockito.mock(Pageable.class);
        planService.findAll(pageable);

        verify(planRepository, times(1)).findAllPlans(existingClientId, pageable);
    }

    @Test
    public void findByIdShouldReturnPlanDTO() {
        PlanDTO result = planService.findById(existingPlanId);

        Assertions.assertNotNull(result);
        verify(planRepository, times(1)).findById(existingPlanId);
        verify(authService, times(1)).validateClient(existingClientId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundException() {
        when(planRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            planService.findById(100L);
        });
    }

    @Test
    public void updateShouldSaveUpdatedPlan() {
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        PlanDTO result = planService.update(existingPlanId, planDTO);

        Assertions.assertNotNull(result);
        verify(planRepository, times(1)).save(any(Plan.class));
        verify(authService, times(1)).validateClient(existingClientId);
    }

    @Test
    public void updateShouldThrowResourceNotFoundException() {
        when(planRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            planService.update(100L, planDTO);
        });
    }

    @Test
    public void deleteShouldRemovePlan() {
        when(planRepository.existsById(anyLong())).thenReturn(true);

        planService.delete(existingPlanId);

        verify(planRepository, times(1)).deleteById(existingPlanId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundException() {
        when(planRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            planService.delete(100L);
        });
    }

    @Test
    public void mapTasksToPlanShouldAddTasksToPlan() {
        MapTasksDTO tasksDTO = new MapTasksDTO();
        tasksDTO.setTaskIds(Set.of(1L, 2L));

        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(2L);
        List<Task> tasks = List.of(task1, task2);

        when(taskRepository.searchTasksById(anyList(), anyLong())).thenReturn(tasks);
        when(planRepository.findById(existingPlanId)).thenReturn(Optional.of(plan));

        PlanDTO result = planService.mapTasksToPlan(existingPlanId, tasksDTO);

        Assertions.assertNotNull(result);
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    public void mapTasksToPlanShouldThrowResourceNotFoundException() {
        when(planRepository.findById(anyLong())).thenReturn(Optional.empty());

        MapTasksDTO tasksDTO = new MapTasksDTO();
        tasksDTO.setTaskIds(Set.of(1L, 2L));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            planService.mapTasksToPlan(100L, tasksDTO);
        });
    }

    @Test
    public void mapVehicleToPlanShouldAddVehicleToPlan() {
        MapVehicleDTO vehicleInfo = new MapVehicleDTO();
        vehicleInfo.setVehicleId(1L);

        Vehicle vehicle = new Vehicle();
        vehicle.setExternalId(1L);
        Set<Vehicle> vehicles = new HashSet<>();
        vehicles.add(vehicle);
        plan.setVehicles(vehicles);

        when(vehicleRepository.searchById(anyLong(), anyLong())).thenReturn(Optional.of(vehicle));

        PlanDTO result = planService.mapVehicleToPlan(existingPlanId, vehicleInfo);

        Assertions.assertNotNull(result);
        verify(planRepository, times(1)).saveAndFlush(any(Plan.class));
    }

    @Test
    public void mapVehicleToPlanShouldThrowResourceNotFoundException() {
        when(planRepository.findById(anyLong())).thenReturn(Optional.empty());

        MapVehicleDTO vehicleInfo = new MapVehicleDTO();
        vehicleInfo.setVehicleId(1L);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            planService.mapVehicleToPlan(100L, vehicleInfo);
        });
    }

    // Adicione mais testes para cobrir outras partes do código

}

