CREATE DATABASE inside;

CREATE TABLE IF NOT EXISTS account
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(128) UNIQUE,
    password VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS access_tokens
(
    id           BIGSERIAL PRIMARY KEY,
    account_id   BIGINT NOT NULL UNIQUE REFERENCES account (id),
    access_token VARCHAR(256)
);