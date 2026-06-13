package com.example.avia1.models;

import jakarta.persistence.*;
import lombok.Getter;import lombok.Setter;

import java.util.List;

@Setter
@Entity
@Table(name = "User")

public class User {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    @Getter
    private String username;
    @Getter
    private String password;

    @Enumerated(EnumType.STRING)
    @Getter
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pax_id")
    private Pax passenger;

    public User() {    }

    // Геттер для ID
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public User(Pax passenger) {
        this.passenger = passenger;
    }

    public Pax getPassenger() {
        return passenger;
    }

    public void setPassenger(Pax passenger) {
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return "User [user_id=" + user_id + ", username=" + username + ", password=" + password + ", role=" + role + "]";
    }

    @OneToMany(mappedBy = "user")
    private List<Wishlist> wishlistItems;
}