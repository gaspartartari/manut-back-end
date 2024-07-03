package com.manut.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.Company;


public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT obj FROM Company obj WHERE obj.externalId = :externalId AND obj.client.id = :clientId")
    Optional<Company> searchById(@Param("externalId") Long externalId, @Param("clientId") Long clientId);
    
}
