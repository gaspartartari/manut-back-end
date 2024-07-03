package com.manut.api.tests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.manut.api.dto.FuelingDTO;
import com.manut.api.entities.Attendant;
import com.manut.api.entities.Client;
import com.manut.api.entities.Company;
import com.manut.api.entities.Driver;
import com.manut.api.entities.Fuel;
import com.manut.api.entities.Fueling;
import com.manut.api.entities.Pump;
import com.manut.api.entities.Station;
import com.manut.api.entities.Tank;
import com.manut.api.entities.Vehicle;

public class FuelingFactory {

    public static Fueling createFueling() {

        Fueling fueling = new Fueling();
        fueling.setId(1L);
        fueling.setExternalId(4345L);
        fueling.setAttendant(new Attendant(1L, "435", "alberto", "0245256621", false, null));
        fueling.setClient(new Client(1L,"cliente teste", "token"));
        fueling.setCompany(new Company(1L, "4545", "Company1", null, null));
        fueling.setCost(new BigDecimal("40"));
        fueling.setDistance(30);
        fueling.setDriver(new Driver(1L, "Motorista 1", "435", null, null));
        fueling.setEndDate(LocalDate.now());
        fueling.setEndTime(LocalTime.now());
        fueling.setFinalMeasure(new BigDecimal("50"));
        fueling.setFuel(new Fuel(1L, "Diesel", "545"));
        fueling.setHourmeter(new BigDecimal("40"));
        fueling.setKilometerPerLiterAverage(new BigDecimal("30"));
        fueling.setLiterPerHourAverage(new BigDecimal("50"));
        fueling.setOdometer(null);
        fueling.setPump(new Pump(1L, "4543", "Bomba 1"));
        fueling.setStartDate(LocalDate.now());
        fueling.setStartTime(LocalTime.now());
        fueling.setStation(new Station(1L, "Station 1", "545", false, null));
        fueling.setVehicle(new Vehicle(1L, null, "Veiculo1", "ABD-3435", "FROTA1", false, null, null, null));
        fueling.setVolume(new BigDecimal("10"));
        fueling.setWorkedHours(new BigDecimal("1000"));
        fueling.setTank(new Tank(1L, "545", "Tank 1"));
        return fueling;
    }

    public static FuelingDTO createFuelingDTO(Fueling fueling){
        FuelingDTO fuelingDTO = new FuelingDTO(fueling);
        
        return fuelingDTO;
    }
}
