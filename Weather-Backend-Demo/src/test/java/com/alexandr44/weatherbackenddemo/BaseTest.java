package com.alexandr44.weatherbackenddemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public abstract class BaseTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @SneakyThrows
    protected String readFromFile(final String path) {
        try (InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream(path)) {
            return IOUtils.toString(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
        }
    }

}
