package com.manut.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manut.api.entities.Client;
import com.manut.api.projections.ClientProjection;

public interface ClientRepository extends JpaRepository<Client, Long> {


    @Query( nativeQuery = true, value = "SELECT id, token FROM TB_CLIENT  ")
    List<ClientProjection> findAllTokens();

    
}
 