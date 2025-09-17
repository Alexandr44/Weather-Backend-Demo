package com.alexandr44.weatherbackenddemo.mapper;

import com.alexandr44.weatherbackenddemo.dto.OpenWeatherResponse;
import com.alexandr44.weatherbackenddemo.dto.WeatherDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(target = "city", source = "name")
    @Mapping(target = "countryCode", source = "sys.country")
    @Mapping(target = "longitude", source = "coord.lon")
    @Mapping(target = "latitude", source = "coord.lat")
    @Mapping(target = "timestamp", source = "openWeatherResponse", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "weather.main", source = "weather", qualifiedByName = "getWeatherMain")
    @Mapping(target = "weather.description", source = "weather", qualifiedByName = "getWeatherDescription")
    @Mapping(target = "weather.temperature", source = "main.temp")
    @Mapping(target = "weather.temperatureFeelsLike", source = "main.feelsLike")
    @Mapping(target = "weather.temperatureMin", source = "main.tempMin")
    @Mapping(target = "weather.temperatureMax", source = "main.tempMax")
    @Mapping(target = "weather.pressure", source = "main.pressure")
    @Mapping(target = "weather.humidity", source = "main.humidity")
    @Mapping(target = "weather.windSpeed", source = "wind.speed")
    @Mapping(target = "weather.clouds", source = "clouds.all")
    WeatherDto map(OpenWeatherResponse openWeatherResponse);

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(final OpenWeatherResponse weatherResponse) {
        return weatherResponse.getDt() != null && weatherResponse.getTimezone() != null
            ? LocalDateTime.ofEpochSecond(weatherResponse.getDt(), 0, ZoneOffset.ofTotalSeconds(weatherResponse.getTimezone()))
            : null;
    }

    @Named("getWeatherMain")
    default String getWeatherMain(final List<OpenWeatherResponse.Weather> weather) {
        if (weather == null || weather.isEmpty()) {
            return null;
        } else {
            return weather.getFirst().getMain();
        }
    }

    @Named("getWeatherDescription")
    default String getWeatherDescription(final List<OpenWeatherResponse.Weather> weather) {
        if (weather == null || weather.isEmpty()) {
            return null;
        } else {
            return weather.getFirst().getDescription();
        }
    }

}
