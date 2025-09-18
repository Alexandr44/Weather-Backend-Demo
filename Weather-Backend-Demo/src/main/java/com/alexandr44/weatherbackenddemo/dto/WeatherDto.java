package com.alexandr44.weatherbackenddemo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeatherDto {

    private String city;
    private String countryCode;
    private Double longitude;
    private Double latitude;
    private LocalDateTime timestamp;
    private Weather weather;

    @Data
    public static class Weather {
        private String main;
        private String description;
        private String temperature;
        private String temperatureFeelsLike;
        private String temperatureMin;
        private String temperatureMax;
        private String pressure;
        private String humidity;
        private String windSpeed;
        private String clouds;
    }

}
