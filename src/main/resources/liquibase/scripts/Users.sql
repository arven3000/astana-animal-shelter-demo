--liquibase formatted sql

--changeset nsafarov:1

CREATE TABLE IF NOT EXISTS users
(
    id           SERIAL PRIMARY KEY,
    chat_Id       BIGINT NOT NULL,
    user_Name     TEXT   NOT NULL,
    phone_Number  TEXT,
    email_Address TEXT
);
