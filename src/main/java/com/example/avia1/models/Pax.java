package com.example.avia1.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@Setter
@Entity
public class Pax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pax_id;
    @Getter
    private String last_nm;
    @Getter
    private String fst_nm;
    @Getter
    private String mid_nm;
    @Getter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth_date;
    @Getter
    private String passport;
    @Getter
    private String phone;
    @Getter
    private String email;

    public int getPax_id() {
        return pax_id;
    }

    @OneToOne(mappedBy = "passenger")
    private User user_pax;

    @OneToMany(mappedBy = "pax_tkt")
    private List<Tkt> paxList;

}
