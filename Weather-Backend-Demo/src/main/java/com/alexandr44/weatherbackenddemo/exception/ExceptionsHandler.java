package com.alexandr44.weatherbackenddemo.exception;

import com.alexandr44.weatherbackenddemo.dto.ErrorDto;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    private static final String FEIGN_NOT_FOUND_MESSAGE = "Could not found weather by your input data";
    private static final String FEIGN_ERROR_MESSAGE = "Could not receive weather";
    private static final String BAD_REQUEST_MESSAGE = "Bad request";
    private static final String ERROR_MESSAGE = "Internal error";

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorDto> handleFeignExceptionNotFound(
        final FeignException.NotFound exception,
        final HttpServletRequest request) {
        return logAndBuildResponse(
            exception,
            FEIGN_NOT_FOUND_MESSAGE,
            exception.status(),
            request.getRequestURI() + "?" + request.getQueryString()
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDto> handleFeignException(final FeignException exception,
                                                         final HttpServletRequest request) {
        return logAndBuildResponse(
            exception,
            FEIGN_ERROR_MESSAGE,
            exception.status(),
            request.getRequestURI() + "?" + request.getQueryString()
        );
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MissingServletRequestParameterException.class
    })
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
        final Exception exception,
        final HttpServletRequest request
    ) {
        return logAndBuildResponse(
            exception,
            BAD_REQUEST_MESSAGE,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI() + "?" + request.getQueryString()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(final Exception exception,
                                                    final HttpServletRequest request) {
        return logAndBuildResponse(
            exception,
            ERROR_MESSAGE,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI() + "?" + request.getQueryString()
        );
    }

    private ResponseEntity<ErrorDto> logAndBuildResponse(
        final Exception exception,
        final String description,
        final int status,
        final String queryStr
    ) {
        log.error("Got error while requesting {}, description {}, status {}", queryStr, description, status, exception);
        final ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(exception.getMessage());
        errorDto.setDescription(description);
        errorDto.setErrorCode(status);
        errorDto.setQuery(queryStr);
        return ResponseEntity.status(status).body(errorDto);
    }

}
