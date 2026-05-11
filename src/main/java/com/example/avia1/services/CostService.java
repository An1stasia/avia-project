package com.example.avia1.services;

import com.example.avia1.models.Cost;
import com.example.avia1.models.Flt;
import com.example.avia1.repositories.CostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CostService {

    @Autowired
    private CostRepository costRepository;

    public List<Cost> listAll(String keyword) {
        if (keyword != null) {
            return costRepository.search(keyword);
        }
        return costRepository.findAll();
    }

    public Optional<Cost> get(int id) {
        return costRepository.findById(id);
    }
}
