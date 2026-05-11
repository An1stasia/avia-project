package com.example.avia1.services;

import com.example.avia1.models.User;
import com.example.avia1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public List<User> listAll(String keyword, String sort) {
        if (keyword != null) {
            return repo.search(keyword); // Созданный метод в репозитории
        }
        return repo.findAll();// Встроенный в коллекцию метод
    }

    //Этот метод сохраняет студента в базе данных, используя репозиторий
    public void save(User user) {
        repo.save(user);
    }
    //Этот метод получает студента по его идентификатору.
    public User get(Long user_id) {
        return repo.findById(user_id).get();
    }
    //Этот метод удаляет студента из базы данных по его идентификатору
    public void delete(Long user_id) {
        repo.deleteById(user_id);
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }
}

