package com.example.avia1.repositories;

import com.example.avia1.models.Aip;
import com.example.avia1.models.Cpn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CpnRepository extends JpaRepository<Cpn, Integer> { }
