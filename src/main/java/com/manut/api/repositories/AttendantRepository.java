package com.manut.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.Attendant;


public interface AttendantRepository extends JpaRepository<Attendant, Long> {


    @Query("SELECT obj FROM Attendant obj WHERE obj.externalId = :externalId AND obj.client.id = :clientId")
    Optional<Attendant> searchById(@Param("externalId") Long externalId, @Param("clientId") Long clientId);
    
}
