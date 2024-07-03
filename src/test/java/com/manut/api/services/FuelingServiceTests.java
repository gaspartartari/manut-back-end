package com.manut.api.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.manut.api.dto.FuelingDTO;
import com.manut.api.dto.SyncFuelingDTO;
import com.manut.api.entities.Client;
import com.manut.api.entities.Fueling;
import com.manut.api.projections.ClientProjection;
import com.manut.api.proxys.CTAProxy;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.FuelingRepository;
import com.manut.api.repositories.UserRepository;
import com.manut.api.tests.ClientFactory;
import com.manut.api.tests.ClientProjectionFactory;
import com.manut.api.tests.FuelingFactory;
import com.manut.api.tests.SyncFuelingDTOFactory;
import com.manut.api.utils.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class FuelingServiceTests {

    @InjectMocks
    private FuelingService fuelingService;

    @Mock
    private FuelingRepository fuelingRepository;

    @Mock
    private CTAProxy ctaProxy;

    @Mock
    private MapperService mapperService;

    @Mock
    private CustomUserUtil customUserUtil;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    private String loggedUsername;
    private ClientProjection clientProjection;
    private Fueling fueling;
    private FuelingDTO fuelingDTO;
    private SyncFuelingDTO syncFuelingDTO;
    private Long existingClientId;
    private Client client;
    private String existingClientToken;

    @BeforeEach
    void setup() throws Exception {
        loggedUsername = "gaspar@gmail.com";
        clientProjection = ClientProjectionFactory.createClientProjection(loggedUsername);
        List<ClientProjection> clientList = new ArrayList<>();
        clientList.add(clientProjection);
        fueling = FuelingFactory.createFueling();
        fuelingDTO = FuelingFactory.createFuelingDTO(fueling);
        List<FuelingDTO> list = new ArrayList<>();
        list.add(fuelingDTO);
        syncFuelingDTO = SyncFuelingDTOFactory.createSyncFuelingDTO(list);
        existingClientId = 1L;
        client = ClientFactory.createClient();
        client.setId(existingClientId);
        existingClientToken = "token";

        Mockito.when(customUserUtil.getLoggedUser()).thenReturn(loggedUsername);
        Mockito.when(clientRepository.findAllTokens()).thenReturn(clientList);
        Mockito.when(mapperService.mapDtoToFueling(fuelingDTO, existingClientId)).thenReturn(fueling);
        Mockito.when(mapperService.mapDtoToFueling(null, existingClientId)).thenThrow(NullPointerException.class);
        Mockito.when(clientRepository.getReferenceById(existingClientId)).thenReturn(client);
        Mockito.when(fuelingRepository.save(any(Fueling.class))).thenReturn(fueling);
        Mockito.when(ctaProxy.syncFuelings("token")).thenReturn(syncFuelingDTO);
    }

    @Test
    public void syncFuelingsShouldSaveFuelings() {
        Assertions.assertDoesNotThrow(() -> {
            fuelingService.syncFuelings();
        });
        Mockito.verify(clientRepository).findAllTokens();
        Mockito.verify(ctaProxy).syncFuelings(existingClientToken);
        Mockito.verify(clientRepository).getReferenceById(existingClientId);
        Mockito.verify(fuelingRepository).save(any(Fueling.class));
    }

    @Test
    public void testSyncFuelingsHandlesExceptionInCtaProxy() {
        Mockito.when(ctaProxy.syncFuelings(anyString())).thenThrow(RuntimeException.class);
        Assertions.assertDoesNotThrow(() -> fuelingService.syncFuelings());
        Mockito.verify(ctaProxy).syncFuelings(anyString());
    }

    @Test
    public void testSyncFuelingsHandlesExceptionInMapperService() {
        // Simulando uma exceção lançada pelo mapperService.mapDtoToFueling
        Mockito.when(mapperService.mapDtoToFueling(any(FuelingDTO.class), anyLong())).thenThrow(RuntimeException.class);
        
        Assertions.assertDoesNotThrow(() -> fuelingService.syncFuelings());
        Mockito.verify(ctaProxy).syncFuelings(anyString());
        Mockito.verify(clientRepository).findAllTokens();
    }

    @Test
    public void testSyncFuelingsHandlesExceptionInFuelingRepository() {
        // Simulando uma exceção lançada pelo fuelingRepository.save
        Mockito.when(fuelingRepository.save(any(Fueling.class))).thenThrow(RuntimeException.class);
        
        Assertions.assertDoesNotThrow(() -> fuelingService.syncFuelings());
        Mockito.verify(ctaProxy).syncFuelings(anyString());
        Mockito.verify(clientRepository).findAllTokens();
        Mockito.verify(mapperService).mapDtoToFueling(any(FuelingDTO.class), anyLong());
        Mockito.verify(fuelingRepository).save(any(Fueling.class));
    }

    @Test
    public void testSyncFuelingsHandlesNullPointerException() {
        fueling.setExternalId(null);
        Assertions.assertDoesNotThrow(() -> fuelingService.syncFuelings());
        Mockito.verify(ctaProxy).syncFuelings(anyString());
    }
}
