package com.example.avia1.models;

public class City {
    private final String city;
    private final String country;
    private final String city_rus;

    public City(String city, String country, String city_rus) {
        this.city = city;
        this.country = country;
        this.city_rus = city_rus;
    }

    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getCity_rus() { return city_rus; }
}
