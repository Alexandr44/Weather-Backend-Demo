package com.alexandr44.weatherbackenddemo.controller;

import com.alexandr44.weatherbackenddemo.dto.WeatherDto;
import com.alexandr44.weatherbackenddemo.service.WeatherService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather-by-city")
    public ResponseEntity<WeatherDto> getWeatherByCity(
        @RequestParam @NotBlank @Size(min = 2, max = 100) final String city,
        @RequestParam(required = false) final String countryCode
    ) {
        return ResponseEntity.ok(weatherService.getWeatherByCity(city, countryCode));
    }

    @GetMapping("/weather-by-coord")
    public ResponseEntity<WeatherDto> getWeatherByCoordinates(
        @RequestParam Double lon,
        @RequestParam Double lat
    ) {
        return ResponseEntity.ok(weatherService.getWeatherByCoordinates(lon, lat));
    }

}
