package com.alexandr44.weatherbackenddemo.repository;

import com.alexandr44.weatherbackenddemo.entity.HistoryItem;
import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryItem, Long> {

    List<HistoryItem> findAllByCityAndCountryCodeAndLonAndLat(
        final String city,
        final String countryCode,
        final Double lon,
        final Double lat
    );

    int countAllByType(final HistoryType historyType);

}
