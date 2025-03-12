-- Conecte-se a um banco de dados diferente do que deseja dropar, por exemplo, o "postgres"
\c postgres

-- Agora, execute os comandos de DROP e CREATE
DROP DATABASE IF EXISTS db_auth;
CREATE DATABASE db_auth;

-- Para visualizar as tabelas do esquema "public" (no banco de dados atual)
\d public.tb_users

-- Se você precisar conectar-se a outro banco, por exemplo, "db_login"
\c db_login

-- E então realizar uma consulta
SELECT * FROM tb_users;
