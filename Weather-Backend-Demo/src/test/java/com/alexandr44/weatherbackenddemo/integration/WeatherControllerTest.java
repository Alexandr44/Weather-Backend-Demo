package com.alexandr44.weatherbackenddemo.integration;

import com.alexandr44.weatherbackenddemo.client.WeatherClient;
import com.alexandr44.weatherbackenddemo.config.CacheConfig;
import com.alexandr44.weatherbackenddemo.dto.OpenWeatherResponse;
import com.alexandr44.weatherbackenddemo.dto.WeatherDto;
import com.alexandr44.weatherbackenddemo.entity.HistoryItem;
import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import com.alexandr44.weatherbackenddemo.mapper.WeatherMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherControllerTest extends BaseWireMockTest {

    private static final Map<String, String> COMMON_PARAMS_MAO =
        Map.of("appid", "123", "units", "metric", "lang", "ru");

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    WeatherMapper weatherMapper;

    @MockitoSpyBean
    WeatherClient weatherClient;

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testSuccessGetByCity() {
        cacheManager.getCache(CacheConfig.WEATHER_CACHE_NAME).clear();
        assertTrue(historyRepository.findAll().isEmpty());

        final String openWeatherResponseJson = readFromFile("json/OpenWeatherResponse.json");
        final OpenWeatherResponse openWeatherResponse =
            objectMapper.readValue(openWeatherResponseJson, OpenWeatherResponse.class);
        final WeatherDto weatherDto = weatherMapper.map(openWeatherResponse);

        final Map<String, String> params = new HashMap<>(Map.of("q", weatherDto.getCity()));
        params.putAll(COMMON_PARAMS_MAO);
        stubGet(
            "/weather-api/weather",
            params,
            openWeatherResponseJson,
            HttpStatus.OK
        );

        mockMvc.perform(get("/api/v1/weather-by-city")
                .param("city", weatherDto.getCity())
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().json(objectMapper.writeValueAsString(weatherDto)));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertEquals(1, historyItems.size());

        final HistoryItem historyItem = historyItems.getFirst();
        assertEquals(weatherDto.getCity(), historyItem.getCity());
        assertNull(historyItem.getCountryCode());
        assertEquals(HistoryType.CITY, historyItem.getType());
        assertNull(historyItem.getLon());
        assertNull(historyItem.getLat());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testSuccessGetByCityAndCountryCode() {
        cacheManager.getCache(CacheConfig.WEATHER_CACHE_NAME).clear();
        assertTrue(historyRepository.findAll().isEmpty());

        final String openWeatherResponseJson = readFromFile("json/OpenWeatherResponse.json");
        final OpenWeatherResponse openWeatherResponse =
            objectMapper.readValue(openWeatherResponseJson, OpenWeatherResponse.class);
        final WeatherDto weatherDto = weatherMapper.map(openWeatherResponse);

        final Map<String, String> params = new HashMap<>(
            Map.of("q", weatherDto.getCity() + "," + weatherDto.getCountryCode())
        );
        params.putAll(COMMON_PARAMS_MAO);
        stubGet(
            "/weather-api/weather",
            params,
            openWeatherResponseJson,
            HttpStatus.OK
        );

        mockMvc.perform(get("/api/v1/weather-by-city")
                .param("city", weatherDto.getCity())
                .param("countryCode", weatherDto.getCountryCode())
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().json(objectMapper.writeValueAsString(weatherDto)));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertEquals(1, historyItems.size());

        final HistoryItem historyItem = historyItems.getFirst();
        assertEquals(weatherDto.getCity(), historyItem.getCity());
        assertEquals(weatherDto.getCountryCode(), historyItem.getCountryCode());
        assertEquals(HistoryType.CITY, historyItem.getType());
        assertNull(historyItem.getLon());
        assertNull(historyItem.getLat());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testSuccessGetByLonAndLat() {
        cacheManager.getCache(CacheConfig.WEATHER_CACHE_NAME).clear();
        assertTrue(historyRepository.findAll().isEmpty());

        final String openWeatherResponseJson = readFromFile("json/OpenWeatherResponse.json");
        final OpenWeatherResponse openWeatherResponse =
            objectMapper.readValue(openWeatherResponseJson, OpenWeatherResponse.class);
        final WeatherDto weatherDto = weatherMapper.map(openWeatherResponse);

        final Map<String, String> params = new HashMap<>(
            Map.of("lon", weatherDto.getLongitude().toString(), "lat", weatherDto.getLatitude().toString())
        );
        params.putAll(COMMON_PARAMS_MAO);
        stubGet(
            "/weather-api/weather",
            params,
            openWeatherResponseJson,
            HttpStatus.OK
        );

        mockMvc.perform(get("/api/v1/weather-by-coord")
                .param("lon", weatherDto.getLongitude().toString())
                .param("lat", weatherDto.getLatitude().toString())
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().json(objectMapper.writeValueAsString(weatherDto)));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertEquals(1, historyItems.size());

        final HistoryItem historyItem = historyItems.getFirst();
        assertNull(historyItem.getCity());
        assertNull(historyItem.getCountryCode());
        assertEquals(HistoryType.COORDINATES, historyItem.getType());
        assertEquals(weatherDto.getLongitude(), historyItem.getLon());
        assertEquals(weatherDto.getLatitude(), historyItem.getLat());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testFailedGetByCityBadRequest() {
        assertTrue(historyRepository.findAll().isEmpty());

        mockMvc.perform(get("/api/v1/weather-by-city")
                .param("city", "M")
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertTrue(historyItems.isEmpty());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testFailedGetByCityBadRequestNoValue() {
        assertTrue(historyRepository.findAll().isEmpty());

        mockMvc.perform(get("/api/v1/weather-by-city"))
            .andDo(print())
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertTrue(historyItems.isEmpty());
    }

    @Test
    @SneakyThrows
    void testFailedGetByCityNoAuth() {
        assertTrue(historyRepository.findAll().isEmpty());

        mockMvc.perform(get("/api/v1/weather-by-city")
                .param("city", "Moscow")
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertTrue(historyItems.isEmpty());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testFailedGetByCityInternalError() {
        assertTrue(historyRepository.findAll().isEmpty());

        doThrow(new RuntimeException("internal error"))
            .when(weatherClient).getWeatherByCity(anyString(), anyString(), anyString(), anyString());

        mockMvc.perform(get("/api/v1/weather-by-city")
                .param("city", "Moscow")
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertEquals(1, historyItems.size());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testFailedGetByLonAndLatBadRequestNoLon() {
        assertTrue(historyRepository.findAll().isEmpty());

        mockMvc.perform(get("/api/v1/weather-by-coord")
                .param("lat", "55.7522")
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertTrue(historyItems.isEmpty());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testFailedGetByLonAndLatBadRequestNoLat() {
        assertTrue(historyRepository.findAll().isEmpty());

        mockMvc.perform(get("/api/v1/weather-by-coord")
                .param("lon", "37.6156")
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertTrue(historyItems.isEmpty());
    }

    @Test
    @SneakyThrows
    void testFailedGetByLonAndLatNoAuth() {
        assertTrue(historyRepository.findAll().isEmpty());

        mockMvc.perform(get("/api/v1/weather-by-coord")
                .param("lon", "37.6156")
                .param("lat", "55.7522")
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertTrue(historyItems.isEmpty());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "testUser", roles = "USER")
    void testFailedGetByLonAndLatInternalError() {
        assertTrue(historyRepository.findAll().isEmpty());

        doThrow(new RuntimeException("internal error"))
            .when(weatherClient).getWeatherByCoordinates(anyDouble(), anyDouble(), anyString(), anyString(), anyString());

        mockMvc.perform(get("/api/v1/weather-by-coord")
                .param("lon", "37.6156")
                .param("lat", "55.7522")
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        final List<HistoryItem> historyItems = historyRepository.findAll();
        assertEquals(1, historyItems.size());
    }

}
