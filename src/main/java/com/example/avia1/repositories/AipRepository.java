package com.example.avia1.repositories;

import com.example.avia1.models.Aip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AipRepository extends JpaRepository<Aip, Integer> {
    @Query("SELECT a FROM Aip a WHERE CONCAT(a.aip_id, a.aipName, a.city, a.ctry, a.city_rus) LIKE %?1%")
    public List<Aip> search(String keyword);

    @Query("SELECT a.city, a.ctry, a.city_rus FROM Aip a WHERE CONCAT(a.city, a.ctry, a.city_rus) LIKE %?1% AND " +
            "a.city != '' AND a.ctry != '' GROUP BY a.city, a.ctry, a.city_rus " +
            "ORDER BY CASE WHEN a.city = 'Moscow' THEN 1 WHEN a.city = 'Saint Petersburg' and a.ctry = 'Russian Federation' THEN 2 " +
            "WHEN a.ctry = 'Russian Federation' THEN 3 ELSE 4 END, a.city ASC")
    public List<Object[]> findCityWithSorting(String keyword);
}
