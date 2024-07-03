package com.manut.api.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.manut.api.dto.MapTasksDTO;
import com.manut.api.dto.exceptions.FieldMessage;
import com.manut.api.entities.Task;
import com.manut.api.repositories.TaskRepository;
import com.manut.api.services.AuthService;

public class TaskInsertValidator implements ConstraintValidator<TaskInsertValid, MapTasksDTO> {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private AuthService authService;

	@Override
	public void initialize(TaskInsertValid ann) {
	}

	@Override
	public boolean isValid(MapTasksDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();
		Long clientId = authService.getClientId();
		for(Long id : dto.getTaskIds()){
			Optional<Task> task = taskRepository.searchById(id, clientId);
			if(!task.isPresent())
				list.add(new FieldMessage("id", "Task id " + id + " not found"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}