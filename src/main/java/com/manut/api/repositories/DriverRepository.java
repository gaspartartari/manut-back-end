package com.manut.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.Driver;


public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("SELECT obj FROM Driver obj WHERE obj.externalId = :externalId AND obj.client.id = :clientId")
    Optional<Driver> searchById(@Param("externalId") Long externalId, @Param("clientId") Long clientId);
    
}
