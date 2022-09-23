-- liquibase formatted sql

-- changeSet sLi:3
CREATE TABLE IF NOT EXISTS reports
(
    id BIGSERIAL PRIMARY KEY,
    users_id BIGINT,
    pet_id BIGINT,
    diet TEXT,
    state_of_health TEXT,
    habits TEXT,
    data_time TIMESTAMP
)