package com.alexandr44.weatherbackenddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class WeatherBackendDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherBackendDemoApplication.class, args);
    }

}
