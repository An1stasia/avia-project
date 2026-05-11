package com.example.avia1.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Entity
public class Cost {
    @Id
    private int cost_id;
//    @Getter
//    private int flt_id;
    @Getter
    private String cls;
    @Getter
    private String cost;


    @ManyToOne
    @JoinColumn(name = "flt_id")
    private Flt flight;

    public Cost() {}

    public Cost(Flt flight) {
        this.flight = flight;
    }

    public Flt getflight(){
        return flight;
    }

    public void setflight(Flt flight){
        this.flight = flight;
    }

    public int getCost_id() {
        return cost_id;
    }

    @OneToMany(mappedBy = "cost_cpn")
    private List<Cpn> cpnList;
}
