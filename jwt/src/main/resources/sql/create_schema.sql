CREATE DATABASE inside;

CREATE TABLE IF NOT EXISTS account
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    token_id BIGINT references access_tokens (id)
);

CREATE TABLE IF NOT EXISTS access_tokens
(
    id           BIGSERIAL PRIMARY KEY,
    account_id   BIGINT NOT NULL UNIQUE REFERENCES account (id),
    access_token VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS message
(
    id         BIGSERIAL PRIMARY KEY,
    message    varchar(255) NOT NULL,
    timestamp  BIGINT       NOT NULL,
    account_id BIGINT references account (id)
);