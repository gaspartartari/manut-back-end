package com.manut.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.Fuel;


public interface FuelRepository extends JpaRepository<Fuel, Long> {


    @Query("SELECT obj FROM Fuel obj WHERE obj.externalId = :externalId AND obj.client.id = :clientId")
    Optional<Fuel> searchById(@Param("externalId") Long externalId, @Param("clientId") Long clientId);
    
}
