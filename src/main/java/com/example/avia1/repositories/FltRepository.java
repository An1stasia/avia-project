package com.example.avia1.repositories;

import com.example.avia1.models.Flt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface FltRepository extends JpaRepository<Flt, Integer> {
    @Query("SELECT f FROM Flt f " +
            "WHERE CONCAT(f.flt_id, f.flt_nbr, f.flt_date, f.deptr_dttm_lcl, f.deptr_dttm_utc, f.arvl_dttm_lcl," +
            "f.arvl_dttm_utc, f.tpm_km, f.acft_vrsn, f.cap_total, f.cap_business, f.cap_economy, f.cap_comfort, " +
            "f.deptrAirports.aipName, f.arvlAirports.aipName, f.deptrAirports.city, f.arvlAirports.city) LIKE %?1%")
    public List<Flt> search(String keyword);

    @Query("SELECT f FROM Flt f " +
            "LEFT JOIN FETCH f.flightList " +
            "WHERE CONCAT(f.flt_id, f.flt_nbr, f.flt_date, f.deptr_dttm_lcl, f.deptr_dttm_utc, f.arvl_dttm_lcl," +
            "f.arvl_dttm_utc, f.tpm_km, f.acft_vrsn, f.cap_total, f.cap_business, f.cap_economy, f.cap_comfort, " +
            "f.deptrAirports.aipName, f.arvlAirports.aipName, f.deptrAirports.city, f.arvlAirports.city) LIKE %?1% " +
            "AND f.deptrAirports.city = :departureCity " +
            "AND f.arvlAirports.city = :arrivalCity " +
            "AND f.deptr_dttm_lcl BETWEEN :startOfDay AND :endOfDay " +
            "ORDER BY f.deptr_dttm_lcl ASC")
    List<Flt> findFlightsByCityAndDate(@Param("departureCity") String departureCity,
                                       @Param("arrivalCity") String arrivalCity,
                                       @Param("startOfDay") Timestamp startOfDay,
                                       @Param("endOfDay") Timestamp endOfDay);
}



//Aip d on f.deptr_aip_id = d.aip_id
//Aip a on f.arvl_aip_id = a.aip_id
//f.deptr_aip_id, f.arvl_aip_id