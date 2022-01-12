-- check trigger
create or replace TRIGGER "Z08"."T_CHECK_SALARY" 
BEFORE INSERT OR UPDATE OF SALARY ON EMPLOYEES 
FOR EACH ROW
DECLARE 
    v_min_sal professions.min_salary%TYPE;
    v_max_sal professions.max_salary%TYPE;
BEGIN 
    SELECT min_salary, max_salary INTO v_min_sal, v_max_sal
    FROM professions WHERE profession_id = :new.professions_profession_id;

    IF :new.salary NOT BETWEEN v_min_sal AND v_max_sal THEN
        dbms_output.put_line('Zarobki pracownika spoza zakresu płac: ' || v_min_sal || ' ' || v_max_sal);
        raise_application_error(-20001, 'Przekroczony zakres płac');
    END IF;
END;

-- trigger used to set default ending date of loan if one was not specified 
create or replace TRIGGER "Z08"."T_DEF_LOAN_END_DATE" 
BEFORE INSERT OR UPDATE OF END_DATE ON LOANS
FOR EACH ROW
DECLARE
    v_date DATE;
BEGIN
    SELECT add_months(CURRENT_DATE,12)INTO v_date 
    FROM dual;

    IF :new.end_date IS NULL THEN
        :new.end_date := v_date;
    ELSIF :new.end_date < CURRENT_DATE THEN 
        :new.end_date := v_date;
    END IF;
END;

-- triggers to change number of employees assigned to specific bank branch
create or replace TRIGGER "Z08"."T_EMPLOYEES_NO_D" 
AFTER DELETE ON EMPLOYEES
FOR EACH ROW
BEGIN 
    UPDATE bank_branches 
    SET employees_no = employees_no - 1
    WHERE bank_branch_id = :old.branch_id;
END;

create or replace TRIGGER "Z08"."T_EMPLOYEES_NO_I" 
AFTER INSERT ON EMPLOYEES
FOR EACH ROW
BEGIN 
    UPDATE bank_branches 
    SET employees_no = employees_no + 1
    WHERE bank_branch_id = :new.branch_id;
END;

-- trigger used to assign assistant with the least number of clients to new client
create or replace TRIGGER "Z08"."T_NEW_CLIENT_ASSISTANT" 
BEFORE INSERT ON CLIENTS
FOR EACH ROW
DECLARE 
    v_assistant CLIENTS.EMPLOYEES_EMPLOYEE_ID%TYPE;
    v_profession PROFESSIONS.NAME%TYPE;
BEGIN
    SELECT employee_id INTO v_assistant 
    FROM v_needs_work;

    IF :new.employees_employee_id IS NULL THEN
        :new.employees_employee_id := v_assistant;
    ELSE
        SELECT name INTO v_profession 
        FROM professions 
        WHERE profession_id = 
            (SELECT professions_profession_id 
            FROM employees 
            WHERE employee_id = :new.employees_employee_id);
        IF v_profession NOT LIKE 'assistant' THEN 
            :new.employees_employee_id := v_assistant;
        END IF;
    END IF;
END;

-- trigger creating second row in transaction_history, used to describe a second side of transaction between two clients
create or replace TRIGGER "Z08"."T_TRANS_HISTORY" 
BEFORE INSERT ON transaction_history
FOR EACH ROW
DECLARE
    v_type_name transaction_type.type_name%TYPE;
    v_transfer_in_id transaction_type.transaction_type_id%TYPE;
    v_target_curr currencies.exchange_rate%TYPE;
    v_own_curr currencies.exchange_rate%TYPE;
    v_amount transaction_history.amount%TYPE;
BEGIN
    SELECT TYPE_NAME INTO v_type_name
    FROM transaction_type 
    WHERE transaction_type_id = :new.transaction_type_type_id;
    SELECT TRANSACTION_TYPE_ID INTO v_transfer_in_id
    FROM transaction_type
    WHERE type_name like 'transfer_in';
    IF v_type_name like 'transfer_out' THEN
        SELECT exchange_rate INTO v_own_curr FROM v_bank_accounts WHERE bank_account_id = :new.bank_account_id;
        SELECT exchange_rate INTO v_target_curr FROM v_bank_accounts WHERE bank_account_id = :new.target_acc_no;
        IF v_own_curr <> v_target_curr THEN
            v_amount := F_CONVERT_ACC_CURRENCY(:new.amount, v_own_curr, v_target_curr);
        ELSE
            v_amount := :new.amount;
        END IF;
        UPDATE SERVICES_INFO SET BALANCE = BALANCE + v_amount
        WHERE SERVICE_INFO_ID = 
            (SELECT SERVICE_INFO_ID 
            FROM BANK_ACCOUNTS 
            WHERE BANK_ACCOUNT_ID = :new.TARGET_ACC_NO);
        INSERT INTO TRANSACTION_HISTORY (TRANSACTION_ID, AMOUNT, "Date", BANK_ACCOUNT_ID, TRANSACTION_TYPE_TYPE_ID, TARGET_ACC_NO)
        VALUES ("Z08"."ISEQ$$_271389".nextval -1, :new.amount, :new."Date", :new.target_acc_no, v_transfer_in_id, :new.bank_account_id);
        :new.transaction_id := :new.transaction_id + 1;
    END IF;
END;

-- trigger creates new unique bank account number on insert
create or replace TRIGGER T_ADD_BANK_ACCOUNT
BEFORE INSERT ON BANK_ACCOUNTS
FOR EACH ROW
BEGIN
    :new.ACCOUNT_NO := F_FIND_UNIQUE_ACC_NO;
END;


-- trigger adds abbreviation of currency used during payment
create or replace TRIGGER T_PAYMENT_HISTORY 
BEFORE INSERT ON PAYMENT_HISTORY
FOR EACH ROW 
DECLARE 
    v_currency CURRENCIES.ABBREVIATION%TYPE;
BEGIN
    SELECT ABBREVIATION INTO v_currency 
    FROM v_loans 
    WHERE loan_id = :new.loan_id;

    :new.currency := v_currency;
END;

