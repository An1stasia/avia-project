package com.example.avia1.repositories;

import com.example.avia1.models.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    @Query("SELECT c FROM Cost c WHERE CONCAT(c.cost_id, c.cls, c.cost) LIKE %?1%")
    public List<Cost> search(String keyword);
}