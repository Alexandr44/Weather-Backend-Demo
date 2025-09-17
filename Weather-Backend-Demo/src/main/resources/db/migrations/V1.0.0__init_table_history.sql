CREATE TABLE weather.history_items
(
    id           BIGSERIAL PRIMARY KEY,
    type         VARCHAR(10)              NOT NULL,
    city         VARCHAR(100),
    country_code VARCHAR(50),
    lon          NUMERIC,
    lat          NUMERIC,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL
)