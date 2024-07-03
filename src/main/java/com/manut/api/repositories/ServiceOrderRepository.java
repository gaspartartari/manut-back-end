package com.manut.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manut.api.entities.ServiceOrder;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    

    @Query("SELECT obj FROM ServiceOrder obj JOIN FETCH obj.client WHERE obj.client.id = :clientId")
    Page<ServiceOrder> findAllServiceOrders(Long clientId, Pageable pageable);
}
