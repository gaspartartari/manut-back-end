package com.manut.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.Pump;

public interface PumpRepository extends JpaRepository<Pump, Long> {
    @Query("SELECT obj FROM Pump obj WHERE obj.externalId = :externalId AND obj.client.id = :clientId")   
    Optional<Pump> searchById(@Param("externalId") Long externalId, @Param("clientId") Long clientId);
    
}
