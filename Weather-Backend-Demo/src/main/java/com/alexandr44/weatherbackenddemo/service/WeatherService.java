package com.alexandr44.weatherbackenddemo.service;

import com.alexandr44.weatherbackenddemo.client.WeatherClient;
import com.alexandr44.weatherbackenddemo.dto.OpenWeatherResponse;
import com.alexandr44.weatherbackenddemo.dto.WeatherDto;
import com.alexandr44.weatherbackenddemo.mapper.WeatherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weather.api-key}")
    private String apiKey;

    @Value("${weather.lang}")
    private String language;

    @Value("${weather.units}")
    private String units;

    private final WeatherClient weatherClient;
    private final WeatherMapper weatherMapper;

    public WeatherDto getWeatherByCity(final String city, final String countryCode) {
        final ResponseEntity<OpenWeatherResponse> response = weatherClient.getWeatherByCity(
            buildQuery(city, countryCode), apiKey, units, language
        );
        return weatherMapper.map(response.getBody());
    }

    private String buildQuery(final String city, final String countryCode) {
        if (countryCode == null || countryCode.isEmpty()) {
            return city;
        } else {
            return city + "," + countryCode;
        }
    }

}
