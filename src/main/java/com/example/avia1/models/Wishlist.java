package com.example.avia1.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "Wishlist")
public class Wishlist {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int list_id;
    @Getter
    private String city;
    @Getter
    private Double latitude;
    @Getter
    private Double longitude;
    @Getter
    private Boolean visited;

    public Wishlist() {}

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}