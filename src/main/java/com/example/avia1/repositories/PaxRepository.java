package com.example.avia1.repositories;

import com.example.avia1.models.Pax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaxRepository extends JpaRepository<Pax, Integer> {
    @Query("SELECT p FROM Pax p WHERE CONCAT(p.pax_id, p.last_nm, p.fst_nm, p.mid_nm, p.birth_date, p.phone, p.email, p.passport) LIKE %?1%")
    public List<Pax> search(String keyword);
}