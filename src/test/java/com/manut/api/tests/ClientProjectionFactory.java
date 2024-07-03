package com.manut.api.tests;

import com.manut.api.projections.ClientProjection;

public class ClientProjectionFactory {

    public static ClientProjection createClientProjection(String username) {
      
        return new ClientProjectionImp(1L, "token");
    }
}

class ClientProjectionImp implements ClientProjection {

    private Long id;
    private String token;

    public ClientProjectionImp() {
    }

    public ClientProjectionImp(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getToken() {
        return token;
    }
}
