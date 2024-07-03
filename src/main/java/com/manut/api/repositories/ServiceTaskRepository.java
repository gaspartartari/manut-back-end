package com.manut.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manut.api.entities.ServiceTask;

public interface ServiceTaskRepository extends JpaRepository<ServiceTask, Long> {
    
}
