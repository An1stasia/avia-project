package com.example.avia1.services;

import com.example.avia1.models.Pax;
import com.example.avia1.repositories.PaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaxService {
    @Autowired
    private PaxRepository paxRepository;
    public List<Pax> listAll(String keyword) {
        if (keyword != null) {
            return paxRepository.search(keyword);
        }
    return paxRepository.findAll();
    }

    public void save(Pax pax) {
        paxRepository.save(pax);
    }

    public Pax get(int id) {
        return paxRepository.findById(id).get();
    }
}
