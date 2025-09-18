package com.alexandr44.weatherbackenddemo.service;

import com.alexandr44.weatherbackenddemo.BaseTest;
import com.alexandr44.weatherbackenddemo.client.WeatherClient;
import com.alexandr44.weatherbackenddemo.config.CacheConfig;
import com.alexandr44.weatherbackenddemo.dto.OpenWeatherResponse;
import com.alexandr44.weatherbackenddemo.dto.WeatherDto;
import com.alexandr44.weatherbackenddemo.entity.HistoryItem;
import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import com.alexandr44.weatherbackenddemo.mapper.WeatherMapper;
import com.alexandr44.weatherbackenddemo.repository.HistoryRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherServiceTest extends BaseTest {

    @Autowired
    WeatherService weatherService;
    @Autowired
    WeatherMapper weatherMapper;
    @Autowired
    CacheManager cacheManager;

    @MockitoBean
    WeatherClient weatherClient;
    @MockitoBean
    HistoryRepository historyRepository;

    @Captor
    ArgumentCaptor<HistoryItem> historyItemCaptor;

    @Test
    @SneakyThrows
    void testSuccessByCity() {
        final String city = "Moscow";
        final String countryCode = "RU";
        final String query = city + "," + countryCode;

        final String openWeatherResponseJson = readFromFile("json/OpenWeatherResponse.json");
        final OpenWeatherResponse openWeatherResponse = objectMapper.readValue(
            openWeatherResponseJson, OpenWeatherResponse.class
        );
        when(weatherClient.getWeatherByCity(eq(query), anyString(), anyString(), anyString())).thenReturn(
            ResponseEntity.ok(openWeatherResponse)
        );

        final WeatherDto weatherDto = weatherService.getWeatherByCity(city, countryCode);
        assertEquals(weatherMapper.map(openWeatherResponse), weatherDto);

        verify(weatherClient, times(1)).getWeatherByCity(eq(query), anyString(), anyString(), anyString());
        verify(historyRepository, times(1)).save(historyItemCaptor.capture());
        final HistoryItem historyItem = historyItemCaptor.getValue();
        assertEquals(city, historyItem.getCity());
        assertEquals(countryCode, historyItem.getCountryCode());
        assertEquals(HistoryType.CITY, historyItem.getType());
        assertNull(historyItem.getLon());
        assertNull(historyItem.getLat());
    }

    @Test
    @SneakyThrows
    void testSuccessByCoordinates() {
        final Double lon = 37.6156;
        final Double lat = 55.7522;

        final String openWeatherResponseJson = readFromFile("json/OpenWeatherResponse.json");
        final OpenWeatherResponse openWeatherResponse = objectMapper.readValue(
            openWeatherResponseJson, OpenWeatherResponse.class
        );
        when(weatherClient.getWeatherByCoordinates(eq(lon), eq(lat), anyString(), anyString(), anyString())).thenReturn(
            ResponseEntity.ok(openWeatherResponse)
        );

        final WeatherDto weatherDto = weatherService.getWeatherByCoordinates(lon, lat);
        assertEquals(weatherMapper.map(openWeatherResponse), weatherDto);

        verify(weatherClient, times(1))
            .getWeatherByCoordinates(eq(lon), eq(lat), anyString(), anyString(), anyString());
        verify(historyRepository, times(1)).save(historyItemCaptor.capture());
        final HistoryItem historyItem = historyItemCaptor.getValue();
        assertNull(historyItem.getCity());
        assertNull(historyItem.getCountryCode());
        assertEquals(HistoryType.COORDINATES, historyItem.getType());
        assertEquals(lon, historyItem.getLon());
        assertEquals(lat, historyItem.getLat());
    }

    @Test
    @SneakyThrows
    void testCachedByCity() {
        cacheManager.getCache(CacheConfig.WEATHER_CACHE_NAME).clear();

        final String city = "Moscow";
        final String countryCode = "RU";

        when(weatherClient.getWeatherByCity(anyString(), anyString(), anyString(), anyString())).thenReturn(
            ResponseEntity.ok(new OpenWeatherResponse())
        );

        weatherService.getWeatherByCity(city, countryCode);
        weatherService.getWeatherByCity(city, countryCode);
        weatherService.getWeatherByCity(city, countryCode);

        verify(weatherClient, times(1)).getWeatherByCity(anyString(), anyString(), anyString(), anyString());
        verify(historyRepository, times(3)).save(any());
    }

    @Test
    @SneakyThrows
    void testCachedByCoordinates() {
        cacheManager.getCache(CacheConfig.WEATHER_CACHE_NAME).clear();

        final Double lon = 37.6156;
        final Double lat = 55.7522;

        when(weatherClient.getWeatherByCoordinates(anyDouble(), anyDouble(), anyString(), anyString(), anyString()))
            .thenReturn(ResponseEntity.ok(new OpenWeatherResponse()));

        weatherService.getWeatherByCoordinates(lon, lat);
        weatherService.getWeatherByCoordinates(lon, lat);
        weatherService.getWeatherByCoordinates(lon, lat);

        verify(weatherClient, times(1))
            .getWeatherByCoordinates(anyDouble(), anyDouble(), anyString(), anyString(), anyString());
        verify(historyRepository, times(3)).save(any());
    }


}
