package com.alexandr44.weatherbackenddemo.service;

import com.alexandr44.weatherbackenddemo.entity.HistoryItem;
import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import com.alexandr44.weatherbackenddemo.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private void saveHistoryItem(final HistoryItem historyItem) {
        historyRepository.save(historyItem);
    }

}
