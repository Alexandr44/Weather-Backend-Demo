package com.alexandr44.weatherbackenddemo.service;

import com.alexandr44.weatherbackenddemo.client.WeatherClient;
import com.alexandr44.weatherbackenddemo.config.CacheConfig;
import com.alexandr44.weatherbackenddemo.dto.OpenWeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenWeatherService {

    @Value("${weather.api-key}")
    private String apiKey;

    @Value("${weather.lang}")
    private String language;

    @Value("${weather.units}")
    private String units;

    private final WeatherClient weatherClient;

    @Cacheable(value = CacheConfig.WEATHER_CACHE_NAME, key = "#city + #countryCode")
    public OpenWeatherResponse getWeatherByCity(final String city, final String countryCode) {
        log.debug("Requesting weather by city with params: city {}, countryCode {}", city, countryCode);
        final ResponseEntity<OpenWeatherResponse> response = weatherClient.getWeatherByCity(
            buildQuery(city, countryCode), apiKey, units, language
        );
        return response.getBody();
    }

    @Cacheable(value = CacheConfig.WEATHER_CACHE_NAME, key = "#lon + #lat")
    public OpenWeatherResponse getWeatherByCoordinates(final Double lon, final Double lat) {
        log.debug("Requesting weather by coordinates with params: lon {}, lat {}", lon, lat);
        final ResponseEntity<OpenWeatherResponse> response = weatherClient.getWeatherByCoordinates(
            lon, lat, apiKey, units, language
        );
        return response.getBody();
    }

    private String buildQuery(final String city, final String countryCode) {
        if (countryCode == null || countryCode.isEmpty()) {
            return city;
        } else {
            return city + "," + countryCode;
        }
    }

}
