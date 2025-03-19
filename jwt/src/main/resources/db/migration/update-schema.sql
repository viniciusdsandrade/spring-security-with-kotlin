CREATE TABLE tb_accounts
(
    user_id           UUID         NOT NULL,
    email             VARCHAR(255) NOT NULL,
    registration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name              VARCHAR(255) NOT NULL,
    company_id        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_tb_accounts PRIMARY KEY (user_id)
);

CREATE TABLE tb_addresses
(
    id          UUID NOT NULL,
    cep         VARCHAR(255),
    logradouro  VARCHAR(255),
    numero      VARCHAR(255),
    complemento VARCHAR(255),
    unidade     VARCHAR(255),
    bairro      VARCHAR(255),
    localidade  VARCHAR(255),
    uf          VARCHAR(255),
    estado      VARCHAR(255),
    regiao      VARCHAR(255),
    ibge        VARCHAR(255),
    gia         VARCHAR(255),
    ddd         VARCHAR(255),
    siafi       VARCHAR(255),
    CONSTRAINT pk_tb_addresses PRIMARY KEY (id)
);

CREATE TABLE tb_companies
(
    id              UUID NOT NULL,
    name            VARCHAR(255),
    email           VARCHAR(255),
    phone           VARCHAR(255),
    cnpj            VARCHAR(255),
    additional_info VARCHAR(255),
    user_id         UUID,
    cep             VARCHAR(255),
    logradouro      VARCHAR(255),
    numero          VARCHAR(255),
    complemento     VARCHAR(255),
    unidade         VARCHAR(255),
    bairro          VARCHAR(255),
    localidade      VARCHAR(255),
    uf              VARCHAR(255),
    estado          VARCHAR(255),
    regiao          VARCHAR(255),
    ibge            VARCHAR(255),
    gia             VARCHAR(255),
    ddd             VARCHAR(255),
    siafi           VARCHAR(255),
    CONSTRAINT pk_tb_companies PRIMARY KEY (id)
);

CREATE TABLE tb_employees
(
    id              UUID NOT NULL,
    name            VARCHAR(255),
    email           VARCHAR(255),
    phone           VARCHAR(255),
    cpf             VARCHAR(255),
    additional_info VARCHAR(255),
    role            VARCHAR(255),
    user_id         UUID,
    cep             VARCHAR(255),
    logradouro      VARCHAR(255),
    numero          VARCHAR(255),
    complemento     VARCHAR(255),
    unidade         VARCHAR(255),
    bairro          VARCHAR(255),
    localidade      VARCHAR(255),
    uf              VARCHAR(255),
    estado          VARCHAR(255),
    regiao          VARCHAR(255),
    ibge            VARCHAR(255),
    gia             VARCHAR(255),
    ddd             VARCHAR(255),
    siafi           VARCHAR(255),
    company_id      UUID,
    CONSTRAINT pk_tb_employees PRIMARY KEY (id)
);

CREATE TABLE tb_merchants
(
    merchant_id      UUID         NOT NULL,
    name             VARCHAR(255) NOT NULL,
    merchant_score   DOUBLE PRECISION,
    created_at       TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    main_category    VARCHAR(255),
    price_range      VARCHAR(255),
    address_district VARCHAR(255),
    address_latitude DOUBLE PRECISION,
    CONSTRAINT pk_tb_merchants PRIMARY KEY (merchant_id)
);

CREATE TABLE tb_orders
(
    order_id             UUID             NOT NULL,
    dt                   VARCHAR(255)     NOT NULL,
    order_creation_shift VARCHAR(255)     NOT NULL,
    merchant_id          UUID             NOT NULL,
    customer_id          UUID             NOT NULL,
    current_status       VARCHAR(255)     NOT NULL,
    review_score         DOUBLE PRECISION,
    gross_value          DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_tb_orders PRIMARY KEY (order_id)
);

CREATE TABLE tb_refresh_tokens
(
    token    VARCHAR(255) NOT NULL,
    username VARCHAR(255),
    CONSTRAINT pk_tb_refresh_tokens PRIMARY KEY (token)
);

CREATE TABLE tb_users
(
    id       UUID         NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_tb_users PRIMARY KEY (id)
);

ALTER TABLE tb_addresses
    ADD CONSTRAINT uc_6cb0d6a00f39a01ea7f0b1024 UNIQUE (cep, numero);

ALTER TABLE tb_employees
    ADD CONSTRAINT uc_7fa9f2cc7b7d737f181538f6a UNIQUE (cpf);

ALTER TABLE tb_users
    ADD CONSTRAINT uc_81f59ce6e4df568670ca73af3 UNIQUE (email);

ALTER TABLE tb_employees
    ADD CONSTRAINT uc_88f07b3109b6f93933c6105ec UNIQUE (email);

ALTER TABLE tb_companies
    ADD CONSTRAINT uc_9dd4781f4c461ccddbf97f533 UNIQUE (cnpj);

ALTER TABLE tb_accounts
    ADD CONSTRAINT uc_be83da8228fe75524b2588669 UNIQUE (email);

ALTER TABLE tb_companies
    ADD CONSTRAINT uc_tb_companies_user UNIQUE (user_id);

ALTER TABLE tb_employees
    ADD CONSTRAINT uc_tb_employees_user UNIQUE (user_id);

ALTER TABLE tb_companies
    ADD CONSTRAINT FK_TB_COMPANIES_ON_USER FOREIGN KEY (user_id) REFERENCES tb_users (id);

ALTER TABLE tb_employees
    ADD CONSTRAINT FK_TB_EMPLOYEES_ON_COMPANY FOREIGN KEY (company_id) REFERENCES tb_companies (id);

ALTER TABLE tb_employees
    ADD CONSTRAINT FK_TB_EMPLOYEES_ON_USER FOREIGN KEY (user_id) REFERENCES tb_users (id);

ALTER TABLE tb_orders
    ADD CONSTRAINT FK_TB_ORDERS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES tb_accounts (user_id);

ALTER TABLE tb_orders
    ADD CONSTRAINT FK_TB_ORDERS_ON_MERCHANT FOREIGN KEY (merchant_id) REFERENCES tb_merchants (merchant_id);