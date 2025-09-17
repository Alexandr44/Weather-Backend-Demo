package com.alexandr44.weatherbackenddemo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HistoryResponse {

    private String params;
    private int count;

}
