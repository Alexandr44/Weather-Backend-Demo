package com.alexandr44.weatherbackenddemo.entity;

import com.alexandr44.weatherbackenddemo.enums.HistoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "history_items", schema = "weather")
public class HistoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private HistoryType type;

    @Column(name = "city")
    private String city;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "lat")
    private Double lat;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

}
