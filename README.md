# Weather-Backend-Demo

A backend service demo project for fetching, processing, and serving weather data.  
Designed to illustrate API design, external HTTP integration, concurrency control, and modular architecture in Java.

---

## 🚀 Main Features

- **External API Integration** — fetch weather data from third-party services (e.g. OpenWeatherMap).
- **Concurrent Data Retrieval** — parallelize API requests to improve responsiveness.
- **Security and Authentication** — basic Authentication with user via `InMemoryUserDetailsManager`, endpoint protection.
- **Data Processing & Transformation** — convert, aggregate, filter, or enrich raw weather data.
- **Database and Migrations** — PostgreSQL with Flyway for migrations, demonstrates ORM and database schema management.
- **REST API Layer** — expose endpoints for clients to request weather forecasts, current conditions, etc.
- **Caching and Performance** — uses Caffeine for local caching, showcases query optimization for external APIs.
- **Error Handling & Resilience** — manage failures, retries, fallbacks, and logging.
- **Clear Modular Structure** — separation of concerns: controller, service, client, model, util layers.
- **API Documentation (Swagger)** — Springdoc OpenAPI generates Swagger UI (`/swagger-ui.html`) and OpenAPI JSON (`/api-docs`).
- **Testing** — integration tests with MockMvc and WireMock for mocking OpenWeatherMap API.

---

## 🧩 Tech Stack

| Layer / Concern        | Technology / Library                                            | Purpose / Role |
|-------------------------|-----------------------------------------------------------------|-----------------|
| **Language / Platform** | Java (e.g. Java 17+)                                            | Core backend and business logic |
| **Build / Dependency**  | Gradle (or Maven)                                               | Project building, dependency management |
| **HTTP Client**         | `FeignClient` | For outgoing HTTP requests to weather APIs |
| **Caching / Rate Limit**| Caffeine, Guava Cache, or simple in-memory cache                | To reduce repeated external requests |
| **REST API / Web**      | Spring Boot / Spring Web MVC / JAX-RS / Spark Java              | To expose HTTP endpoints |
| **JSON Processing**     | Jackson                                       | Serializing / deserializing JSON payloads |
| **Logging**             | SLF4J + Logback (or Log4j2)                                     | Request tracing, error reporting |
| **Testing**             | JUnit, Mockito, WireMock                                        | Unit tests, mocking external API responses |
| **Version Control**     | Git + GitHub                                                    | Repository hosting, versioning, collaboration |

---

# Getting Started

Set env variable WEATHER-API-KEY to your open weather api key
https://home.openweathermap.org/api_keys
and run project

### Swagger
swagger-ui/index.html

### Api-doc
/api-docs
