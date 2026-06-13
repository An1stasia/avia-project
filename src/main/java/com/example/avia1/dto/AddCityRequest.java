package com.example.avia1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCityRequest {
    private String city;
    private double lat;
    private double lng;
}
