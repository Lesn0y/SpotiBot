CREATE SCHEMA IF NOT EXISTS spoti_bot;

CREATE TABLE IF NOT EXISTS spoti_bot.tokens
(
    username        VARCHAR(50) PRIMARY KEY,
    refresh_token   VARCHAR(255),
    access_token    VARCHAR(255),
    token_type      VARCHAR(10),
    scope           VARCHAR(255),
    expires         INT,
    expiration_time DATE
);