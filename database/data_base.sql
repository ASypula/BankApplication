-- Generated by Oracle SQL Developer Data Modeler 21.2.0.165.1515
--   at:        2021-11-10 23:42:05 CET
--   site:      Oracle Database 11g
--   type:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE account_types (
    type_id INTEGER NOT NULL,
    name    VARCHAR2(15) NOT NULL
);

ALTER TABLE account_types ADD CONSTRAINT account_types_pk PRIMARY KEY ( type_id );

CREATE TABLE addresses (
    address_id              INTEGER NOT NULL,
    street                  VARCHAR2(30) NOT NULL,
    apartment_no            INTEGER NOT NULL,
    bank_branches_f_id 	    INTEGER NOT NULL,
    cities_f_id          INTEGER NOT NULL
);

CREATE UNIQUE INDEX addresses__idx ON
    addresses (
        bank_branches_f_id
    ASC );

ALTER TABLE addresses ADD CONSTRAINT addresses_pk PRIMARY KEY ( address_id );

CREATE TABLE bank_accounts (
    account_id            INTEGER NOT NULL,
    balance               NUMBER(14, 2) NOT NULL,
    account_no            VARCHAR2(32),
    start_date            DATE NOT NULL,
    end_date              DATE,
    interest_rate         NUMBER(5, 2),
    accum_period          INTEGER,
    installment_size      NUMBER(14, 2),
    account_types_f_id INTEGER NOT NULL,
    clients_f_id     INTEGER NOT NULL
);

ALTER TABLE bank_accounts ADD CONSTRAINT bank_accounts_pk PRIMARY KEY ( account_id );

CREATE TABLE bank_branches (
    branch_id            INTEGER NOT NULL,
    employees_no         INTEGER NOT NULL,
    addresses_f_id INTEGER NOT NULL,
    opening_hours_id     INTEGER NOT NULL
);

CREATE UNIQUE INDEX bank_branches__idx ON
    bank_branches (
        addresses_f_id
    ASC );

ALTER TABLE bank_branches ADD CONSTRAINT bank_branches_pk PRIMARY KEY ( branch_id );

CREATE TABLE card_types (
    type_id   INTEGER NOT NULL,
    type_name VARCHAR2(10) NOT NULL
);

ALTER TABLE card_types ADD CONSTRAINT card_types_pk PRIMARY KEY ( type_id );

CREATE TABLE cards (
    card_id                  INTEGER NOT NULL,
    expiration_date          DATE NOT NULL,
    hashed_ccv               CHAR(64) NOT NULL,
    hashed_pin               CHAR(64) NOT NULL,
    card_types_f_id       INTEGER NOT NULL,
    bank_accounts_f_id INTEGER NOT NULL
);

ALTER TABLE cards ADD CONSTRAINT cards_pk PRIMARY KEY ( card_id );

CREATE TABLE cities (
    city_id   INTEGER NOT NULL,
    city_name VARCHAR2(30) NOT NULL
);

ALTER TABLE cities ADD CONSTRAINT cities_pk PRIMARY KEY ( city_id );

CREATE TABLE clients (
    client_id             INTEGER NOT NULL,
    personal_data_f_id INTEGER NOT NULL,
    employees_f_id INTEGER NOT NULL
);

ALTER TABLE clients ADD CONSTRAINT clients_pk PRIMARY KEY ( client_id );

CREATE TABLE employees (
    employee_id               INTEGER NOT NULL,
    salary                    NUMBER(7, 2) NOT NULL,
    professions_f_id INTEGER NOT NULL,
    personal_data_f_id     INTEGER NOT NULL
);

ALTER TABLE employees ADD CONSTRAINT employees_pk PRIMARY KEY ( employee_id );

CREATE TABLE opening_hours (
    id        INTEGER NOT NULL,
    monday    INTEGER NOT NULL,
    mon_hours VARCHAR2(5),
    tuesday   INTEGER NOT NULL,
    tue_hours VARCHAR2(5),
    wednesday INTEGER NOT NULL,
    wed_hours VARCHAR2(5),
    thursday  INTEGER NOT NULL,
    thu_hours VARCHAR2(5),
    friday    INTEGER NOT NULL,
    fri_hours VARCHAR2(5),
    saturday  INTEGER NOT NULL,
    sat_hours VARCHAR2(5),
    sunday    INTEGER NOT NULL,
    sun_hours VARCHAR2(5)
);

ALTER TABLE opening_hours ADD CONSTRAINT opening_hours_pk PRIMARY KEY ( id );

CREATE TABLE personal_data (
    data_id              INTEGER NOT NULL,
    name                 VARCHAR2(30) NOT NULL,
    surname              VARCHAR2(30) NOT NULL,
    pesel                VARCHAR2(11) NOT NULL,
    phone_no             VARCHAR2(9) NOT NULL,
    hashed_pswd          VARCHAR2(64) NOT NULL,
    addresses_f_id INTEGER NOT NULL
);

ALTER TABLE personal_data ADD CONSTRAINT personal_data_pk PRIMARY KEY ( data_id );

CREATE TABLE professions (
    profession_id INTEGER NOT NULL,
    name          VARCHAR2(30) NOT NULL,
    min_salary    NUMBER(7, 2) NOT NULL,
    max_salary    NUMBER(7, 2) NOT NULL
);

ALTER TABLE professions ADD CONSTRAINT professions_pk PRIMARY KEY ( profession_id );

CREATE TABLE transaction_history (
    transaction_id           INTEGER NOT NULL,
    amount                   NUMBER(14, 2) NOT NULL,
    "Date"                   DATE NOT NULL,
    bank_accounts_f_id INTEGER NOT NULL,
    transaction_type_f_id INTEGER NOT NULL
);

ALTER TABLE transaction_history ADD CONSTRAINT transaction_history_pk PRIMARY KEY ( transaction_id );

CREATE TABLE transaction_type (
    type_id   INTEGER NOT NULL,
    type_name VARCHAR2(15) NOT NULL
);

ALTER TABLE transaction_type ADD CONSTRAINT transaction_type_pk PRIMARY KEY ( type_id );

ALTER TABLE addresses
    ADD CONSTRAINT addresses_bank_branches_fk FOREIGN KEY ( bank_branches_f_id )
        REFERENCES bank_branches ( branch_id );

ALTER TABLE addresses
    ADD CONSTRAINT addresses_cities_fk FOREIGN KEY ( cities_f_id )
        REFERENCES cities ( city_id );

ALTER TABLE bank_accounts
    ADD CONSTRAINT bank_accounts_account_types_fk FOREIGN KEY ( account_types_f_id )
        REFERENCES account_types ( type_id );

ALTER TABLE bank_accounts
    ADD CONSTRAINT bank_accounts_clients_fk FOREIGN KEY ( clients_f_id )
        REFERENCES clients ( client_id );

ALTER TABLE bank_branches
    ADD CONSTRAINT bank_branches_addresses_fk FOREIGN KEY ( addresses_f_id )
        REFERENCES addresses ( address_id );

ALTER TABLE bank_branches
    ADD CONSTRAINT bank_branches_opening_hours_fk FOREIGN KEY ( opening_hours_id )
        REFERENCES opening_hours ( id );

ALTER TABLE cards
    ADD CONSTRAINT cards_bank_accounts_fk FOREIGN KEY ( bank_accounts_f_id )
        REFERENCES bank_accounts ( account_id )
            ON DELETE CASCADE;

ALTER TABLE cards
    ADD CONSTRAINT cards_card_types_fk FOREIGN KEY ( card_types_f_id )
        REFERENCES card_types ( type_id );

ALTER TABLE clients
    ADD CONSTRAINT clients_employees_fk FOREIGN KEY ( employees_f_id )
        REFERENCES employees ( employee_id );

ALTER TABLE clients
    ADD CONSTRAINT clients_personal_data_fk FOREIGN KEY ( personal_data_f_id )
        REFERENCES personal_data ( data_id );

ALTER TABLE employees
    ADD CONSTRAINT employees_personal_data_fk FOREIGN KEY ( personal_data_f_id )
        REFERENCES personal_data ( data_id );

ALTER TABLE employees
    ADD CONSTRAINT employees_professions_fk FOREIGN KEY ( professions_f_id )
        REFERENCES professions ( profession_id );

ALTER TABLE personal_data
    ADD CONSTRAINT personal_data_addresses_fk FOREIGN KEY ( addresses_f_id )
        REFERENCES addresses ( address_id );

--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE transaction_history
    ADD CONSTRAINT transaction_hist_bank_acc_fk FOREIGN KEY ( bank_accounts_f_id )
        REFERENCES bank_accounts ( account_id );

--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE transaction_history
    ADD CONSTRAINT transaction_hist_trans_type_fk FOREIGN KEY ( transaction_type_f_id )
        REFERENCES transaction_type ( type_id )
            ON DELETE CASCADE;



-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                            14
-- CREATE INDEX                             2
-- ALTER TABLE                             29
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   2
-- WARNINGS                                 0
