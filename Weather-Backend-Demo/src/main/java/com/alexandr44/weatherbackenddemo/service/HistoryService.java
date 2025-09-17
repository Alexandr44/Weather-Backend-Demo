package com.alexandr44.weatherbackenddemo.service;

import com.alexandr44.weatherbackenddemo.dto.HistoryResponse;
import com.alexandr44.weatherbackenddemo.entity.HistoryItem;
import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import com.alexandr44.weatherbackenddemo.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Transactional
    public void addHistoryCity(final String city, final String countryCode) {
        saveHistoryItem(
            new HistoryItem()
                .setType(HistoryType.CITY)
                .setCity(city)
                .setCountryCode(countryCode)
        );
    }

    @Transactional
    public void addHistoryCoord(final Double lon, final Double lat) {
        saveHistoryItem(
            new HistoryItem()
                .setType(HistoryType.COORDINATES)
                .setLon(lon)
                .setLat(lat)
        );
    }

    public HistoryResponse getHistoryStatistic(
        final String city,
        final String countryCode,
        final Double lon,
        final Double lat
    ) {
        final List<HistoryItem> historyItems = historyRepository.findAllByCityAndCountryCodeAndLonAndLat(
            city, countryCode, lon, lat
        );

        return new HistoryResponse()
            .setParams(buildParamsString(city, countryCode, lon, lat))
            .setCount(historyItems.size());
    }

    public HistoryResponse getHistoryStatisticByType(final HistoryType type) {
        return new HistoryResponse()
            .setParams("type=" + type.name())
            .setCount(historyRepository.countAllByType(type));
    }

    private void saveHistoryItem(final HistoryItem historyItem) {
        historyRepository.save(historyItem);
    }

    private String buildParamsString(
        final String city,
        final String countryCode,
        final Double lon,
        final Double lat
    ) {
        final StringBuilder params = new StringBuilder();
        if (city != null) {
            params.append("city=").append(city);
        }
        if (countryCode != null) {
            if (!params.isEmpty()) {
                params.append("&");
            }
            params.append("countryCode=").append(countryCode);
        }
        if (lon != null) {
            if (!params.isEmpty()) {
                params.append("&");
            }
            params.append("lon=").append(lon);
        }
        if (lat != null) {
            if (!params.isEmpty()) {
                params.append("&");
            }
            params.append("lat=").append(lat);
        }
        return params.toString();
    }

}
