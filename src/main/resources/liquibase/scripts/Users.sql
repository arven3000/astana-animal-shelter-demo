--liquibase formatted sql

--changeset nsafarov:1

CREATE TABLE IF NOT EXISTS users
(
    id           SERIAL PRIMARY KEY,

    chat_id       BIGINT NOT NULL,
    user_name     VARCHAR NOT NULL,
    phone_number  VARCHAR   ,
    email_address VARCHAR   ,
    pet_id BIGINT ,
    data_time_of_pet TIMESTAMP,
    role VARCHAR
);
