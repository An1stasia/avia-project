package com.example.avia1.controllers;

import com.example.avia1.models.*;
import com.example.avia1.repositories.CpnRepository;
import com.example.avia1.repositories.TktRepository;
import com.example.avia1.repositories.UserRepository;
import com.example.avia1.services.CostService;
import com.example.avia1.services.FltService;
import com.example.avia1.services.AipService;
import com.example.avia1.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AviaController {
    @Autowired
    private FltService fltService;

//    @RequestMapping("/")
//    public String index(Model model, @Param("keyword") String keyword) {
//        List<Flt> listFlt = fltService.listAll(keyword);
//        model.addAttribute("listFlt", listFlt);
//        model.addAttribute("keyword", keyword);
//        return "index";
//    }

    @Autowired
    private AipService aipService;

//    @GetMapping("/")
//    public String showSearchForm(Model model) {
//        List<Aip> airports = aipService.listAll();  // убрали keyword
//        model.addAttribute("airports", airports);
//        return "index"; // ваш шаблон index.html
//    }

//    @GetMapping("/")
//    public String showSearchForm(Model model, @RequestParam(required = false) String keyword) {
//        List<Aip> airports = aipService.listAll(keyword);
//        model.addAttribute("airports", airports);
//        return "index";
//    }

    @Autowired
    private UserService serviceu;

    @Autowired
    private CostService costService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CpnRepository cpnRepository;
    @Autowired
    private TktRepository tktRepository;

    @GetMapping("/") //глав страница
    public String showSearchForm(Model model, @RequestParam(required = false) String keyword) {
        List<City> cities = aipService.getCities(keyword);

        // Получение текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Гость"; // или любое другое значение по умолчанию

        model.addAttribute("cities", cities);
        model.addAttribute("username", username); // Добавляем имя пользователя в модель
        return "index";
    }

    @RequestMapping("/basa")
    public String basa(Model model, @Param("keyword") String keyword, @Param("sort") String sort) {
        List<User> listUser = serviceu.listAll(keyword, sort);
        model.addAttribute("listUser", listUser);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
//        listUser.sort(Comparator.comparing(Post::getDate_publ).reversed());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Гость"; // или любое другое значение по умолчанию
        model.addAttribute("username", username); // Добавляем имя пользователя в модель
        return "basa_user";
    }

    @RequestMapping(value = "/basa/save", method = RequestMethod.POST) // Передаем данные из модели
    public String savebasa(@ModelAttribute("user") User user) {
        try{
            serviceu.save(user);// Сохраяем наш список
            return "redirect:/basa";
        } catch (Exception e) {
            return "redirect:/basa";
        }

    }

    @RequestMapping("/basa/edit/{user_id}") // Страница редактирования данных
    public ModelAndView showEditPostForm(@PathVariable(name = "user_id") Long user_id, Model model) {
        ModelAndView mav = new ModelAndView("edit_basa"); // Добавляем шаблон в модель
        User user = serviceu.get(user_id); // Передаем ID, по которому будем редактировать
        mav.addObject("user", user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Гость"; // или любое другое значение по умолчанию
        model.addAttribute("username", username); // Добавляем имя пользователя в модель
        return mav; // Возвращаем полностью модель
    }

    // Обработка поиска
    @GetMapping("/search")
    public String searchFlights(@RequestParam("departureCity") String departureCity,
                                @RequestParam("arrivalCity") String arrivalCity,
                                @RequestParam("departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                @RequestParam(value = "returnDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
                                Model model, HttpSession session) {

        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("departureCity", departureCity);
        searchParams.put("arrivalCity", arrivalCity);
        searchParams.put("departureDate", departureDate.toString());
//        if (returnDate != null) {
//            searchParams.put("returnDate", returnDate.toString());
//        } else {
//            searchParams.put("returnDate", "");  // или просто не класть, но тогда проверка на isEmpty должна работать
//        }

        searchParams.put("returnDate", returnDate != null ? returnDate.toString() : "");

        session.setAttribute("searchParams", searchParams);
        // Простейшая проверка
        if (departureCity.equals(arrivalCity)) {
            model.addAttribute("error", "Города отправления и прибытия не могут совпадать");
            model.addAttribute("cities", aipService.getCities(null));
            return "index";
        }

        List<Flt> flights = fltService.searchFlights(departureCity, arrivalCity, departureDate);
        model.addAttribute("flights", flights);
        model.addAttribute("departureCity", departureCity);
        model.addAttribute("arrivalCity", arrivalCity);
        model.addAttribute("departureDate", departureDate);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        boolean hasPassenger = false;
        if (isAuthenticated) {
            User user = userRepository.findByUsername(authentication.getName());
            if (user != null && user.getPassenger() != null) {
                hasPassenger = isPassengerComplete(user.getPassenger());
            }
        }

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("hasPassenger", hasPassenger);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Гость"; // или любое другое значение по умолчанию
        model.addAttribute("username", username); // Добавляем имя пользователя в модель
        return "results";
    }

    @GetMapping("/select")
    public String selectFare(@RequestParam("fltId") Integer fltId,
                             @RequestParam("costId") Integer costId,
                             HttpSession session) {
        session.setAttribute("selectedFltId", fltId);
        session.setAttribute("selectedCostId", costId);
        return "redirect:/review";
    }

    @GetMapping("/review")
    public String reviewPage(Model model, HttpSession session) {
        Integer fltId = (Integer) session.getAttribute("selectedFltId");
        Integer costId = (Integer) session.getAttribute("selectedCostId");

        if (fltId == null || costId == null) {
            return "redirect:/";
        }
        // Загружаем рейс и тариф из БД
        Flt flight = fltService.get(fltId)
                .orElseThrow(() -> new RuntimeException("Рейс не найден"));
        Cost cost = costService.get(costId)
                .orElseThrow(() -> new RuntimeException("Тариф не найден"));

//        boolean passengerComplete = false;
//        Pax passenger = null;

//        if (principal != null) {
//            User currentUser = userRepository.findByUsername(principal.getName());
//            if (currentUser != null) {
//                passenger = currentUser.getPassenger();
//                if (passenger != null) {
//                    passengerComplete = isPassengerComplete(passenger);
//                }
//            }
//        }

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAuthenticated = authentication != null
//                && authentication.isAuthenticated()
//                && !(authentication instanceof AnonymousAuthenticationToken);
//
//        boolean hasPassenger = false;
//        if (isAuthenticated) {
//            User user = userRepository.findByUsername(authentication.getName());
//            if (user != null && user.getPassenger() != null) {
//                hasPassenger = isPassengerComplete(user.getPassenger());
//            }
//        }
//
//        model.addAttribute("isAuthenticated", isAuthenticated);
//        model.addAttribute("hasPassenger", hasPassenger);

        model.addAttribute("flight", flight);
        model.addAttribute("cost", cost);
//        model.addAttribute("passenger", passenger);          // может быть null
//        model.addAttribute("passengerComplete", passengerComplete);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Гость";
        model.addAttribute("username", username);
        return "review";
    }

    private boolean isPassengerComplete(Pax pax) {
        return pax.getLast_nm() != null && !pax.getLast_nm().isBlank()
                && pax.getFst_nm() != null && !pax.getFst_nm().isBlank()
                && pax.getBirth_date() != null
                && pax.getPassport() != null && !pax.getPassport().isBlank();
    }

//    @PostMapping("/coupon/create")  // теперь создание через POST
//    public String createCoupon(HttpSession session, Principal principal) {
//        Integer fltId = (Integer) session.getAttribute("selectedFltId");
//        Integer costId = (Integer) session.getAttribute("selectedCostId");
//
//        if (fltId == null || costId == null || principal == null) {
//            return "redirect:/";
//        }
//
//        User currentUser = userRepository.findByUsername(principal.getName());
//        Pax passenger = currentUser.getPassenger();
//
//        if (passenger == null) {
//            return "redirect:/personal_account";  // пассажир не заполнен
//        }
//
//        Flt flight = fltService.get(fltId).orElseThrow();
//        Cost cost = costService.get(costId).orElseThrow();
//
//        Cpn cpn = new Cpn();
//        cpn.setFlt_cpn(flight);
//        cpn.setCost_cpn(cost);
//        cpn.setPax_cpn(passenger);
//
//        cpnRepository.save(cpn);
//
//        // Здесь можно сохранить ID купона в сессии, чтобы показать на странице успеха
//        session.setAttribute("lastCouponId", cpn.getCpn_id());
//
//        return "redirect:/coupon/success";
//    }

    @PostMapping("/coupon/create")
    public String createCoupon(HttpSession session, Principal principal) {
        Integer fltId = (Integer) session.getAttribute("selectedFltId");
        Integer costId = (Integer) session.getAttribute("selectedCostId");

        if (fltId == null || costId == null || principal == null) {
            return "redirect:/";
        }

        Flt flight = fltService.get(fltId).orElseThrow();
        Cost cost = costService.get(costId).orElseThrow();

        Cpn coupon = new Cpn();
        coupon.setFlt_cpn(flight);
        coupon.setCost_cpn(cost);
        // bkg пока null
        cpnRepository.save(coupon);

        // Определяем, первый это купон или второй
        Integer firstCpnId = (Integer) session.getAttribute("firstCpnId");
        if (firstCpnId == null) {
            // Это первый купон (туда)
            session.setAttribute("firstCpnId", coupon.getCpn_id());
        } else {
            // Это второй купон (обратно)
            session.setAttribute("secondCpnId", coupon.getCpn_id());
        }

        // Сохраняем ID последнего созданного купона для страницы успеха
        session.setAttribute("lastCouponId", coupon.getCpn_id());
        return "redirect:/coupon/success";
    }

//    @GetMapping("/coupon/success")
//    public String couponSuccess(Model model, HttpSession session) {
//        Integer cpnId = (Integer) session.getAttribute("lastCouponId");
//        if (cpnId != null) {
//            Cpn cpn = cpnRepository.findById(cpnId).orElse(null);
//            model.addAttribute("cpn", cpn);
//        }
//        return "coupon_success";
//    }

    @GetMapping("/coupon/success")
    public String couponSuccess(Model model, HttpSession session) {
        Integer lastCpnId = (Integer) session.getAttribute("lastCouponId");
        Integer firstCpnId = (Integer) session.getAttribute("firstCpnId");
        Integer secondCpnId = (Integer) session.getAttribute("secondCpnId");

        Cpn lastCpn = cpnRepository.findById(lastCpnId).orElse(null);
        model.addAttribute("cpn", lastCpn);

        // Определяем, показывать ли кнопку "Продолжить обратный поиск"
        // Эта кнопка нужна, если:
        // - в сессии есть returnDate (поиск был туда-обратно)
        // - создан только первый купон (secondCpnId ещё нет)
        Map<String, String> params = (Map<String, String>) session.getAttribute("searchParams");
        boolean hasReturn = params != null && !params.get("returnDate").isEmpty();
        boolean showBackward = hasReturn && secondCpnId == null && firstCpnId != null;
        model.addAttribute("showBackward", showBackward);

        model.addAttribute("hasReturn", hasReturn);

        // Новая переменная: true, если создан только первый купон (можно завершить бронь без обратного билета)
        boolean onlyOneWay = firstCpnId != null && secondCpnId == null;
        model.addAttribute("onlyOneWay", onlyOneWay);

        // Кнопка "Завершить бронирование" нужна всегда, если есть хотя бы один купон
        model.addAttribute("canFinalize", firstCpnId != null);

        List<Cpn> allCoupons = new ArrayList<>();
        Integer firstId = (Integer) session.getAttribute("firstCpnId");
        Integer secondId = (Integer) session.getAttribute("secondCpnId");
        if (firstId != null) allCoupons.add(cpnRepository.findById(firstId).orElse(null));
        if (secondId != null) allCoupons.add(cpnRepository.findById(secondId).orElse(null));
        model.addAttribute("allCoupons", allCoupons);

        return "coupon_success";
    }

    @GetMapping("/continue-backward")
    public String continueBackward(HttpSession session) {
        Map<String, String> params = (Map<String, String>) session.getAttribute("searchParams");
        if (params == null || params.get("returnDate").isEmpty()) {
            return "redirect:/";
        }

        String dep = params.get("arrivalCity");
        String arr = params.get("departureCity");
        String returnDate = params.get("returnDate");

        // Формируем редирект на /search с переставленными городами и датой "обратно"
        return "redirect:/search?departureCity=" + dep +
                "&arrivalCity=" + arr +
                "&departureDate=" + returnDate +
                "&returnDate=" + returnDate; // можно передать любую, всё равно используется только departureDate
    }

//    @RequestMapping("/new_pax") //Добавление клиента
//    public String showNewClientForm(Model model) {
//        Pax pax = new Pax(); //Создаем экземпляр класса Pax, внутри которого "лежит" наша модель базы данных
//        model.addAttribute("pax", pax); //Добавляем в модель данные
//        return "new_pax";
//    }

    @PostMapping("/tkt/finalize")
    public String finalizeTkt(HttpSession session, Principal principal) {
        Integer firstCpnId = (Integer) session.getAttribute("firstCpnId");
        Integer secondCpnId = (Integer) session.getAttribute("secondCpnId");

        if (firstCpnId == null) {
            // Без купонов нельзя
            return "redirect:/";
        }

        User currentUser = userRepository.findByUsername(principal.getName());
        Pax passenger = currentUser.getPassenger();

        if (passenger == null) {
            // Пассажир не заполнен – перенаправляем в личный кабинет
            return "redirect:/personal_account";
        }

        // Создаём бронирование
        Tkt tkt = new Tkt();
        tkt.setPax_tkt(passenger);
        tktRepository.save(tkt);

        // Привязываем купоны к бронированию
        Cpn firstCpn = cpnRepository.findById(firstCpnId).orElseThrow();
        firstCpn.setTkt_cpn(tkt);
        cpnRepository.save(firstCpn);

        if (secondCpnId != null) {
            Cpn secondCpn = cpnRepository.findById(secondCpnId).orElseThrow();
            secondCpn.setTkt_cpn(tkt);
            cpnRepository.save(secondCpn);
        }

        // Сохраняем ID бронирования в сессии для страницы успеха
        session.setAttribute("finalTktId", tkt.getTkt_id());

        // Очищаем промежуточные данные
        session.removeAttribute("firstCpnId");
        session.removeAttribute("secondCpnId");
        session.removeAttribute("lastCouponId");

        return "redirect:/tkt/success";
    }

    @GetMapping("/tkt/success")
    public String TktSuccess(Model model, HttpSession session) {
        Integer tktId = (Integer) session.getAttribute("finalTktId");
        if (tktId != null) {
            Tkt tkt = tktRepository.findById(tktId).orElse(null);
            model.addAttribute("tkt", tkt);
        }

        return "tkt_success";
    }

    @PostMapping("/coupon/cancel")
    public String cancelCoupons(HttpSession session) {
        Integer firstCpnId = (Integer) session.getAttribute("firstCpnId");
        Integer secondCpnId = (Integer) session.getAttribute("secondCpnId");

        if (firstCpnId != null) cpnRepository.deleteById(firstCpnId);
        if (secondCpnId != null) cpnRepository.deleteById(secondCpnId);

        session.removeAttribute("firstCpnId");
        session.removeAttribute("secondCpnId");
        session.removeAttribute("lastCouponId");

        return "redirect:/";   // или на страницу результатов поиска
    }
}

