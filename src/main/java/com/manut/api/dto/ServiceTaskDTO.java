package com.manut.api.dto;

import com.manut.api.entities.ServiceTask;
import com.manut.api.enums.TaskIncompleteAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor

@Getter 
@Setter

public class ServiceTaskDTO {

    private Long id;

    private TaskMinDTO task;

    // private Long serviceOrderId;

    private TaskIncompleteAction taskIncompleteAction;

    private boolean isCompleted;

    public ServiceTaskDTO(ServiceTask entity){
        id = entity.getId();
        task = new TaskMinDTO(entity.getTask());
        //serviceOrderId = entity.getService().getId();
        isCompleted = entity.isCompleted();
        taskIncompleteAction = entity.getTaskIncompleteAction() == null ? null : entity.getTaskIncompleteAction();
    }
}
