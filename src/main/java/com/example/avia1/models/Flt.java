package com.example.avia1.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Entity
public class Flt {
    @Id
    private int flt_id;
    @Getter
    private String flt_nbr;
    @Getter
    private Date flt_date;
    @Getter
    private Date deptr_dttm_lcl;
    @Getter
    private Date deptr_dttm_utc;
    @Getter
    private Date arvl_dttm_lcl;
    @Getter
    private Date arvl_dttm_utc;
//    @Getter
//    private int deptr_aip_id;
//    @Getter
//    private int arvl_aip_id;
    @Getter
    private double tpm_km;
    @Getter
    private String acft_vrsn;
    @Getter
    private int cap_total;
    @Getter
    private int cap_business;
    @Getter
    private int cap_economy;
    @Getter
    private int cap_comfort;

    @ManyToOne
    @JoinColumn(name = "deptr_aip_id")
    private Aip deptrAirports;

    @ManyToOne
    @JoinColumn(name = "arvl_aip_id")
    private Aip arvlAirports;

    @OneToMany(mappedBy = "flight")
    private List<Cost> flightList;

    // Геттер (обязательно)
    public List<Cost> getFlightList() {
        return flightList;
    }

    // Сеттер (опционально, но полезно)
    public void setFlightList(List<Cost> flightList) {
        this.flightList = flightList;
    }

    public Flt(){}

    public Flt(Aip deptrAirports, Aip arvlAirports) {
        this.deptrAirports = deptrAirports;
        this.arvlAirports = arvlAirports;
    }

    public Aip getDeptrAirports() {
        return deptrAirports;
    }

    public void setDeptrAirports(Aip deptrAirports) {
        this.deptrAirports = deptrAirports;
    }

    public Aip getArvlAirports() {
        return arvlAirports;
    }

    public void setArvlAirports(Aip arvlAirports) {
        this.arvlAirports = arvlAirports;
    }

    public int getFlt_id() {
        return flt_id;
    }

    @OneToMany(mappedBy = "flt_cpn")
    private List<Cpn> cpnFltList;

}
