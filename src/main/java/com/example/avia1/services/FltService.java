package com.example.avia1.services;

import com.example.avia1.models.Flt;
import com.example.avia1.models.Pax;
import com.example.avia1.repositories.FltRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class FltService {
    @Autowired
    private FltRepository fltRepository;
//    public List<Flt> listAll(String keyword) {
//        if (keyword != null) {
//            return fltRepository.search(keyword);
//        }
//        return fltRepository.findAll();
//    }

    public List<Flt> searchFlights(String departureCity, String arrivalCity, LocalDate departureDate) {
        Timestamp start = Timestamp.valueOf(departureDate.atStartOfDay());
        Timestamp end = Timestamp.valueOf(departureDate.atTime(LocalTime.MAX));
        return fltRepository.findFlightsByCityAndDate(departureCity, arrivalCity, start, end);
    }

    public Optional<Flt> get(int id) {
        return fltRepository.findById(id);
    }
}
