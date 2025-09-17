package com.alexandr44.weatherbackenddemo.dto;

import lombok.Data;

@Data
public class ErrorDto {

    private String message;
    private String description;
    private String query;
    private int errorCode;

}
