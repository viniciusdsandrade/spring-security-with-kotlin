-- Conecte-se a um banco de dados diferente do que deseja dropar, por exemplo, o "postgres"
\c postgres

-- Agora, execute os comandos de DROP e CREATE
DROP DATABASE IF EXISTS db_auth;
CREATE DATABASE db_auth;

-- Conecte-se ao banco de dados "db_auth"
\c db_auth

-- Seleciona o email e o role de todos os usuários
SELECT email "Email principal", role "Cargo"
FROM tb_users;

-- Consulta para listar todas as tabelas de um banco de dados
SELECT table_schema, table_name
FROM information_schema.tables
WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
  AND table_type = 'BASE TABLE'
ORDER BY table_schema, table_name;