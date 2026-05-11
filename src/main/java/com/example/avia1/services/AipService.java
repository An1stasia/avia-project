package com.example.avia1.services;

import com.example.avia1.models.Aip;
import com.example.avia1.models.City;
import com.example.avia1.repositories.AipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AipService {
    @Autowired
    private AipRepository aipRepository;

    public List<Aip> listAll(String keyword) {
        if (keyword != null) {
            return aipRepository.search(keyword);
        }
        return aipRepository.findAll();
    }

    public List<City> getCities(String keyword) {
        String kw = (keyword == null) ? "" : keyword.trim();
        List<Object[]> rows = aipRepository.findCityWithSorting(kw);
        return rows.stream()
                .map(row -> new City((String) row[0], (String) row[1], (String) row[2]))
                .collect(Collectors.toList());
    }
}
