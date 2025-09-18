package com.alexandr44.weatherbackenddemo.controller;

import com.alexandr44.weatherbackenddemo.dto.ErrorDto;
import com.alexandr44.weatherbackenddemo.dto.HistoryResponse;
import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import com.alexandr44.weatherbackenddemo.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "History API", description = "API for history statistics")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/history")
    @Operation(
        summary = "Requests history statistics by params",
        description = "Returns requested params and count of requests in history",
        security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Not authorized", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        )),
        @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        ))
    })
    public ResponseEntity<HistoryResponse> getWeatherByCity(
        @Parameter(description = "City name", example = "Moscow")
        @RequestParam(required = false) final String city,
        @Parameter(description = "Country code", example = "RU")
        @RequestParam(required = false) final String countryCode,
        @Parameter(description = "longitude")
        @RequestParam(required = false) final Double lon,
        @Parameter(description = "latitude")
        @RequestParam(required = false) final Double lat
    ) {
        return ResponseEntity.ok(historyService.getHistoryStatistic(city, countryCode, lon, lat));
    }

    @GetMapping("/history-by-type")
    @Operation(
        summary = "Requests history statistics by type",
        description = "Returns requested type and count of requests in history",
        security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Not authorized", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        )),
        @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        ))
    })
    public ResponseEntity<HistoryResponse> getWeatherByCity(
        @Parameter(description = "History type", example = "CITY")
        @RequestParam final HistoryType historyType
    ) {
        return ResponseEntity.ok(historyService.getHistoryStatisticByType(historyType));
    }

}
