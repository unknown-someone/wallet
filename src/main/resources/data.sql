DROP TABLE IF EXISTS wallet;

CREATE TABLE wallet (
    id bigint PRIMARY KEY,
    balance numeric(10,2) NOT NULL
);

INSERT INTO wallet (id, balance) VALUES
(1, 100),
(2, 999.99);