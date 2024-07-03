package com.manut.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manut.api.entities.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {    
} 
