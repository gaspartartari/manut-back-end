package com.manut.api.tests;

import com.manut.api.entities.Client;

public class ClientFactory {
    
    public static Client createClient (){
        Client client = new Client(1L, "client1 ", "token");
        return client;
    }
}
