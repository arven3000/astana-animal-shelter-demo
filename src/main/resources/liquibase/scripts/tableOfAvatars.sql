-- liquibase formatted sql

-- changeSet sli:2
CREATE TABLE IF NOT EXISTS avatars
(
    id BIGSERIAL PRIMARY KEY,
    photo OID,
    media_type VARCHAR,
    file_path VARCHAR,
    file_size BIGINT,
    pet_id BIGINT,
    report_id BIGINT
)