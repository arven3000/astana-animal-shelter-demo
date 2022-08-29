--liquibase formatted sql

--changeset nsafarov:1

CREATE TABLE IF NOT EXISTS users
(
    id           SERIAL PRIMARY KEY,
    chatId       BIGINT NOT NULL,
    userName     TEXT   NOT NULL,
    phoneNumber  TEXT   NOT NULL,
    emailAddress TEXT   NOT NULL
);