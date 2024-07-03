package com.manut.api.entities;

import java.util.HashSet;
import java.util.Set;



import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_attendant")
public class Attendant  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long externalId;
    private String code;
    private String name;
    private String cpf;
    private boolean active;
    private String rfid;

    @OneToMany(mappedBy = "attendant")
    private Set<Fueling> fuelings = new HashSet<>();


    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    public Attendant() {

    }

    public Attendant(Long id, String code, String name, String cpf, boolean active, String rfid) {
      
        this.id = id;
        this.code = code;
        this.name = name;
        this.cpf = cpf;
        this.active = active;
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
        Attendant other = (Attendant) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
