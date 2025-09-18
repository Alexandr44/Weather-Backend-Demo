# Weather-Backend-Demo Project Description

**Weather-Backend-Demo** is a demo application built with Spring Boot, designed as a portfolio showcase. 
It provides a simple REST API to retrieve weather information for a given city by integrating with the 
OpenWeatherMap external service. The project demonstrates modern backend development practices, 
including microservice architecture, HTTP client integration via Feign, data caching, database migrations, 
authentication, and API documentation. Developed using Java 17+ and Spring Boot 3.5.5, 
it is tailored for a test environment but easily extensible for production use.

## Key Technologies and Dependencies
The project leverages a robust stack of technologies within the Spring ecosystem, 
showcasing a full-cycle backend development process:
- **Spring Boot 3.5.5**: Core framework for building the REST API, managing dependencies, and 
auto-configuration. Demonstrates Spring Web MVC for handling HTTP requests and Spring Security for 
securing endpoints.
- **Spring Cloud OpenFeign**: Used to create a declarative HTTP client (`WeatherClient`) for integrating 
with the OpenWeatherMap API. Shows seamless interaction with external services without manual HTTP client 
management.
- **Spring Data JPA and PostgreSQL**: For interacting with a relational database. Uses a 
repository (`HistoryRepository`) to log requests, demonstrating ORM-based database operations.
- **Flyway**: For managing database migrations (e.g., creating `weather_data` or `history` tables). 
Showcases automated schema management.
- **Spring Data Redis and Caffeine**: For caching weather query results (`@Cacheable` in `WeatherService` 
with key `#city + #countryCode`). Demonstrates performance optimization using local caching (Caffeine).
- **Spring Security**: Implements Basic Authentication with user via `InMemoryUserDetailsManager`. 
Secures endpoints (e.g., `/api/v1/weather-by-city` requires authentication), handles authentication errors.
- **Springdoc OpenAPI**: For generating API documentation (Swagger UI at `/swagger-ui.html`). Uses OpenAPI 3 
annotations (`@Tag`, `@Operation`, `@ApiResponse`, `@SecurityRequirement`) to describe endpoints, 
including custom error response schemas (`ErrorDto` for 400, 401, 500).
- **Lombok**: Reduces boilerplate code (e.g., `@Data`, `@RequiredArgsConstructor` in DTOs and services).
- **WireMock**: For mocking external APIs in tests (e.g., stubbing OpenWeatherMap responses).
- **Others**: Jackson for JSON serialization, Testcontainers for integration tests with PostgreSQL, 
JUnit 5 and Mockito for unit tests, H2 DB for testing.

The project uses Gradle for build and dependency management.

## Key Features and Demonstrations
The project showcases a comprehensive backend development workflow, from external service integration to 
error handling and API documentation. Key components include:

1. **REST API and External Service Integration**:
    - Endpoint `/api/v1/weather-by-city?city=Moscow&countryCode=RU` retrieves weather data via 
`WeatherClient` (Feign client) from the OpenWeatherMap API.
    - Demonstrates declarative HTTP clients and query parameter handling.

2. **Security and Authentication**:
    - Basic Authentication with user via `InMemoryUserDetailsManager`.
    - Endpoint protection: `/api/v1/weather-by-city` requires authentication.
    - Handles authentication errors (401) via `@ControllerAdvice`, returning `ErrorDto`.

3. **Error Handling and Global Exception Handler**:
    - `@ControllerAdvice` intercepts exceptions, returning `ErrorDto`.
    - Customizes Swagger responses with `@ApiResponse(content = @Content(schema = @Schema(implementation = ErrorDto.class)))` 
for 400, 401, 500 statuses.

4. **Database and Migrations**:
    - PostgreSQL with Flyway for migrations.
    - Spring Data JPA with repositories (`HistoryRepository`) for request logging.
    - Demonstrates ORM and database schema management.

5. **Caching and Performance**:
    - Spring Cache with `@Cacheable` in `WeatherService`.
    - Uses Caffeine for local caching.
    - Showcases query optimization for external APIs.

6. **API Documentation (Swagger)**:
    - Springdoc OpenAPI generates Swagger UI (`/swagger-ui.html`) and OpenAPI JSON (`/api-docs`).
    - Uses OpenAPI 3 annotations (`@Tag`, `@Operation`, `@ApiResponse`, `@SecurityRequirement`) 
for endpoint and schema documentation (`WeatherDto`, `ErrorDto`).

7. **Testing**:
    - Unit tests with JUnit 5, Mockito.
    - Integration tests with MockMvc and WireMock for mocking OpenWeatherMap API.
    - Demonstrates testing with authentication without hardcoded credentials.


# Getting Started

Set env variable WEATHER-API-KEY to your open weather api key
https://home.openweathermap.org/api_keys
and run project

### Swagger
swagger-ui/index.html

### Api-doc
/api-docs
