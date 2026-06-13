package com.example.avia1.repositories;

import com.example.avia1.models.User;
import com.example.avia1.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findAllByUser(User user);
}
