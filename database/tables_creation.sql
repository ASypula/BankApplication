-- Generated by Oracle SQL Developer Data Modeler 21.2.0.165.1515
--   at:        2021-11-23 20:05:00 CET
--   site:      Oracle Database 11g
--   type:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE account_types (
    account_type_id NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    name    VARCHAR2(15 BYTE) NOT NULL
);

ALTER TABLE account_types ADD CONSTRAINT account_types_pk PRIMARY KEY ( account_type_id );

CREATE TABLE addresses (
    address_id     NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    street         VARCHAR2(30 BYTE) NOT NULL,
    apartment_no   NUMBER(*, 0) NOT NULL,
    cities_city_id NUMBER(*, 0) NOT NULL
);

ALTER TABLE addresses ADD CONSTRAINT addresses_pk PRIMARY KEY ( address_id );

CREATE TABLE bank_accounts (
    bank_account_id            NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    balance               NUMBER(14, 2) NOT NULL,
    account_no            VARCHAR2(32 BYTE),
    start_date            DATE NOT NULL,
    end_date              DATE,
    interest_rate         NUMBER(5, 2),
    accum_period          NUMBER(*, 0),
    installment_size      NUMBER(14, 2),
    account_types_type_id NUMBER(*, 0) NOT NULL,
    clients_client_id     NUMBER(*, 0) NOT NULL
);

ALTER TABLE bank_accounts ADD CONSTRAINT bank_accounts_pk PRIMARY KEY ( bank_account_id );

CREATE TABLE bank_branches (
    bank_branch_id            NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    employees_no         NUMBER(*, 0) NOT NULL,
    addresses_address_id NUMBER(*, 0) NOT NULL,
    opening_hours_id     NUMBER(*, 0) NOT NULL
);

CREATE UNIQUE INDEX bank_branches__idx ON
    bank_branches (
        addresses_address_id
    ASC );

ALTER TABLE bank_branches ADD CONSTRAINT bank_branches_pk PRIMARY KEY ( bank_branch_id );

CREATE TABLE card_types (
    card_type_id   NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    type_name VARCHAR2(10 BYTE) NOT NULL
);

ALTER TABLE card_types ADD CONSTRAINT card_types_pk PRIMARY KEY ( card_type_id );

CREATE TABLE cards (
    card_id                  NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    expiration_date          DATE NOT NULL,
    hashed_ccv               CHAR(64 BYTE) NOT NULL,
    hashed_pin               CHAR(64 BYTE) NOT NULL,
    card_types_type_id       NUMBER(*, 0) NOT NULL,
    bank_accounts_account_id NUMBER(*, 0) NOT NULL
);

ALTER TABLE cards ADD CONSTRAINT cards_pk PRIMARY KEY ( card_id );

CREATE TABLE cities (
    city_id   NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    city_name VARCHAR2(30 BYTE) NOT NULL
);

ALTER TABLE cities ADD CONSTRAINT cities_pk PRIMARY KEY ( city_id );

CREATE TABLE clients (
    client_id             NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    personal_data_data_id NUMBER(*, 0) NOT NULL,
    employees_employee_id NUMBER(*, 0) NOT NULL
);

CREATE UNIQUE INDEX clients__idx ON
    clients (
        personal_data_data_id
    ASC );

ALTER TABLE clients ADD CONSTRAINT clients_pk PRIMARY KEY ( client_id );

CREATE TABLE employees (
    employee_id               NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    salary                    NUMBER(7, 2) NOT NULL,
    professions_profession_id NUMBER(*, 0) NOT NULL,
    personal_data_data_id     NUMBER(*, 0) NOT NULL
);

CREATE UNIQUE INDEX employees__idx ON
    employees (
        personal_data_data_id
    ASC );

ALTER TABLE employees ADD CONSTRAINT employees_pk PRIMARY KEY ( employee_id );

CREATE TABLE opening_hours (
    opening_hours_id        NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    mon_hours VARCHAR2(5 BYTE),
    tue_hours VARCHAR2(5 BYTE),
    wed_hours VARCHAR2(5 BYTE),
    thu_hours VARCHAR2(5 BYTE),
    fri_hours VARCHAR2(5 BYTE),
    sat_hours VARCHAR2(5 BYTE),
    sun_hours VARCHAR2(5 BYTE)
);

ALTER TABLE opening_hours ADD CONSTRAINT opening_hours_pk PRIMARY KEY ( opening_hours_id );

CREATE TABLE personal_data (
    personal_data_id     NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    name                 VARCHAR2(30 BYTE) NOT NULL,
    surname              VARCHAR2(30 BYTE) NOT NULL,
    pesel                VARCHAR2(11 BYTE) NOT NULL,
    phone_no             VARCHAR2(9 BYTE) NOT NULL,
    hashed_pswd          VARCHAR2(64 BYTE) NOT NULL,
    addresses_address_id NUMBER(*, 0) NOT NULL
);

ALTER TABLE personal_data ADD CONSTRAINT personal_data_pk PRIMARY KEY ( personal_data_id );

CREATE TABLE professions (
    profession_id NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    name          VARCHAR2(30 BYTE) NOT NULL,
    min_salary    NUMBER(7, 2) NOT NULL,
    max_salary    NUMBER(7, 2) NOT NULL
);

ALTER TABLE professions ADD CONSTRAINT professions_pk PRIMARY KEY ( profession_id );

CREATE TABLE transaction_history (
    transaction_id           NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    amount                   NUMBER(14, 2) NOT NULL,
    "Date"                   DATE NOT NULL,
    bank_accounts_account_id NUMBER(*, 0) NOT NULL,
    transaction_type_type_id NUMBER(*, 0) NOT NULL
);

ALTER TABLE transaction_history ADD CONSTRAINT transaction_history_pk PRIMARY KEY ( transaction_id );

CREATE TABLE transaction_type (
    transaction_type_id   NUMBER(*, 0) GENERATED BY DEFAULT AS IDENTITY,
    type_name VARCHAR2(15 BYTE) NOT NULL
);

ALTER TABLE transaction_type ADD CONSTRAINT transaction_type_pk PRIMARY KEY ( transaction_type_id );

ALTER TABLE addresses
    ADD CONSTRAINT addresses_cities_fk FOREIGN KEY ( cities_city_id )
        REFERENCES cities ( city_id );

ALTER TABLE bank_accounts
    ADD CONSTRAINT bank_accounts_account_types_fk FOREIGN KEY ( account_types_type_id )
        REFERENCES account_types ( account_type_id );

ALTER TABLE bank_accounts
    ADD CONSTRAINT bank_accounts_clients_fk FOREIGN KEY ( clients_client_id )
        REFERENCES clients ( client_id );

ALTER TABLE bank_branches
    ADD CONSTRAINT bank_branches_addresses_fk FOREIGN KEY ( addresses_address_id )
        REFERENCES addresses ( address_id );

ALTER TABLE bank_branches
    ADD CONSTRAINT bank_branches_opening_hours_fk FOREIGN KEY ( opening_hours_id )
        REFERENCES opening_hours ( opening_hours_id );

ALTER TABLE cards
    ADD CONSTRAINT cards_bank_accounts_fk FOREIGN KEY ( bank_accounts_account_id )
        REFERENCES bank_accounts ( bank_account_id )
            ON DELETE CASCADE;

ALTER TABLE cards
    ADD CONSTRAINT cards_card_types_fk FOREIGN KEY ( card_types_type_id )
        REFERENCES card_types ( card_type_id );

ALTER TABLE clients
    ADD CONSTRAINT clients_employees_fk FOREIGN KEY ( employees_employee_id )
        REFERENCES employees ( employee_id );

ALTER TABLE clients
    ADD CONSTRAINT clients_personal_data_fk FOREIGN KEY ( personal_data_data_id )
        REFERENCES personal_data ( personal_data_id );

ALTER TABLE employees
    ADD CONSTRAINT employees_personal_data_fk FOREIGN KEY ( personal_data_data_id )
        REFERENCES personal_data ( personal_data_id );

ALTER TABLE employees
    ADD CONSTRAINT employees_professions_fk FOREIGN KEY ( professions_profession_id )
        REFERENCES professions ( profession_id );

ALTER TABLE personal_data
    ADD CONSTRAINT personal_data_addresses_fk FOREIGN KEY ( addresses_address_id )
        REFERENCES addresses ( address_id );
 
ALTER TABLE transaction_history
    ADD CONSTRAINT trans_hist_bank_accounts_fk FOREIGN KEY ( bank_accounts_account_id )
        REFERENCES bank_accounts ( bank_account_id );

ALTER TABLE transaction_history
    ADD CONSTRAINT trans_hist_transaction_type_fk FOREIGN KEY ( transaction_type_type_id )
        REFERENCES transaction_type ( transaction_type_id )
            ON DELETE CASCADE;
