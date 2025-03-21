CREATE TABLE IF NOT EXISTS tb_users
(
    id       VARCHAR(36),
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL CHECK (role IN ('USER', 'ADMIN', 'EMPLOYEE', 'MERCHANT', 'COMPANY')),

    PRIMARY KEY (id)
);