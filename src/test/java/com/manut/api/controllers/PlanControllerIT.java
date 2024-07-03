package com.manut.api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manut.api.dto.PlanDTO;
import com.manut.api.enums.RecurrenceType;
import com.manut.api.services.TokenUtil;

import net.minidev.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PlanControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String usernameC1;
    private String passwordC1;
    private String bearerTokenC1, invalidToken;

    @BeforeEach
    public void setup() throws Exception {
        usernameC1 = "gaspar@gmail.com";
        passwordC1 = "123456";
        bearerTokenC1 = tokenUtil.obtainAccessToken(mockMvc, usernameC1, passwordC1);
        invalidToken = bearerTokenC1 + "abc";
    }

    @Test
    public void insertPlanShouldReturnCreatedWhenValidData() throws Exception {

        PlanDTO dto = new PlanDTO();
        dto.setName("new plan");
        dto.setRecurrenceInterval(1000);
        dto.setRecurrenceType(RecurrenceType.ODOMETER);
        dto.setIsRecurrent(true);
        dto.setIsActive(true);
        dto.setTolerance(250);

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(post("/plans")
                .header("Authorization", "Bearer " + bearerTokenC1)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.name").value("new plan"));
        result.andExpect(jsonPath("$.recurrenceInterval").value(1000));
        result.andExpect(jsonPath("$.recurrenceType").value("ODOMETER"));
        result.andExpect(jsonPath("$.isRecurrent").value(true));
        result.andExpect(jsonPath("$.isActive").value(true));
        result.andExpect(jsonPath("$.tolerance").value(250));
    }

    @Test
    public void insertPlanShouldReturnUnauthorizedWhenNoUserLogged() throws Exception {
        
        PlanDTO dto = new PlanDTO();
        dto.setName("new plan");
        dto.setRecurrenceInterval(1000);
        dto.setRecurrenceType(RecurrenceType.ODOMETER);
        dto.setIsRecurrent(true);
        dto.setIsActive(true);
        dto.setTolerance(250);

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(post("/plans")
                .header("Authorization", "Bearer " + invalidToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());

    }

}
