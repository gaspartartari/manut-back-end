package com.manut.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manut.api.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
