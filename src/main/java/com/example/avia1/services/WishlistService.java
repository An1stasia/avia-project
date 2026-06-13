package com.example.avia1.services;

import com.example.avia1.models.User;
import com.example.avia1.models.Wishlist;
import com.example.avia1.repositories.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    public List<Wishlist> getUserWishlist(User user) {
        return wishlistRepository.findAllByUser(user);
    }

    public Wishlist addCity(User user, String cityName, double lat, double lng) {
        Wishlist item = new Wishlist();
        item.setUser(user);
        item.setCity(cityName);
        item.setLatitude(lat);
        item.setLongitude(lng);
        item.setVisited(false);
        return wishlistRepository.save(item);
    }

    public void toggleVisited(int id, User user) {
        Wishlist item = wishlistRepository.findById((long) id).orElseThrow();
        if (item.getUser().getUser_id() != user.getUser_id()) {
            throw new RuntimeException("Нет доступа");
        }
        item.setVisited(!item.getVisited());
        wishlistRepository.save(item);
    }

    public void deleteCity(int id, User user) {
        Wishlist item = wishlistRepository.findById((long) id).orElseThrow();
        if (item.getUser().getUser_id() != user.getUser_id()) {
            throw new RuntimeException("Нет доступа");
        }
        wishlistRepository.delete(item);
    }
}