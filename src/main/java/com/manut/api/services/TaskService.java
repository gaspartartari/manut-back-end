package com.manut.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.manut.api.dto.TaskMinDTO;
import com.manut.api.entities.Task;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.TaskRepository;
import com.manut.api.services.exceptions.ClientNotFoundException;
import com.manut.api.services.exceptions.DatabaseException;
import com.manut.api.services.exceptions.ResourceNotFoundException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public TaskMinDTO insert(TaskMinDTO insertTaskDTO) {
        Task task = new Task();
        mapDtoToEntity(task, insertTaskDTO);
        Long clientId = authService.getClientId();
        if (!clientRepository.existsById(clientId))
            throw new ClientNotFoundException("Client not found");
        task.setClient(clientRepository.findById(clientId).get());
        task = taskRepository.save(task);
        return new TaskMinDTO(task);
    }

    @Transactional(readOnly = true)
    public List<TaskMinDTO> findAll(Pageable pageable) {
        Long clientId = authService.getClientId();
        Page<Task> list = taskRepository.findAllTasks(clientId, pageable);
        return list.map(x -> new TaskMinDTO(x)).toList();
    }

    @Transactional(readOnly = true)
    public TaskMinDTO findById(Long id) {
        Task result = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task " + id + " not found"));
        authService.validateClient(result.getClient().getId());
        return new TaskMinDTO(result);
    }

    @Transactional
    public TaskMinDTO update(Long id, TaskMinDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found"));
        authService.validateClient(task.getClient().getId());
        mapDtoToEntity(task, taskDTO);
        task = taskRepository.save(task);
        return new TaskMinDTO(task);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!taskRepository.existsById(id))
            throw new ResourceNotFoundException("Resource not found: " + id);
        try {
            Task task = taskRepository.findById(id).get();
            authService.validateClient(task.getClient().getId());
            taskRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential constraint violation");
        }
    }

    private void mapDtoToEntity(Task entity, TaskMinDTO dto) {
        entity.setName(dto.getName());
        entity.setComponent(dto.getComponent());
        entity.setObservation(dto.getObservation());
        entity.setExecutor(dto.getExecutor());
    }
}
