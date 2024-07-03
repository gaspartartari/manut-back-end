package com.manut.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manut.api.dto.GarageDTO;
import com.manut.api.entities.Client;
import com.manut.api.entities.Garage;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.GarageRepository;
import com.manut.api.services.exceptions.DatabaseException;
import com.manut.api.services.exceptions.ResourceNotFoundException;

import jakarta.validation.Valid;

@Service
public class GarageService {

    private static final Logger logger = LoggerFactory.getLogger(PlanService.class);

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public Page<GarageDTO> findAll(Pageable pageable) {
        Long clientId = authService.getClientId();
        logger.debug("Client id: {}", clientId);
        Page<Garage> result = garageRepository.findAllGarages(clientId, pageable);
        return result.map(x -> new GarageDTO(x));
    }

    @Transactional
    public GarageDTO insertGarage(@Valid GarageDTO garageDTO) {
        Garage entity = new Garage();
        entity.setName(garageDTO.getName());
        Client client = clientRepository.findById(authService.getClientId()).get();
        entity.setClient(client);
        entity = garageRepository.save(entity);
        return new GarageDTO(entity);
    }

    @Transactional(readOnly = true)
    public GarageDTO findById(Long id) {
        Garage entity = garageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Garage " + id + " not found"));
        authService.validateClient(entity.getClient().getId());
        return new GarageDTO(entity);
    }

    @Transactional
    public GarageDTO update(Long id, @Valid GarageDTO garageDTO) {
        Garage entity = garageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Garage " + id + " not found"));
        authService.validateClient(entity.getClient().getId());
        entity.setName(garageDTO.getName());
        garageRepository.save(entity);
        return new GarageDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        logger.debug("Garage workshop with id: {}", id);
        if (!garageRepository.existsById(id))
            throw new ResourceNotFoundException("Resource not found: " + id); 
        try {
            Garage entity = garageRepository.findById(id).get();
            authService.validateClient(entity.getClient().getId());
            if (entity.getServices() != null)
                if(entity.getServices().size() > 0) 
                    throw new DatabaseException("Garage has associated service orders");
            garageRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential constraint violation");
        }
    }
    
}
