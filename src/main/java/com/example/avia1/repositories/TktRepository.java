package com.example.avia1.repositories;

import com.example.avia1.models.Pax;
import com.example.avia1.models.Tkt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TktRepository extends JpaRepository<Tkt, Integer> {
    @Query("SELECT t FROM Tkt t WHERE t.pax_tkt = :pax")
    List<Tkt> findByPax_tkt(@Param("pax") Pax pax_tkt);
}
