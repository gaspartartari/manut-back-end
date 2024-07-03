package com.manut.api.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.manut.api.enums.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "tb_service_order")
public class ServiceOrder extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate conclusionDate;
    private Integer conclusionCounter;
    private OrderStatus status = OrderStatus.PENDING;
    private Double serviceCost;
    private Double materialCost;
    private Double totalCost;
   

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "service")
    private Set<ServiceTask> serviceTasks = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    public ServiceOrder() {

    }

    public ServiceOrder(Long id) {
        this.id = id;
        this.status = OrderStatus.PENDING;
    }

    public Double getTotalCost(){
        if(serviceCost == null || materialCost == null)
            return null;
        return serviceCost + materialCost;
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
        ServiceOrder other = (ServiceOrder) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}