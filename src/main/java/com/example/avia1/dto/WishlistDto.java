package com.example.avia1.dto;

import lombok.Data;

@Data
public class WishlistDto {
    private final int id;
    private final String city;
    private final double lat;
    private final double lng;
    private final boolean visited;
}