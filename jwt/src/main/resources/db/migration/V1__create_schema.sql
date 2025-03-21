-- Desconecta todos os clientes conectados ao banco (necessário para conseguir dropar)
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'db_auth'
  AND pid <> pg_backend_pid();

-- Remove o banco de dados (vai apagar tudo)
DROP DATABASE IF EXISTS db_auth;

-- Cria o banco de dados novamente
CREATE DATABASE db_auth;

-- Garante que o usuário "postgres" tenha acesso ao banco
GRANT ALL PRIVILEGES ON DATABASE db_auth TO postgres;


DROP TABLE IF EXISTS tb_users;
DROP TABLE IF EXISTS tb_refresh_tokens;


CREATE TABLE IF NOT EXISTS tb_users
(
    id       VARCHAR(36),
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL CHECK (role IN ('USER', 'ADMIN', 'EMPLOYEE', 'MERCHANT', 'COMPANY')),

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tb_refresh_tokens
(
    id       VARCHAR(36),
    token    VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);