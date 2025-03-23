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

-- (Opcional) Lista as tabelas existentes
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public';

-- Remove as tabelas (se existirem) para recriação
DROP TABLE IF EXISTS tb_employees;
DROP TABLE IF EXISTS tb_companies;
DROP TABLE IF EXISTS tb_addresses;
DROP TABLE IF EXISTS tb_refresh_tokens;
DROP TABLE IF EXISTS tb_users;

-------------------------------------------------
-- Criação da tabela de Usuários (tb_users)
-------------------------------------------------
CREATE TABLE IF NOT EXISTS tb_users
(
    id       VARCHAR(36) PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL CHECK (role IN ('USER', 'ADMIN', 'EMPLOYEE', 'MERCHANT', 'COMPANY'))
);

-------------------------------------------------
-- Criação da tabela de Refresh Tokens (tb_refresh_tokens)
-------------------------------------------------
CREATE TABLE IF NOT EXISTS tb_refresh_tokens
(
    id       VARCHAR(36) PRIMARY KEY,
    token    VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);

-------------------------------------------------
-- Criação da tabela de Endereços (tb_addresses)
-------------------------------------------------
CREATE TABLE IF NOT EXISTS tb_addresses
(
    id          VARCHAR(36) PRIMARY KEY,
    cep         VARCHAR(20)  NOT NULL,
    logradouro  VARCHAR(255),
    numero      VARCHAR(50)  NOT NULL,
    complemento VARCHAR(255),
    unidade     VARCHAR(100),
    bairro      VARCHAR(100) NOT NULL,
    localidade  VARCHAR(100) NOT NULL,
    uf          VARCHAR(10)  NOT NULL,
    estado      VARCHAR(100),
    regiao      VARCHAR(100),
    ibge        VARCHAR(50),
    gia         VARCHAR(50),
    ddd         VARCHAR(10),
    siafi       VARCHAR(50),
    CONSTRAINT uq_addresses_cep_numero UNIQUE (cep, numero)
);

-------------------------------------------------
-- Criação da tabela de Empresas (tb_companies)
-------------------------------------------------
CREATE TABLE IF NOT EXISTS tb_companies
(
    id              VARCHAR(36) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    phone           VARCHAR(20)  NOT NULL,
    cnpj            VARCHAR(50)  NOT NULL,
    additional_info VARCHAR(255),
    role            VARCHAR(50)  NOT NULL CHECK (role = 'COMPANY'),
    -- Campos de endereço embutidos
    cep             VARCHAR(20),
    logradouro      VARCHAR(255),
    numero          VARCHAR(50),
    complemento     VARCHAR(255),
    unidade         VARCHAR(100),
    bairro          VARCHAR(100),
    localidade      VARCHAR(100),
    uf              VARCHAR(10),
    estado          VARCHAR(100),
    regiao          VARCHAR(100),
    ibge            VARCHAR(50),
    gia             VARCHAR(50),
    ddd             VARCHAR(10),
    siafi           VARCHAR(50),
    -- Associação com o usuário (apenas user_id)
    user_id         VARCHAR(36)  NOT NULL,
    CONSTRAINT fk_company_user FOREIGN KEY (user_id) REFERENCES tb_users(id)
);

-------------------------------------------------
-- Criação da tabela de Funcionários (tb_employees)
-------------------------------------------------
CREATE TABLE IF NOT EXISTS tb_employees
(
    id              VARCHAR(36) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    phone           VARCHAR(20)  NOT NULL,
    cpf             VARCHAR(20)  NOT NULL,
    additional_info VARCHAR(255),
    -- Campos de endereço embutidos
    cep             VARCHAR(20),
    logradouro      VARCHAR(255),
    numero          VARCHAR(50),
    complemento     VARCHAR(255),
    unidade         VARCHAR(100),
    bairro          VARCHAR(100),
    localidade      VARCHAR(100),
    uf              VARCHAR(10),
    estado          VARCHAR(100),
    regiao          VARCHAR(100),
    ibge            VARCHAR(50),
    gia             VARCHAR(50),
    ddd             VARCHAR(10),
    siafi           VARCHAR(50),
    role            VARCHAR(50)  NOT NULL CHECK (role = 'EMPLOYEE'),
    -- Associação com o usuário (apenas user_id)
    user_id         VARCHAR(36)  NOT NULL,
    CONSTRAINT fk_employee_user FOREIGN KEY (user_id) REFERENCES tb_users(id)
);

SELECT * FROM tb_users;
SELECT * FROM tb_refresh_tokens;
SELECT * FROM tb_companies;
SELECT * FROM tb_addresses;



TRUNCATE TABLE tb_employees, tb_companies, tb_addresses, tb_refresh_tokens, tb_users RESTART IDENTITY CASCADE;
