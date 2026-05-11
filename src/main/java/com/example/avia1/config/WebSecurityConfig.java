package com.example.avia1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/", "/register", "/login").permitAll() // Разрешить доступ к страницам регистрации и входа
                                .requestMatchers("/delete/**").hasRole("ADMIN") // Доступ к удалению книги только для ADMIN
                                .requestMatchers("/edit/**").hasRole("ADMIN") // Доступ к редактированию книги только для ADMIN
                                .requestMatchers("/basa").hasRole("ADMIN") // Доступ к редактированию книги только для ADMIN
                                .requestMatchers("/personal_account").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Перенаправление на главную страницу после успешного входа
                        .permitAll()
                )
//                .logout(logout -> logout.permitAll());
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода
                        .logoutSuccessUrl("/") // URL для перенаправления после выхода
                        .invalidateHttpSession(true) // Очищаем сессию
                        .clearAuthentication(true) // Очищаем аутентификацию
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
//                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select username,password,'true' as enabled from User where username = ?")
//                .authoritiesByUsernameQuery("select u.username, u.role from User u where u.username=?");
                .authoritiesByUsernameQuery("select u.username, concat('ROLE_', u.role) from User u where u.username=?");


        return authenticationManagerBuilder.build();
    }

}

