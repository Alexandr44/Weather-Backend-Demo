package com.alexandr44.weatherbackenddemo.service;

import com.alexandr44.weatherbackenddemo.client.WeatherClient;
import com.alexandr44.weatherbackenddemo.config.CacheConfig;
import com.alexandr44.weatherbackenddemo.dto.OpenWeatherResponse;
import com.alexandr44.weatherbackenddemo.dto.WeatherDto;
import com.alexandr44.weatherbackenddemo.mapper.WeatherMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
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

    @Cacheable(value = CacheConfig.WEATHER_CACHE_NAME, key = "#city + #countryCode")
    public WeatherDto getWeatherByCity(final String city, final String countryCode) {
        log.debug("Requesting weather by city with params: city {}, countryCode {}", city, countryCode);
        final ResponseEntity<OpenWeatherResponse> response = weatherClient.getWeatherByCity(
            buildQuery(city, countryCode), apiKey, units, language
        );
        return weatherMapper.map(response.getBody());
    }

    @Cacheable(value = CacheConfig.WEATHER_CACHE_NAME, key = "#lon + #lat")
    public WeatherDto getWeatherByCoordinates(final Double lon, final Double lat) {
        log.debug("Requesting weather by coordinates with params: lon {}, lat {}", lon, lat);
        final ResponseEntity<OpenWeatherResponse> response = weatherClient.getWeatherByCoordinates(
            lon, lat, apiKey, units, language
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
