package com.manut.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manut.api.entities.User;
import com.manut.api.projections.ClientProjection;
import com.manut.api.repositories.UserRepository;
import com.manut.api.services.exceptions.ClientNotFoundException;
import com.manut.api.services.exceptions.ForbiddenException;
import com.manut.api.utils.CustomUserUtil;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserUtil customUserUtil;

    @Autowired
    private UserRepository userRepository;

    public void validateClient(Long clientId) {
        User user = userService.authenticated();
        if (user.getClient().getId() != clientId) {
            throw new ForbiddenException("Unauthorized");
        }
    }

    public Long getClientId() {
        try {
            String username = customUserUtil.getLoggedUser();
            ClientProjection proj = userRepository.findClientIdByUserEmail(username);
            Long clientId = proj.getId();
            return clientId;
        } catch (Exception e) {
            logger.error("Problem in the getClientId method from AuthService class", e);
            throw new ClientNotFoundException("Client not found for logged user");
        }
    
    }
}
