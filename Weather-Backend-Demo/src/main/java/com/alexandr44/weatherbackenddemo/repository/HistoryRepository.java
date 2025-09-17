package com.alexandr44.weatherbackenddemo.repository;

import com.alexandr44.weatherbackenddemo.entity.HistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryItem, Long> {

}
