package com.manut.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.Station;


public interface StationRepository extends JpaRepository<Station, Long> {

    @Query("SELECT obj FROM Station obj WHERE obj.externalId = :externalId AND obj.client.id = :clientId")
    Optional<Station>searchById(@Param("externalId") Long externalId, @Param("clientId") Long clientId);
    
}
