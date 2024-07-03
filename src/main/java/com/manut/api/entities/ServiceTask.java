package com.manut.api.entities;

import com.manut.api.enums.TaskIncompleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "tb_service_task")
public class ServiceTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceOrder service;

    private boolean isCompleted;

    private TaskIncompleteAction taskIncompleteAction;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    public ServiceTask() {

    }

    public ServiceTask(Long id, Task task, ServiceOrder service, Client client) {
        this.id = id;
        this.task = task;
        this.service = service;
        this.isCompleted = false;
        this.client = client;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceTask other = (ServiceTask) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
