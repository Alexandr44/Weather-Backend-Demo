package com.alexandr44.weatherbackenddemo.integration;

import com.alexandr44.weatherbackenddemo.dto.HistoryResponse;
import com.alexandr44.weatherbackenddemo.entity.HistoryItem;
import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HistoryControllerTest extends BaseWireMockTest {

    @Autowired
    MockMvc mockMvc;

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"Moscow", "Ufa"})
    @Sql("/sql/history_items.sql")
    @WithMockUser(username = "testUser", roles = "USER")
    void testRequestByCity(final String city) {
        final List<HistoryItem> historyItems =
            historyRepository.findAllByCityAndCountryCodeAndLonAndLat(city, null, null, null);

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/history")
                .param("city", city)
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn();

        final String historyResponseStr = mvcResult.getResponse().getContentAsString();
        final HistoryResponse historyResponse = objectMapper.readValue(historyResponseStr, HistoryResponse.class);

        assertEquals(historyItems.size(), historyResponse.getCount());
        assertEquals("city=" + city, historyResponse.getParams());
    }

    @Test
    @SneakyThrows
    @Sql("/sql/history_items.sql")
    @WithMockUser(username = "testUser", roles = "USER")
    void testRequestByCityAndCountryCode() {
        final String city = "Ufa";
        final String countryCode = "RU";
        final List<HistoryItem> historyItems =
            historyRepository.findAllByCityAndCountryCodeAndLonAndLat(city, countryCode, null, null);

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/history")
                .param("city", city)
                .param("countryCode", countryCode)
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn();

        final String historyResponseStr = mvcResult.getResponse().getContentAsString();
        final HistoryResponse historyResponse = objectMapper.readValue(historyResponseStr, HistoryResponse.class);

        assertEquals(historyItems.size(), historyResponse.getCount());
        assertEquals("city=" + city + "&countryCode=" + countryCode, historyResponse.getParams());
    }

    @Test
    @SneakyThrows
    @Sql("/sql/history_items.sql")
    @WithMockUser(username = "testUser", roles = "USER")
    void testRequestByLatAndLon() {
        final Double lon = 56.0375;
        final Double lat = 54.775;
        final List<HistoryItem> historyItems =
            historyRepository.findAllByCityAndCountryCodeAndLonAndLat(null, null, lon, lat);

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/history")
                .param("lon", lon.toString())
                .param("lat", lat.toString())
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn();

        final String historyResponseStr = mvcResult.getResponse().getContentAsString();
        final HistoryResponse historyResponse = objectMapper.readValue(historyResponseStr, HistoryResponse.class);

        assertEquals(historyItems.size(), historyResponse.getCount());
        assertEquals("lon=" + lon + "&lat=" + lat, historyResponse.getParams());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("getTypes")
    @Sql("/sql/history_items.sql")
    @WithMockUser(username = "testUser", roles = "USER")
    void testRequestByType(final HistoryType type) {
        final int count = historyRepository.countAllByType(type);

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/history-by-type")
                .param("historyType", type.name())
            )
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn();

        final String historyResponseStr = mvcResult.getResponse().getContentAsString();
        final HistoryResponse historyResponse = objectMapper.readValue(historyResponseStr, HistoryResponse.class);

        assertEquals(count, historyResponse.getCount());
        assertEquals("type=" + type, historyResponse.getParams());
    }

    private List<HistoryType> getTypes() {
        return Arrays.stream(HistoryType.values())
            .toList();
    }

}
