package com.alexandr44.weatherbackenddemo.integration;

import com.alexandr44.weatherbackenddemo.BaseTest;
import com.alexandr44.weatherbackenddemo.repository.HistoryRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import lombok.Getter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Map;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Getter
public class BaseWireMockTest extends BaseTest {

    private static final Integer WIRE_MOCK_SERVER_PORT = 8080;
    private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(WIRE_MOCK_SERVER_PORT);

    @MockitoSpyBean
    protected HistoryRepository historyRepository;

    @BeforeAll
    public static void beforeAll() {
        WIRE_MOCK_SERVER.start();
        configureFor("localhost", WIRE_MOCK_SERVER_PORT);
    }

    @AfterAll
    public static void afterAll() {
        WIRE_MOCK_SERVER.stop();
    }

    @AfterEach
    public void cleanUp() {
        WIRE_MOCK_SERVER.resetAll();
        historyRepository.deleteAll();
    }

    public void stubGet(final String url,
                        final Map<String, String> rawParams,
                        final String responseBody,
                        final HttpStatus httpStatus) {
        final Map<String, StringValuePattern> params = rawParams.entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    e -> equalTo(e.getValue())
                )
            );
        stubFor(get(urlMatching(url + ".*$"))
            .withQueryParams(params)
            .willReturn(aResponse()
                .withStatus(httpStatus.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));
    }

}
