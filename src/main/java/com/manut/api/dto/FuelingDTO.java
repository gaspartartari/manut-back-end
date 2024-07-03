package com.manut.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.manut.api.entities.Fueling;
import com.manut.api.jsons.BigDecimalFormatter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({ "sequencial", "completo", "extra1", "extra2", "extra3",
        "extra4", "encerrante_eletronica", "custo_unitario", "produto_categoria_id", "ORIGEM", "id_externo", "chave_nfe", "encerrante_fixed", "volume_fixed" })
public class FuelingDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("data_inicio")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @JsonProperty("data_fim")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @JsonProperty("hora_fim")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    @JsonProperty("hora_inicio")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonProperty("volume")
    @JsonDeserialize(using = BigDecimalFormatter.class)
    private BigDecimal volume;

    @JsonProperty("odometro")
    private Integer odometer;

    @JsonProperty("distancia")
    private Integer distance;

    @JsonProperty("media_kilometro_litro")
    @JsonDeserialize(using = BigDecimalFormatter.class)
    private BigDecimal kilometerPerLiterAverage;

    @JsonProperty("horimetro")
    @JsonDeserialize(using = BigDecimalFormatter.class)
    private BigDecimal hourmeter;

    @JsonProperty("horas_trabalhadas")
    @JsonDeserialize(using = BigDecimalFormatter.class)
    private BigDecimal workedHours;

    @JsonProperty("media_litro_hora")
    @JsonDeserialize(using = BigDecimalFormatter.class)
    private BigDecimal literPerHourAverage;

    @JsonProperty("custo")
    @JsonDeserialize(using = BigDecimalFormatter.class)
    private BigDecimal cost;

    @JsonProperty("encerrante")
    @JsonDeserialize(using = BigDecimalFormatter.class)
    private BigDecimal finalMeasure;

    @JsonProperty("veiculo")
    private VehicleDTO vehicle;

    @JsonProperty("motorista")
    private DriverDTO driver;

    @JsonProperty("frentista")
    private AttendantDTO attendant;

    @JsonProperty("posto")
    private StationDTO station;

    @JsonProperty("tanque")
    private TankDTO tank;

    @JsonProperty("bomba")
    private PumpDTO pump;

    @JsonProperty("combustivel")
    private FuelDTO fuel;

    @JsonProperty("empresa")
    private CompanyDTO company;

    public FuelingDTO() {

    }

    public FuelingDTO(Fueling entity) {
        this.id = entity.getExternalId();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.volume = entity.getVolume();
        this.odometer = entity.getOdometer();
        this.distance = entity.getDistance();
        this.kilometerPerLiterAverage = entity.getKilometerPerLiterAverage();
        this.hourmeter = entity.getHourmeter();
        this.workedHours = entity.getWorkedHours();
        this.literPerHourAverage = entity.getLiterPerHourAverage();
        this.cost = entity.getCost();
        this.finalMeasure = entity.getFinalMeasure();
        this.vehicle = new VehicleDTO(entity.getVehicle());
        this.driver = new DriverDTO(entity.getDriver());
        this.attendant = new AttendantDTO(entity.getAttendant());
        this.station = new StationDTO(entity.getStation());
        this.tank = new TankDTO(entity.getTank());
        this.pump = new PumpDTO(entity.getPump());
        this.fuel = new FuelDTO(entity.getFuel());
        this.company = new CompanyDTO(entity.getCompany());
    }
}
