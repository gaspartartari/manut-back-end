package com.manut.api.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.manut.api.dto.FuelingDTO;
import com.manut.api.dto.SyncFuelingDTO;
import com.manut.api.dto.SyncInformDTO;
import com.manut.api.entities.Client;
import com.manut.api.entities.Fueling;
import com.manut.api.entities.FuelingSyncedEvent;
import com.manut.api.enums.SyncStatus;
import com.manut.api.projections.ClientProjection;
import com.manut.api.proxys.CTAProxy;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.FuelingRepository;
import com.manut.api.repositories.UserRepository;
import com.manut.api.utils.CustomUserUtil;

@Service
public class FuelingService {

    private static final Logger logger = LoggerFactory.getLogger(CTAProxy.class);

    @Autowired
    private FuelingRepository fuelingRepository;

    @Autowired
    private CTAProxy ctaProxy;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomUserUtil customUserUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperService mapperService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public SyncFuelingDTO saveFueling(SyncFuelingDTO syncFuelingDTO) {
        List<FuelingDTO> list = new ArrayList<>();
        Fueling entity = new Fueling();
        String username = customUserUtil.getLoggedUser();
        ClientProjection client = userRepository.findClientIdByUserEmail(username);
        if (client == null) {
            throw new UsernameNotFoundException("Client not found for user : " + username);
        }

        for (FuelingDTO f : syncFuelingDTO.getFuellings()) {
            entity = mapperService.mapDtoToFueling(f, client.getId());
            entity.setClient(clientRepository.getReferenceById(client.getId()));
            entity = fuelingRepository.save(entity);
            list.add(new FuelingDTO(entity));
        }

        SyncFuelingDTO result = new SyncFuelingDTO(null, list);
        return result;

    }

    @Transactional
    @Scheduled(initialDelay = 2000, fixedDelay = Long.MAX_VALUE)
    public void syncFuelings() {
        List<ClientProjection> clients = clientRepository.findAllTokens();
        for (ClientProjection client : clients) {
            List<SyncInformDTO> informList = new ArrayList<>();
            List<List<Long>> updatedVehicleIds = new ArrayList<>();
            try {
                SyncFuelingDTO dto = ctaProxy.syncFuelings(client.getToken());
                if (dto.getFuellings() == null) { continue; }
                for (FuelingDTO fuelingDto : dto.getFuellings()) {
                    Fueling fuelingEntity = mapperService.mapDtoToFueling(fuelingDto, client.getId());
                    Client clientEntity = clientRepository.getReferenceById(client.getId());
                    try {
                        fuelingEntity.setClient(clientEntity);
                        fuelingRepository.save(fuelingEntity);
                        updatedVehicleIds.add(Arrays.asList(fuelingDto.getVehicle().getExternalId(), client.getId()));
                        logger.info("external id" + fuelingDto.getVehicle().getExternalId());
                        informList.add(new SyncInformDTO(fuelingEntity.getExternalId(), SyncStatus.SUCESSO, null));
                    } catch (Exception e) {
                        informList.add(new SyncInformDTO(fuelingEntity.getExternalId(), SyncStatus.ERRO, null));
                    }
                }
                String syncInformList = createSyncReportJson(informList);
                // ctaProxy.sendSyncReport(syncInformList, client.getToken());
                eventPublisher.publishEvent(new FuelingSyncedEvent(this, updatedVehicleIds));
            } catch (Exception e) {
                logger.error("Erro ao sincronizar abastecimentos", e);
            }
        }
    }

    

    private String createSyncReportJson(List<SyncInformDTO> syncInformDtos) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ArrayNode fuelingNodes = rootNode.putArray("fuelings");

        for (SyncInformDTO syncInformDto : syncInformDtos) {
            ObjectNode fuelingNode = fuelingNodes.addObject();
            fuelingNode.put("id", syncInformDto.getId());
            fuelingNode.put("status", syncInformDto.getStatus().toString());
            if (syncInformDto.getStatus() == SyncStatus.ERRO) {
                fuelingNode.put("error", syncInformDto.getMotivoErro());
            }
        }

        try {
            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
