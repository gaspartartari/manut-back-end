package com.manut.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT obj FROM Task obj JOIN FETCH obj.client WHERE obj.client.id = :clientId")
    Page<Task> findAllTasks(Long clientId, Pageable pageable);

    @Query("SELECT obj FROM Task obj JOIN FETCH obj.client WHERE obj.client.id = :clientId AND obj.id IN :tasksId")
    List<Task> searchTasksById(@Param("tasksId") List<Long> tasksId, @Param("clientId") Long clientId);

    @Query("SELECT obj FROM Task obj WHERE obj.id = :id AND obj.client.id = :clientId")
    Optional<Task> searchById(@Param("id") Long id, @Param("clientId") Long clientId);
}
