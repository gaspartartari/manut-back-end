package com.manut.api.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientName;
    private String token;

    @OneToMany(mappedBy = "client")
    private Set<Plan> plans = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Fueling> fuelings = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Attendant> attendants = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Driver> drivers = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Company> companies = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Vehicle> vehicles = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Tank> tanks = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Station> stations = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Pump> pumps = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<ServiceOrder> serviceOrders = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<ServiceTask> serviceTasks = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Fuel> fuels = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Garage> garages = new HashSet<>();

    public Client() {

    }

    public Client(Long id, String clientName, String token) {
        this.clientName = clientName;
        this.token = token;
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
        Client other = (Client) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
