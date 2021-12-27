CREATE SEQUENCE next_client start with 100 maxvalue 1000 minvalue 0;
CREATE SEQUENCE next_person start with 100 maxvalue 1000 minvalue 0;

-- adds new client to personal_data and client tables
CREATE PROCEDURE add_client 
(p_name VARCHAR2, p_surname VARCHAR2, p_pesel VARCHAR2, p_phone_no VARCHAR2, p_pwd VARCHAR2, p_address_id NUMBER)
AS
    new_id      clients.client_id%TYPE;
    pers_id     clients.personal_data_data_id%TYPE;
    emp_id      clients.employees_employee_id%TYPE;
BEGIN
    SELECT next_client.NEXTVAL INTO new_id FROM dual;
    SELECT next_person.NEXTVAL INTO pers_id FROM dual;
    SELECT employee_id INTO emp_id FROM employees FETCH FIRST 1 ROWS ONLY;
    INSERT INTO personal_data VALUES (pers_id, p_name, p_surname, p_pesel, p_phone_no, p_pwd, p_address_id);
    INSERT INTO clients VALUES (new_id, pers_id, emp_id);
END;

-- removes a client from client table using client_id
CREATE PROCEDURE remove_client_with_client_id (p_client_id VARCHAR2)
AS
BEGIN
    DELETE FROM clients WHERE client_id=p_client_id;
END;

-- removes client from client table using personal_id
CREATE PROCEDURE remove_client_with_pers_id (p_pers_id VARCHAR2)
AS
    s_client_id     clients.client_id%TYPE;
BEGIN
    SELECT client_id INTO s_client_id FROM personal_data WHERE personal_data_id=p_pers_id;
    remove_client_client_id(s_client_id);
END;

-- returns total amount of installments to be paid in one year
CREATE OR REPLACE FUNCTION year_installments (p_loan_id VARCHAR2)
RETURN NUMBER
AS
    single_install   loans.installment%TYPE;
BEGIN
    SELECT installment INTO single_install FROM loans WHERE loan_id=p_loan_id;
    RETURN 12*single_install;
END;

-- returns amount of most recent transaction made by client with given client_id
CREATE OR REPLACE FUNCTION last_transaction_amount (p_client_id VARCHAR2)
RETURN NUMBER
AS
    last_paym_am   transaction_history.amount%TYPE;
BEGIN
    SELECT amount INTO last_paym_am FROM transaction_history 
    JOIN bank_accounts USING(bank_account_id) 
    JOIN services_info USING(service_info_id)
    WHERE client_id=p_client_id
    ORDER BY (date) desc FETCH FIRST 1 ROWS ONLY;
    
    RETURN last_paym_am;
END;
