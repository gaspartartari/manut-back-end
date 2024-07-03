package com.manut.api.entities;

import java.util.HashSet;
import java.util.Set;



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
@Table(name = "tb_pump")
public class Pump {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// "id"
    private Long externalId;
    private String code; // check for correct data type
    private String name;

    @OneToMany(mappedBy = "pump")
    private Set<Fueling> fuelings = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Pump() {

    }

    public Pump(Long id, String code, String name) {

        this.id = id;
        this.code = code;
        this.name = name;
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
        Pump other = (Pump) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
