package com.example.avia1.models;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.List;

@Setter
@Entity
public class Tkt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tkt_id;

    public int getTkt_id() {
        return tkt_id;
    }

    public Tkt(){}

    @OneToMany(mappedBy = "tkt_cpn")
    private List<Cpn> cpns;

    public List<Cpn> getCpns() {
        return cpns;
    }

    @ManyToOne
    @JoinColumn(name = "pax_id")
    private Pax pax_tkt ;

    public Pax getPax_tkt() {
        return pax_tkt;
    }

    public void setPax_tkt(Pax pax_tkt) {
        this.pax_tkt = pax_tkt;
    }
}



