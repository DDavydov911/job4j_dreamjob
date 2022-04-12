CREATE TABLE users(
    id       SERIAL PRIMARY KEY,
    email    TEXT unique,
    password TEXT
);