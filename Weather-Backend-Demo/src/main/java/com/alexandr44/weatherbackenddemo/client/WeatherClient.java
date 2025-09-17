package com.alexandr44.weatherbackenddemo.client;

import com.alexandr44.weatherbackenddemo.dto.OpenWeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "OpenWeatherClient", url = "${spring.feign.urls.weather-api}")
public interface WeatherClient {

    @GetMapping("/weather")
    ResponseEntity<OpenWeatherResponse> getWeatherByCity(
        @RequestParam("q") String query,
        @RequestParam("appid") String openWeatherMapKey,
        @RequestParam("units") String units,
        @RequestParam("lang") String lang
    );

}
