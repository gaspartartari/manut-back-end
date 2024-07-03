package com.manut.api.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manut.api.dto.FuelingDTO;
import com.manut.api.entities.Attendant;
import com.manut.api.entities.Company;
import com.manut.api.entities.Driver;
import com.manut.api.entities.Fuel;
import com.manut.api.entities.Fueling;
import com.manut.api.entities.Pump;
import com.manut.api.entities.Station;
import com.manut.api.entities.Tank;
import com.manut.api.entities.Vehicle;
import com.manut.api.repositories.AttendantRepository;
import com.manut.api.repositories.ClientRepository;
import com.manut.api.repositories.CompanyRepository;
import com.manut.api.repositories.DriverRepository;
import com.manut.api.repositories.FuelRepository;
import com.manut.api.repositories.PumpRepository;
import com.manut.api.repositories.StationRepository;
import com.manut.api.repositories.TankRepository;
import com.manut.api.repositories.VehicleRepository;

@Service
public class MapperService {

    private static final Logger logger = LoggerFactory.getLogger(MapperService.class);

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private TankRepository tankRepository;
    @Autowired
    private PumpRepository pumpRepository;
    @Autowired
    private FuelRepository fuelRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private AttendantRepository attendantRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public Fueling mapDtoToFueling(FuelingDTO dto, Long existingClientId) {
        try {
            Fueling fueling = new Fueling();

            fueling.setExternalId(dto.getId());
            fueling.setHourmeter(dto.getHourmeter());
            fueling.setCost(dto.getCost());
            fueling.setDistance(dto.getDistance());
            fueling.setFinalMeasure(dto.getFinalMeasure());
            fueling.setKilometerPerLiterAverage(dto.getKilometerPerLiterAverage());
            fueling.setLiterPerHourAverage(dto.getLiterPerHourAverage());
            fueling.setOdometer(dto.getOdometer());
            fueling.setStartDate(dto.getStartDate());
            fueling.setStartTime(dto.getStartTime());
            fueling.setEndDate(dto.getEndDate());
            fueling.setEndTime(dto.getEndTime());
            // converting localDate to UTC
            LocalDateTime startDateTime = LocalDateTime.of(dto.getStartDate(), dto.getStartTime());
            ZoneOffset offset = ZoneOffset.ofHours(-3);
            ZonedDateTime gmtStartDateTime = ZonedDateTime.of(startDateTime, offset);
            Instant instantOfStartDateTime = gmtStartDateTime.toInstant();
            LocalDateTime endDateTime = LocalDateTime.of(dto.getEndDate(), dto.getEndTime());
            ZonedDateTime gmtEndDateTime = ZonedDateTime.of(endDateTime, offset);
            Instant instantOfEndDateTime = gmtEndDateTime.toInstant();
            fueling.setStartDateTime(instantOfStartDateTime);
            fueling.setEndDateTime(instantOfEndDateTime);
            fueling.setVolume(dto.getVolume());
            fueling.setWorkedHours(dto.getWorkedHours());

            // Para 'Station'
            Station station = stationRepository.searchById(dto.getStation().getExternalId(),
                    existingClientId).orElseGet(() -> {
                        Station newStation = new Station();
                        newStation.setExternalId(dto.getStation().getExternalId());
                        newStation.setCode(dto.getStation().getCode());
                        newStation.setName(dto.getStation().getName());
                        newStation.setCommercialStation(dto.getStation().isCommercialStation());
                        newStation.setCnpj(dto.getStation().getCnpj());
                        newStation.setClient(clientRepository.findById(existingClientId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Client not found for id : " + existingClientId)));
                        return newStation;
                    });

            stationRepository.save(station);
            fueling.setStation(station);

            // Para 'Tank'
            Tank tank = tankRepository.searchById(dto.getTank().getExternalId(),
                    existingClientId).orElseGet(() -> {
                        logger.info("Tank not found for id: {}", dto.getTank().getExternalId());
                        logger.info("Creating new tank...");
                        Tank newTank = new Tank();
                        newTank.setExternalId(dto.getTank().getExternalId());
                        newTank.setCode(dto.getTank().getCode());
                        newTank.setName(dto.getTank().getName());
                        newTank.setClient(clientRepository.findById(existingClientId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Client not found for id : " + existingClientId)));
                        return newTank;
                    });

            tankRepository.save(tank);
            fueling.setTank(tank);

            // Para 'Pump'
            Pump pump = pumpRepository.searchById(dto.getPump().getExternalId(),
                    existingClientId).orElseGet(() -> {
                        logger.info("Pump not found for id: {}", dto.getPump().getExternalId());
                        logger.info("Creating new pump...");
                        Pump newPump = new Pump();
                        newPump.setExternalId(dto.getPump().getExternalId());
                        newPump.setCode(dto.getPump().getCode());
                        newPump.setName(dto.getPump().getName());
                        newPump.setClient(clientRepository.findById(existingClientId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Client not found for id : " + existingClientId)));
                        return newPump;
                    });

            pumpRepository.save(pump);
            fueling.setPump(pump);

            // Para 'Fuel'
            Fuel fuel = fuelRepository.searchById(dto.getFuel().getExternalId(),
                    existingClientId).orElseGet(() -> {
                        logger.info("Fuel not found for id: {}", dto.getFuel().getExternalId());
                        logger.info("Creating new fuel...");
                        Fuel newFuel = new Fuel();
                        newFuel.setExternalId(dto.getFuel().getExternalId());
                        newFuel.setCode(dto.getFuel().getCode());
                        newFuel.setDescription(dto.getFuel().getDescription());
                        newFuel.setClient(clientRepository.findById(existingClientId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Client not found for id : " + existingClientId)));
                        return newFuel;

                    });

            fuelRepository.save(fuel);
            fueling.setFuel(fuel);

            // Para 'Attendant'
            Attendant attendant = attendantRepository
                    .searchById(dto.getAttendant().getExternalId(), existingClientId).orElseGet(() -> {
                        logger.info("Attendant not found for id: {}", dto.getAttendant().getExternalId());
                        logger.info("Creating new attendant...");
                        Attendant newAttendant = new Attendant();
                        newAttendant.setExternalId(dto.getAttendant().getExternalId());
                        newAttendant.setCode(dto.getAttendant().getCode());
                        newAttendant.setName(dto.getAttendant().getName());
                        newAttendant.setCpf(dto.getAttendant().getCpf());
                        newAttendant.setActive(dto.getAttendant().isActive());
                        newAttendant.setRfid(dto.getAttendant().getRfid());
                        newAttendant.setClient(clientRepository.findById(existingClientId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Client not found for id : " + existingClientId)));
                        return newAttendant;

                    });

            attendantRepository.save(attendant);
            fueling.setAttendant(attendant);

            // Para 'Driver'
            Driver driver = driverRepository.searchById(dto.getDriver().getExternalId(),
                    existingClientId).orElseGet(() -> {
                        logger.info("Driver not found for id: {}", dto.getDriver().getExternalId());
                        logger.info("Creating new driver...");
                        Driver newDriver = new Driver();
                        newDriver.setExternalId(dto.getDriver().getExternalId());
                        newDriver.setCode(dto.getDriver().getCode());
                        newDriver.setName(dto.getDriver().getName());
                        newDriver.setCpf(dto.getDriver().getCpf());
                        newDriver.setCnh(dto.getDriver().getCnh());
                        newDriver.setClient(clientRepository.findById(existingClientId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Client not found for id : " + existingClientId)));
                        return newDriver;

                    });

            driverRepository.save(driver);
            fueling.setDriver(driver);

            // Para Vehicle 2
            Vehicle vehicle = vehicleRepository.searchById(dto.getVehicle().getExternalId(),
                    existingClientId).orElseGet(() -> {
                        logger.info("Vehicle not fond for id: {}", dto.getVehicle().getExternalId());
                        logger.info("Creating new vehicle..");
                        Vehicle newVehicle = new Vehicle();
                        newVehicle.setExternalId(dto.getVehicle().getExternalId());
                        newVehicle.setName(dto.getVehicle().getName());
                        newVehicle.setBypass(dto.getVehicle().isBypass());
                        newVehicle.setCategory(dto.getVehicle().getCategory());
                        newVehicle.setCode(dto.getVehicle().getCode());
                        newVehicle.setFleet(dto.getVehicle().getFleet());
                        newVehicle.setLicensePlate(dto.getVehicle().getLicensePlate());
                        newVehicle.setModel(dto.getVehicle().getModel());
                        newVehicle.setRfid(dto.getVehicle().getRfid());
                        newVehicle.setCurrentHourmeter(dto.getHourmeter());
                        newVehicle.setCurrentOdometer(dto.getOdometer());
                        newVehicle.setLastFuelingDate(dto.getEndDate());
                        newVehicle.setLastFuelingTime(dto.getEndTime());
                        newVehicle.setClient(clientRepository.findById(existingClientId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Client not found for id : " + existingClientId)));
                        return newVehicle;

                    });
            vehicle = vehicleRepository.save(vehicle);
            fueling.setVehicle(vehicle);

            if (dto.getEndDate().isAfter(vehicle.getLastFuelingDate())) {
                vehicle.setCurrentHourmeter(dto.getHourmeter());
                vehicle.setCurrentOdometer(dto.getOdometer());
                vehicle.setLastFuelingDate(dto.getEndDate());
                vehicle.setLastFuelingTime(dto.getEndTime());
            }
            if (dto.getEndDate().equals(vehicle.getLastFuelingDate())) {
                if (dto.getEndTime().isAfter(vehicle.getLastFuelingTime())) {
                    vehicle.setCurrentHourmeter(dto.getHourmeter());
                    vehicle.setCurrentOdometer(dto.getOdometer());
                    vehicle.setLastFuelingDate(dto.getEndDate());
                    vehicle.setLastFuelingTime(dto.getEndTime());
                }
            }

            vehicle = vehicleRepository.save(vehicle);
            fueling.setVehicle(vehicle);

            // Para "Company"
            Company company = companyRepository.searchById(dto.getCompany().getExternalId(),
                    existingClientId).orElseGet(() -> {
                        logger.info("Company not fond for id: {}", dto.getCompany().getExternalId());
                        logger.info("Creating new company..");
                        Company newCompany = new Company();
                        newCompany.setExternalId(dto.getCompany().getExternalId());
                        newCompany.setCode(dto.getCompany().getCode());
                        newCompany.setName(dto.getCompany().getName());
                        newCompany.setBranch(dto.getCompany().getBranch());
                        newCompany.setCnpj(dto.getCompany().getCnpj());
                        newCompany.setClient(clientRepository.findById(existingClientId)
                                .orElseThrow(
                                        () -> new IllegalArgumentException(
                                                "Client not found for id : " + existingClientId)));
                        return newCompany;

                    });

            company = companyRepository.save(company);
            fueling.setCompany(company);

            return fueling;

        } catch (

        Exception e) {
            logger.error("Error mapping FuelingDTO to Fueling: {}", e.getMessage());
            return null;
        }
    }

}