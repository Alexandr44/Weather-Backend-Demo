package com.alexandr44.weatherbackenddemo.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherResponse {

    private Coordinates coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private Integer visibility;
    private Wind wind;
    private Clouds clouds;
    private Long dt;
    private Sys sys;
    private Integer timezone;
    private Integer id;
    private String name;
    private Integer cod;

    @Data
    public static class Coordinates {
        private Double lon;
        private Double lat;
    }

    @Data
    public static class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    public static class Main {
        private Double temp;
        private Double feelsLike;
        private Double tempMin;
        private Double tempMax;
        private Integer pressure;
        private Integer humidity;
        private Integer seaLevel;
        private Integer grndLevel;
    }

    @Data
    public static class Wind {
        private Double speed;
        private Integer deg;
        private Double gust;
    }

    @Data
    public static class Clouds {
        private Integer all;
    }

    @Data
    public static class Sys {
        private Integer type;
        private Integer id;
        private String country;
        private Long sunrise;
        private Long sunset;
    }

}
