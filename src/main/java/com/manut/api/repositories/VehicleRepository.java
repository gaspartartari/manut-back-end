package com.manut.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
 
    
    @Query("SELECT obj FROM Vehicle obj JOIN FETCH obj.client WHERE obj.client.id = :clientId")
    Page<Vehicle> findAllVehicles(Long clientId, Pageable pageable);


    @Query("SELECT obj FROM Vehicle obj WHERE obj.externalId = :externalId AND obj.client.id = :clientId")
    Optional<Vehicle> searchById(@Param("externalId") Long externalId, @Param("clientId") Long clientId);

    @Query("SELECT obj FROM Vehicle obj JOIN FETCH obj.client WHERE obj.client.id = :clientId AND obj.externalId IN :vehiclesId")
    List<Vehicle> searchVehiclesById(@Param("vehiclesId") List<Long> vehiclesId, @Param("clientId") Long clientId);
}
