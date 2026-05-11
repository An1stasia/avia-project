package com.example.avia1.controllers;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.avia1.models.Pax;
import com.example.avia1.models.Role;
import com.example.avia1.models.User;
import com.example.avia1.repositories.PaxRepository;
import com.example.avia1.repositories.UserRepository;
//import com.fasterxml.jackson.core.Base64Variant;

import com.example.avia1.services.PaxService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
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


@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaxRepository paxRepository;

    @Autowired
    private PaxService paxService;

    @Autowired
    public PasswordEncoder passwordEncoder; // Изменен тип здесь
    @Autowired
    private TaskExecutionProperties taskExecutionProperties;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // имя шаблона для страницы регистрации
    }

    @PostMapping("/register")
    public String registerUser (User user, Map<String, Object> model) {
        // Хешируем пароль перед сохранением
        String encodedPassword = passwordEncoder.encode(user.getPassword()); // Исправлено
        user.setPassword(encodedPassword);
//        user.setPassword(user.getPassword());
        user.setRole(Role.USER); // Установка роли по умолчанию
        userRepository.save(user);
        return "redirect:/"; // Перенаправление на страницу входа
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
        // Получаем текущую аутентификацию
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // прекращаем  сессию
        request.getSession().invalidate();
        // Перенаправляем на страницу входа
        return "redirect:/";
    }

    @GetMapping("/personal_account")
    public String personalAccount(Model model, Principal principal) {
        // principal.getName() содержит текущее имя пользователя
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "personal_account";
    }

    @RequestMapping("/passenger/new") // Добавление пассажира
    public String showNewPaXForm(Model model) {
        try {
            Pax pax = new Pax(); // Создаем экземпляр класса Pax, внутри которого "лежит" наша модель базы данных
            model.addAttribute("pax", pax); // Добавляем в модель данные

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "Гость"; // или любое другое значение по умолчанию
            model.addAttribute("username", username); // Добавляем имя пользователя в модель
            return "pax_register";}
        catch (Exception e){
            return "/pax_register";
        }
    }

    @PostMapping(value = "/passenger/save") // Передаем данные из модели
    public String savePax(@ModelAttribute("pax") Pax pax, Principal principal) {
        try {
            User currentUser = userRepository.findByUsername(principal.getName());
            paxService.save(pax); // Сохраяем наш список
            currentUser.setPassenger(pax);
            userRepository.save(currentUser);
            return "redirect:/personal_account";}
        catch (Exception e){
            return "redirect:/personal_account";
        }
    }

    @RequestMapping("/passenger/edit/{pax_id}") // Страница редактирования данных
    public ModelAndView showEditPaxForm(@PathVariable(name = "pax_id") int pax_id, Model model) {
        ModelAndView mav = new ModelAndView("pax_edit"); // Добавляем шаблон в модель
        Pax pax = paxService.get(pax_id); // Передаем ID, по которому будем редактировать
        mav.addObject("pax", pax);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Гость"; // или любое другое значение по умолчанию
        model.addAttribute("username", username); // Добавляем имя пользователя в модель
        return mav; // Возвращаем полностью модель
    }
}
