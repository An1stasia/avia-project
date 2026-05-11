package com.example.avia1.models;

import jakarta.persistence.*;
import lombok.Setter;

@Setter
@Entity
public class Cpn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cpn_id;


    public int getCpn_id() {
        return cpn_id;
    }

    public Cpn(){}

    @ManyToOne
    @JoinColumn(name = "cost_id")
    private Cost cost_cpn ;

    public Cost getCost_cpn() {
        return cost_cpn;
    }

    public void setCost_cpn(Cost cost_cpn) {
        this.cost_cpn = cost_cpn;
    }

//    @ManyToOne
//    @JoinColumn(name = "pax_id")
//    private Pax pax_cpn ;
//
//    public Pax getPax_cpn() {
//        return pax_cpn;
//    }
//
//    public void setPax_cpn(Pax pax_cpn) {
//        this.pax_cpn = pax_cpn;
//    }

    @ManyToOne
    @JoinColumn(name = "flt_id")
    private Flt flt_cpn ;

    public Flt getFlt_cpn() {
        return flt_cpn;
    }

    public void setFlt_cpn(Flt flt_cpn) {
        this.flt_cpn = flt_cpn;
    }

    @ManyToOne
    @JoinColumn(name = "tkt_id")   // столбец в таблице Cpn
    private Tkt tkt_cpn;

}
