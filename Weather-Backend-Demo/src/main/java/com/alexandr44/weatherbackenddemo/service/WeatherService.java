package com.alexandr44.weatherbackenddemo.service;

import com.alexandr44.weatherbackenddemo.dto.WeatherDto;
import com.alexandr44.weatherbackenddemo.mapper.WeatherMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherService openWeatherService;
    private final HistoryService historyService;
    private final WeatherMapper weatherMapper;

    public WeatherDto getWeatherByCity(final String city, final String countryCode) {
        log.debug("Get weather by city: {} and countryCode: {}", city, countryCode);
        historyService.addHistoryCity(city, countryCode);
        return weatherMapper.map(openWeatherService.getWeatherByCity(city, countryCode));
    }

    public WeatherDto getWeatherByCoordinates(final Double lon, final Double lat) {
        log.debug("Get weather by coordinates lon: {} and lat: {}", lon, lat);
        historyService.addHistoryCoord(lon, lat);
        return weatherMapper.map(openWeatherService.getWeatherByCoordinates(lon, lat));
    }

}
