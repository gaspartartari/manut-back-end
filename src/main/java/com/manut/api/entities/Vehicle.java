package com.manut.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long externalId;
    private String code;
    private String name;
    private String licensePlate;
    private String fleet;
    private boolean bypass; // conferir o que é bypass e qual o tipo de dado correto
    private String category;
    private String model;
    private String rfid;
    private Integer currentOdometer;
    private BigDecimal currentHourmeter;
    private LocalDate lastFuelingDate;
    private LocalTime lastFuelingTime;

    @OneToMany(mappedBy = "vehicle")
    private Set<Fueling> fuellings = new HashSet<>();

    @OneToMany(mappedBy = "vehicle")
    private Set<ServiceOrder> services;

    @ManyToMany(mappedBy = "vehicles")
    private Set<Plan> plans;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Vehicle() {

    }

    public Vehicle(Long externalId, String code, String name, String licensePlate, String fleet, boolean bypass,
            String category, String model, String rfid) {
        this.externalId = externalId;
        this.code = code;
        this.name = name;
        this.licensePlate = licensePlate;
        this.fleet = fleet;
        this.bypass = bypass;
        this.category = category;
        this.model = model;
        this.rfid = rfid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vehicle other = (Vehicle) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
