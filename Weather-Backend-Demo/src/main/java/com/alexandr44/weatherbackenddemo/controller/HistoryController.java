package com.alexandr44.weatherbackenddemo.controller;

import com.alexandr44.weatherbackenddemo.dto.HistoryResponse;
import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import com.alexandr44.weatherbackenddemo.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/history")
    public ResponseEntity<HistoryResponse> getWeatherByCity(
        @RequestParam(required = false) final String city,
        @RequestParam(required = false) final String countryCode,
        @RequestParam(required = false) final Double lon,
        @RequestParam(required = false) final Double lat
    ) {
        return ResponseEntity.ok(historyService.getHistoryStatistic(city, countryCode, lon, lat));
    }

    @GetMapping("/history-by-type")
    public ResponseEntity<HistoryResponse> getWeatherByCity(
        @RequestParam final HistoryType historyType
    ) {
        return ResponseEntity.ok(historyService.getHistoryStatisticByType(historyType));
    }

}
