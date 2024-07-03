package com.manut.api.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.manut.api.dto.CreateServiceOrderDTO;
import com.manut.api.dto.FinishServiceOrderDTO;
import com.manut.api.dto.MapTasksDTO;
import com.manut.api.dto.ServiceOrderDTO;
import com.manut.api.entities.Client;
import com.manut.api.entities.ServiceOrder;
import com.manut.api.entities.ServiceTask;
import com.manut.api.entities.Task;
import com.manut.api.enums.OrderStatus;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.GarageRepository;
import com.manut.api.repositories.ServiceOrderRepository;
import com.manut.api.repositories.ServiceTaskRepository;
import com.manut.api.repositories.TaskRepository;
import com.manut.api.repositories.PlanRepository;
import com.manut.api.repositories.VehicleRepository;
import com.manut.api.services.exceptions.DatabaseException;
import com.manut.api.services.exceptions.ResourceNotFoundException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrdeRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceTaskRepository serviceTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public ServiceOrderDTO createServiceOrder(CreateServiceOrderDTO insertDTO) {
        Long clientId = authService.getClientId();
        Client client = clientRepository.getReferenceById(clientId);
        ServiceOrder entity = new ServiceOrder();
        entity.setClient(client);
        entity = serviceOrdeRepository.saveAndFlush(entity);
        populateServiceOrderEntityFromDTO(entity, insertDTO, clientId, entity.getId());
        entity = serviceOrdeRepository.save(entity);
        return new ServiceOrderDTO(entity);
    }

    @Transactional
    public ServiceOrderDTO insertTask(MapTasksDTO taskList, Long serviceOrderId) {

        ServiceOrder entity = serviceOrdeRepository.findById(serviceOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + serviceOrderId + " not found"));

        authService.validateClient(entity.getClient().getId());
        List<Long> entityTaskIds = entity.getServiceTasks().stream().map(x -> x.getTask().getId())
                .collect(Collectors.toList());

        for (Long id : taskList.getTaskIds())
            if (entityTaskIds.contains(id))
                throw new DatabaseException("Task " + id + " is already in the service order");

        for (Long id : taskList.getTaskIds()) {
            Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Task " + id + " not found"));
            authService.validateClient(task.getClient().getId());
            ServiceTask serviceTask = new ServiceTask();
            serviceTask.setTask(task);
            serviceTask.setService(entity);
            serviceTask.setClient(entity.getClient());
            serviceTask.setCompleted(false);
            serviceTask = serviceTaskRepository.save(serviceTask);
            entity.getServiceTasks().add(serviceTask);
        }

        entity = serviceOrdeRepository.save(entity);
        return new ServiceOrderDTO(entity);

    }

    /*
     * // talvez deletar
     * 
     * @Transactional
     * public ServiceOrderDTO updateServiceOrder(InsertTaskServiceOrderDTO
     * updateDTO, Long id) {
     * ServiceOrder entity = serviceOrdeRepository.findById(id)
     * .orElseThrow(() -> new ResourceNotFoundException("Order " + id +
     * " not found"));
     * authService.validateClient(entity.getClient().getId());
     * if (!updateDTO.getUpdatedTaskList().isEmpty()) {
     * List<Long> existingTaskIds = entity.getServiceTasks().stream().map(x ->
     * x.getTask().getId())
     * .collect(Collectors.toList());
     * for (int i = 0; i < existingTaskIds.size() - 1; i++) {
     * if (updateDTO.getUpdatedTaskList().contains(existingTaskIds.get(i))) {
     * continue;
     * } else {
     * 
     * entity = serviceOrdeRepository.saveAndFlush(entity);
     * }
     * }
     * existingTaskIds = entity.getServiceTasks().stream().map(x ->
     * x.getTask().getId())
     * .collect(Collectors.toList());
     * for (Long taskId : updateDTO.getUpdatedTaskList()) {
     * if (!existingTaskIds.contains(taskId)) {
     * ServiceTask serviceTask = new ServiceTask();
     * serviceTask.setClient(entity.getClient());
     * Task task = taskRepository.findById(id).orElseThrow(() -> new
     * ResourceNotFoundException(
     * "Task " + id + " not found"));
     * authService.validateClient(task.getClient().getId());
     * serviceTask.setTask(task);
     * serviceTask.setService(entity);
     * serviceTask.setCompleted(false);
     * entity.getServiceTasks().add(serviceTask);
     * }
     * }
     * }
     * entity = serviceOrdeRepository.save(entity);
     * return new ServiceOrderDTO(entity);
     * }
     */

    private void populateServiceOrderEntityFromDTO(ServiceOrder order, CreateServiceOrderDTO dto, Long clientId,
            Long orderId) {
        order.setPlan(planRepository.findById(dto.getPlanId()).get());
        order.setVehicle(vehicleRepository.searchById(dto.getVehicleId(), clientId).get());
        Set<ServiceTask> serviceTasks = new HashSet<>();
        for (Task task : planRepository.findById(dto.getPlanId()).get().getTasks()) {
            ServiceTask serviceTask = new ServiceTask();
            serviceTask.setClient(clientRepository.findById(clientId).get());
            serviceTask.setTask(task);
            serviceTask.setService(serviceOrdeRepository.findById(orderId).get());
            serviceTask.setCompleted(false);
            serviceTasks.add(serviceTask);
            serviceTaskRepository.save(serviceTask);
        }
        order.setStatus(OrderStatus.APPROVED);
        order.setServiceTasks(serviceTasks);
        order.setGarage(garageRepository.findById(dto.getGarageId()).get());


    }

    @Transactional
    public ServiceOrderDTO finishServiceOrder(Long id, FinishServiceOrderDTO finishServiceOrderDTO) {

        ServiceOrder so = serviceOrdeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + id + " not found"));

        authService.validateClient(so.getClient().getId());

        if (so.getStatus() == OrderStatus.COMPLETED)
            throw new DatabaseException("The service order " + id + " is already finished");
        so = setTaskCompletion(so, finishServiceOrderDTO);

        switch (so.getPlan().getRecurrenceType()) {
            case ODOMETER:
                Integer finishOdometer = finishServiceOrderDTO.getFinishCounter() == null
                        ? so.getVehicle().getCurrentOdometer()
                        : finishServiceOrderDTO.getFinishCounter();
                if (finishOdometer < so.getVehicle().getCurrentOdometer()
                        || finishOdometer > so.getVehicle().getCurrentOdometer() + 2000)
                    throw new DatabaseException("!Safety trigger. Invalid finish counter value.");
                so.setConclusionCounter(finishOdometer);
                break;

            case HOURMETER:
                BigDecimal finishHourmeter = finishServiceOrderDTO.getFinishCounter() == null
                        ? so.getVehicle().getCurrentHourmeter()
                        : new BigDecimal(finishServiceOrderDTO.getFinishCounter());
                if (finishHourmeter.compareTo(new BigDecimal(so.getVehicle().getCurrentOdometer())) < 0
                        || finishHourmeter.compareTo(new BigDecimal(so.getVehicle().getCurrentOdometer() + 100)) > 0)
                    throw new DatabaseException("!Safety trigger. Invalid finish counter value.");
                so.setConclusionCounter(Integer.parseInt(finishHourmeter.toString()));
                break;

            case TIME:
                break;

            default:
                break;
        }

        so.setConclusionDate(LocalDate.now());
        so.setStatus(OrderStatus.COMPLETED);
        so.setServiceCost(finishServiceOrderDTO.getServiceCost());
        so.setMaterialCost(finishServiceOrderDTO.getMaterialCost());
        so.setTotalCost(so.getTotalCost());
        so = serviceOrdeRepository.save(so);
        return new ServiceOrderDTO(so);
    }

    private ServiceOrder setTaskCompletion(ServiceOrder so, FinishServiceOrderDTO finishServiceOrderDTO) {

        // Verifica se todas as tarefas da OS constam no dto
        if (finishServiceOrderDTO.getTasks().size() != so.getServiceTasks().size())
            throw new DatabaseException("Invalid tasks list");
        
        // Verifica se são as mesmas tarefas
        for (Map.Entry<String, Boolean> entry : finishServiceOrderDTO.getTasks().entrySet()) {
            Long taskId = Long.parseLong(entry.getKey());
            if (!so.getServiceTasks().stream().map(st -> st.getTask().getId()).collect(Collectors.toList()).contains(taskId))
                throw new DatabaseException("Invalid tasks list");
        }

        // Verifica se todas as tarefas do dto não contem chaves ou valores vazios
        for (Map.Entry<String, Boolean> entry : finishServiceOrderDTO.getTasks().entrySet()) {
            if (entry.getKey().isEmpty() || entry.getValue() == null)
                throw new DatabaseException("Invalid tasks list");
        }

        // Mapa de ServiceTask ID para Task ID
        Map<Long, Long> idsMap = so.getServiceTasks().stream()
                .collect(Collectors.toMap(ServiceTask::getId, st -> st.getTask().getId()));

        // Lista de Task IDs
        List<Long> taskIds = so.getServiceTasks().stream()
                .map(st -> st.getTask().getId())
                .collect(Collectors.toList());

        // Iterando sobre os dados fornecidos
        for (Map.Entry<String, Boolean> entry : finishServiceOrderDTO.getTasks().entrySet()) {
            Long taskId = Long.parseLong(entry.getKey());
            if (!taskIds.contains(taskId)) {
                throw new ResourceNotFoundException("Task " + taskId + " not found in the service order");
            }

            // Encontrar ServiceTask pela taskId
            ServiceTask st = serviceTaskRepository.findById(idsMap.entrySet().stream()
                    .filter(e -> e.getValue().equals(taskId))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("ServiceTask not found for Task ID: " + taskId)))
                    .get();
            authService.validateClient(st.getClient().getId());

            // Atualizar estado de conclusão
            st.setCompleted(entry.getValue());
            serviceTaskRepository.save(st);
        }

        // Salvar ServiceOrder
        return serviceOrdeRepository.saveAndFlush(so);
    }

    @Transactional(readOnly = true)
    public Page<ServiceOrderDTO> findAll(Pageable pageable) {
        Page<ServiceOrder> result = serviceOrdeRepository.findAllServiceOrders(authService.getClientId(), pageable);
        return result.map(x -> new ServiceOrderDTO(x));
    }
}
