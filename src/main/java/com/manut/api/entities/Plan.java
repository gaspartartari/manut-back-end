package com.manut.api.entities;

import java.util.HashSet;
import java.util.Set;

import com.manut.api.enums.RecurrenceType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "tb_plan")
@Inheritance(strategy = InheritanceType.JOINED)
public class Plan extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer recurrenceInterval;
    private RecurrenceType recurrenceType;
    private Integer tolerance;
    private Boolean isActive;
    private Boolean isRecurrent;

    @ManyToMany
    @JoinTable(name = "tb_plan_vehicle", joinColumns = @JoinColumn(name = "plan_id"), 
        inverseJoinColumns = @JoinColumn(name = "vehicle_id"))      
    private Set<Vehicle> vehicles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "tb_plan_task", joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "task_id"))
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "plan")
    private Set<ServiceOrder> services;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Plan() {

    }

    public void addVehicle(Vehicle v){
        vehicles.add(v);
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
        Plan other = (Plan) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
