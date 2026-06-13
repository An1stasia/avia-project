package com.example.avia1.controllers;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.avia1.dto.AddCityRequest;
import com.example.avia1.dto.WishlistDto;
import com.example.avia1.models.*;
import com.example.avia1.repositories.PaxRepository;
import com.example.avia1.repositories.UserRepository;
//import com.fasterxml.jackson.core.Base64Variant;

import com.example.avia1.services.CpnService;
import com.example.avia1.services.PaxService;
import com.example.avia1.services.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.annotation.WebFilter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;


@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaxRepository paxRepository;

    @Autowired
    private PaxService paxService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private TaskExecutionProperties taskExecutionProperties;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private CpnService cpnService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        user.setPassenger(new Pax());
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser (User user, Map<String, Object> model) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginForm() {
//        User user = userRepository.findByUsername(username);
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            // Успешный вход
//            return "redirect:/"; // Перенаправление на домашнюю страницу
//        } else {
//            model.addAttribute("error", "Неверное имя пользователя или пароль");
//            return "login"; // Возврат на страницу входа с ошибкой
//        } // имя шаблона для страницы входа
////        return "login";
        return "login";
    }

    @GetMapping("/logout")public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("/personal_account")
    public String personalAccount(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        List<Wishlist> wishlistItems = wishlistService.getUserWishlist(user);

        List<WishlistDto> wishlistDtos = wishlistItems.stream()
                .map(item -> new WishlistDto(item.getList_id(), item.getCity(),
                        item.getLatitude(), item.getLongitude(),
                        item.getVisited()))
                .collect(Collectors.toList());

        List<Cpn> cpn = Collections.emptyList();
        if (user.getPassenger() != null) {
            cpn = cpnService.getUpcomingCouponsSorted(user.getPassenger());
        }

        model.addAttribute("user", user);
        model.addAttribute("wishlistItems", wishlistDtos);
        model.addAttribute("cpn", cpn);
        return "personal_account";
    }

    @PostMapping("/wishlist/add")
    @ResponseBody
    public ResponseEntity<WishlistDto> addCity(@RequestBody AddCityRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Wishlist item = wishlistService.addCity(user, request.getCity(), request.getLat(), request.getLng());
        WishlistDto dto = new WishlistDto(item.getList_id(), item.getCity(),
                item.getLatitude(), item.getLongitude(),
                item.getVisited());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/wishlist/{id}/toggle")
    public ResponseEntity<?> toggleVisited(@PathVariable int id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        wishlistService.toggleVisited(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable int id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        wishlistService.deleteCity(id, user);
        return ResponseEntity.ok().build();
    }

//    @RequestMapping("/passenger/new") // Добавление пассажира
//    public String showNewPaXForm(Model model) {
//        try {
//            Pax pax = new Pax(); // Создаем экземпляр класса Pax, внутри которого "лежит" наша модель базы данных
//            model.addAttribute("pax", pax); // Добавляем в модель данные
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String username = authentication != null ? authentication.getName() : "Гость"; // или любое другое значение по умолчанию
//            model.addAttribute("username", username); // Добавляем имя пользователя в модель
//            return "pax_register";}
//        catch (Exception e){
//            return "/pax_register";
//        }
//    }

    @PostMapping(value = "/passenger/save")
    public String savePax(@ModelAttribute("pax") Pax pax, Principal principal) {
        try {
            User currentUser = userRepository.findByUsername(principal.getName());
            paxService.save(pax);
            currentUser.setPassenger(pax);
            userRepository.save(currentUser);
            return "redirect:/personal_account";}
        catch (Exception e){
            return "redirect:/personal_account";
        }
    }

    @RequestMapping("/passenger/edit/{pax_id}")
    public ModelAndView showEditPaxForm(@PathVariable(name = "pax_id") int pax_id, Model model) {
        ModelAndView mav = new ModelAndView("pax_edit");
        Pax pax = paxService.get(pax_id);
        mav.addObject("pax", pax);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Гость";
        model.addAttribute("username", username);
        return mav;
    }
}