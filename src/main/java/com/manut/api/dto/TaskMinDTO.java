package com.manut.api.dto;

import java.util.HashSet;
import java.util.Set;

import com.manut.api.entities.Task;
import com.manut.api.enums.ExecutorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskMinDTO  {

    private Long id;

    @NotBlank(message = "Name canont be blank")
    @Size(min = 5, message = "Name must have at least 5 characters")
    private String name;

    @NotBlank(message = "Component cannot be blank")
    private String component;
    private String observation;

    @NotNull(message = "Executor cannot be null")
    private ExecutorType executor;


    public TaskMinDTO() {
    }

    public TaskMinDTO(Task entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.component = entity.getComponent();
        this.observation = entity.getObservation();
        this.executor = entity.getExecutor();
    }
}
