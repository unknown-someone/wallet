DROP TABLE IF EXISTS wallet;

CREATE TABLE wallet (
    id bigint PRIMARY KEY,
    balance numeric(10,2) NOT NULL,
    password varchar(255) NOT NULL
);

INSERT INTO wallet (id, balance, password) VALUES
(1, 100, 'pass'),
(2, 999.99, 'pass');