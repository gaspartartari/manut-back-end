package com.manut.api.dto;


import java.util.ArrayList;
import java.util.List;

import com.manut.api.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> authorities = new ArrayList<>();
    
    public UserDTO(){

    }

    public UserDTO(User entity){
        id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        if(entity.getRoles() != null){
            entity.getRoles().forEach(role -> this.authorities.add(role.getAuthority()));
        }
    }
}


