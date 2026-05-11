package com.example.avia1.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Entity
public class Aip {
    private int aip_id;
    @Getter
    private String aipName;
    @Getter
    private String city;
    @Getter
    private String ctry;
    @Getter
    private String city_rus;

    @Id
    public int getAip_id() {
        return aip_id;
    }


    @OneToMany(mappedBy = "deptrAirports")
    private List<Flt> fltListDeprt;


    @OneToMany(mappedBy = "arvlAirports")
    private List<Flt> fltListArvl;

//    public List<Flt> getDeptr_flts() {
//        return deptr_flts;
//    }
//
//    public void setDeptr_flts(List<Flt> deptr_flts) {
//        this.deptr_flts = deptr_flts;
//    }
//
//    public List<Flt> getArvl_flts() {
//        return arvl_flts;
//    }
//
//    public void setArvl_flts(List<Flt> arvl_flts) {
//        this.arvl_flts = arvl_flts;
//    }

}
