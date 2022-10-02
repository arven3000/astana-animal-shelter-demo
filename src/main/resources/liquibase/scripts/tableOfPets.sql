-- liquibase formatted sql

-- changeSet nakremneva:1
CREATE TABLE IF NOT EXISTS pets
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(25) DEFAULT 'Unknown',
    type_of_animal VARCHAR(25) NOT NULL,
    age            INT,
    users_id BIGINT references users(id) on delete set null
);