CREATE DATABASE inside;

CREATE TABLE IF NOT EXISTS accounts
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    token_id BIGINT references access_tokens (id)
);

CREATE TABLE IF NOT EXISTS access_tokens
(
    id           BIGSERIAL PRIMARY KEY,
    account_id   BIGINT NOT NULL UNIQUE REFERENCES accounts (id),
    access_token VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS messages
(
    id         BIGSERIAL PRIMARY KEY,
    message    varchar(255) NOT NULL,
    timestamp  BIGINT       NOT NULL,
    account_id BIGINT references accounts (id)
);