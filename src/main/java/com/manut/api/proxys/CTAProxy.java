package com.manut.api.proxys;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manut.api.dto.SyncFuelingDTO;

@Component
public class CTAProxy {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${url.cta.sync.fueling}")
    private String urlCtaSyncFueling;

    @Value("${url.cta.inform.sync}")
    private String urlCtaInformSync;

    private static final Logger logger = LoggerFactory.getLogger(CTAProxy.class);

    @Transactional
    public SyncFuelingDTO syncFuelings(String token) {
        logger.info("Invoking syncFueling with token: {}", token);
        /* chamar a cta */
        SyncFuelingDTO dto = new SyncFuelingDTO();
        String url = String.format(urlCtaSyncFueling + "?token=%s&formato=json&data_inicio=01/01/2024", token);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                .timeout(Duration.ofSeconds(20)).GET().build();
        try {
            HttpResponse<String> reponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            dto = objectMapper.readValue(reponse.body(), SyncFuelingDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    public void sendSyncReport(String syncInformList, String token) {
        if (syncInformList == null) {
            logger.info("syncInformList is null");
            throw new IllegalArgumentException("sync inform list cannot be null");
        }
        logger.info("Invoking syncInform with token: {}", token);
        try {
            String encodedJson = URLEncoder.encode(syncInformList, StandardCharsets.UTF_8.toString());
            String url = String.format(urlCtaInformSync + "?token=%s&json=%s&formato=json", token, encodedJson);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10)).POST(HttpRequest.BodyPublishers.noBody()).build();

            //HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //if (response.statusCode() == 200) {
                // Processar a resposta conforme necessário
            //     logger.info("Sincronismo informado com sucesso: {}", response.body());
            // } else {
            //     logger.error("Erro ao informar sincronismo: {}", response.body());
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
