package com.manut.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manut.api.entities.Garage;

public interface GarageRepository extends JpaRepository <Garage, Long> {

    @Query("SELECT obj FROM Garage obj JOIN FETCH obj.client WHERE obj.client.id = :clientId")
    Page<Garage> findAllGarages(Long clientId, Pageable pageable);
    
}
