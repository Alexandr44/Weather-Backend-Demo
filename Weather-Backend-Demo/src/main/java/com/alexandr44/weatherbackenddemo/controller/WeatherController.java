package com.alexandr44.weatherbackenddemo.controller;

import com.alexandr44.weatherbackenddemo.dto.ErrorDto;
import com.alexandr44.weatherbackenddemo.dto.WeatherDto;
import com.alexandr44.weatherbackenddemo.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Weather API", description = "API for weather requests")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather-by-city")
    @Operation(
        summary = "Requests weather by city and country code",
        description = "Returns weather data for selected city and country",
        security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Not authorized", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        )),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        )),
        @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        ))
    })
    public ResponseEntity<WeatherDto> getWeatherByCity(
        @Parameter(description = "City name", example = "Moscow")
        @RequestParam @NotBlank @Size(min = 2, max = 100) final String city,
        @Parameter(description = "Country code", example = "RU")
        @RequestParam(required = false) final String countryCode
    ) {
        return ResponseEntity.ok(weatherService.getWeatherByCity(city, countryCode));
    }

    @GetMapping("/weather-by-coord")
    @Operation(
        summary = "Requests weather by longitude and latitude",
        description = "Returns weather data for coordinates",
        security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Not authorized", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        )),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        )),
        @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorDto.class)
        ))
    })
    public ResponseEntity<WeatherDto> getWeatherByCoordinates(
        @Parameter(description = "longitude") @RequestParam Double lon,
        @Parameter(description = "latitude") @RequestParam Double lat
    ) {
        return ResponseEntity.ok(weatherService.getWeatherByCoordinates(lon, lat));
    }

}
