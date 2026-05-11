package com.example.avia1.repositories;

import com.example.avia1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT p FROM User p WHERE CONCAT(p.username, '', p.password, '', p.role) LIKE %?1%")
    List<User> search(String keyword);
    User findByUsername(String username);
}
