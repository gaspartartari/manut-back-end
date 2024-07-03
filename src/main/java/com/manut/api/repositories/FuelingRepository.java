package com.manut.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manut.api.entities.Fueling;

public interface FuelingRepository extends JpaRepository<Fueling, Long> {
    
}
