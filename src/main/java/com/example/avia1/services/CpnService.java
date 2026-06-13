package com.example.avia1.services;

import com.example.avia1.models.Cpn;
//import com.example.avia1.repositories.CpnRepository;
import com.example.avia1.models.Pax;
import com.example.avia1.models.Tkt;
import com.example.avia1.repositories.TktRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CpnService {
    private final TktRepository tktRepository;

    public List<Cpn> getUpcomingCouponsSorted(Pax pax) {
        List<Tkt> tickets = tktRepository.findByPax_tkt(pax);
        LocalDate today = LocalDate.now();

        return tickets.stream()
                .flatMap(ticket -> ticket.getCpns().stream())
                .filter(cpn -> cpn.getFlt_cpn() != null
                        && cpn.getFlt_cpn().getDeptr_dttm_lcl() != null)
                // .filter(cpn -> !cpn.getFlt_cpn().getFltDate().isBefore(today)) // раскомментировать, если нужны только будущие перелеты
                .sorted(Comparator.comparing(cpn -> cpn.getFlt_cpn().getDeptr_dttm_lcl()))
                .collect(Collectors.toList());
    }
}