create or replace FUNCTION       "F_CONVERT_ACC_CURRENCY" (p_amount NUMBER, p_exchange_rate NUMBER, p_new_exchange_rate NUMBER)
RETURN NUMBER
AS
    v_new_amount NUMBER;
BEGIN
    v_new_amount := p_amount * p_exchange_rate;
    v_new_amount := v_new_amount / p_new_exchange_rate;

    RETURN v_new_amount;
END;

create or replace FUNCTION       "F_LAST_TRANSACT_VAL" (p_client_id VARCHAR2)
RETURN NUMBER
AS
    last_paym_am   transaction_history.amount%TYPE;
BEGIN
    SELECT amount INTO last_paym_am 
    FROM transaction_history JOIN bank_accounts USING(bank_account_id) 
    JOIN services_info USING(service_info_id)
    WHERE client_id=p_client_id
    ORDER BY ("Date") desc FETCH FIRST 1 ROWS ONLY;

    RETURN last_paym_am;
END;

create or replace FUNCTION       "F_NEXT_INSTALLMENT" (p_loan_id NUMBER)
RETURN DATE
AS
    v_date   v_loans.START_DATE%TYPE; 
BEGIN
    SELECT START_DATE INTO v_date FROM v_loans WHERE loan_id = p_loan_id;
    WHILE v_date < CURRENT_DATE LOOP
        v_date := add_months(v_date, 1);
    END LOOP;
    RETURN v_date;
END;

create or replace FUNCTION       "F_YEAR_INSTALLMENTS" (p_loan_id NUMBER)
RETURN NUMBER
AS
    v_flag          BOOLEAN := TRUE;
    v_install_sum   v_loans.installment%TYPE := 0;
    v_install       v_loans.installment%TYPE;
    v_date          v_loans.START_DATE%TYPE := f_next_installment(p_loan_id);
    v_balance       v_loans.BALANCE%TYPE;
    v_new_year      v_loans.START_DATE%TYPE  := ROUND(add_months(CURRENT_DATE, 6), 'YEAR');
BEGIN
    SELECT installment INTO v_install FROM v_loans WHERE loan_id = p_loan_id;
    SELECT balance INTO v_balance FROM v_loans WHERE loan_id = p_loan_id;
    WHILE v_date < v_new_year AND v_flag LOOP
        IF v_install_sum + v_install > v_balance THEN
            v_flag := FALSE;
            v_install_sum := v_balance;
        ELSE 
            v_install_sum := v_install + v_install_sum;
        END IF;
        v_date := add_months(v_date,1);
    END LOOP;

    RETURN v_install_sum;
END;

create or replace PROCEDURE       "P_ALTER_CURRENCIES" (
    p_usd in NUMBER, 
    p_eur in NUMBER,  
    p_chf in NUMBER,  
    p_gbp in NUMBER,
    p_czk in NUMBER,
    p_rub in NUMBER,  
    p_try in NUMBER, 
    p_cny in NUMBER, 
    p_sek in NUMBER
)
IS
BEGIN
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_usd WHERE ABBREVIATION like 'USD';
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_eur WHERE ABBREVIATION like 'EUR';
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_chf WHERE ABBREVIATION like 'CHF';
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_gbp WHERE ABBREVIATION like 'GBP';
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_czk WHERE ABBREVIATION like 'CZK';
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_rub WHERE ABBREVIATION like 'RUB';
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_try WHERE ABBREVIATION like 'TRY';
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_cny WHERE ABBREVIATION like 'CNY';
    UPDATE CURRENCIES SET EXCHANGE_RATE = p_sek WHERE ABBREVIATION like 'SEK';
END;

create or replace PROCEDURE       "P_CONVERT_CURRENCY" (p_service_info_id NUMBER, p_new_currency_id NUMBER)
AS
    v_balance            NUMBER;
    v_new_balance        NUMBER;
    v_exchange_rate      NUMBER;
    v_new_exchange_rate  NUMBER;
BEGIN
    SELECT BALANCE INTO v_balance 
    FROM SERVICES_INFO
    WHERE SERVICE_INFO_ID = p_service_info_id;
    SELECT EXCHANGE_RATE INTO v_exchange_rate 
    FROM CURRENCIES 
    WHERE CURRENCY_ID = (
        SELECT CURRENCY_ID 
        FROM SERVICES_INFO
        WHERE SERVICE_INFO_ID = p_service_info_id);
    SELECT EXCHANGE_RATE INTO v_new_exchange_rate
    FROM CURRENCIES 
    WHERE CURRENCY_ID = p_new_currency_id;

    v_new_balance := f_convert_acc_currency(v_balance, v_exchange_rate, v_new_exchange_rate);

    UPDATE SERVICES_INFO 
    SET BALANCE = v_new_balance, CURRENCY_ID = p_new_currency_id
    WHERE SERVICE_INFO_ID = p_service_info_id;

END;

create or replace procedure P_DELETE_CLIENT(p_client_id NUMBER)
IS
    v_count NUMBER;
    v_NULL_ID NUMBER;
    v_pd_id NUMBER;
BEGIN
    SELECT count(service_info_id) INTO v_count 
    FROM services_info 
    WHERE client_id = p_client_id AND balance > 0;
    SELECT PERSONAL_DATA_ID INTO v_NULL_ID
    FROM personal_data 
    WHERE name = 'NULL' AND surname = 'NULL';
    SELECT personal_data_data_id INTO v_pd_id 
    FROM clients
    WHERE client_id = p_client_id;

    IF v_count > 0 THEN
        raise_application_error(-20002, 'Client cannot be deleted! He still has ongoing services!');
    ELSE
        UPDATE clients 
        SET personal_data_data_id = v_NULL_ID, employees_employee_id = NULL 
        WHERE CLIENT_ID = p_client_id;
        UPDATE SERVICES_INFO
        SET STATUS = 'CLOSED'
        WHERE CLIENT_ID = p_client_id;
        DELETE FROM personal_data
        WHERE personal_data_id = v_pd_id;
    END IF;
END;


-- Function generates unique account number code  
create or replace FUNCTION f_FIND_UNIQUE_ACC_NO
RETURN VARCHAR2
AS
    v_code VARCHAR2(19);
    v_check NUMBER := 0;
BEGIN
    v_code := F_GENERATE_CODE();
    SELECT count(account_no) INTO v_check FROM bank_accounts WHERE account_no = v_code;
    WHILE v_check <> 0 LOOP
        v_code := F_GENERATE_CODE();
        SELECT count(account_no) INTO v_check FROM bank_accounts WHERE account_no = v_code;
    END LOOP;

    RETURN v_code;
END;

-- Function generates random account number code
create or replace FUNCTION F_GENERATE_CODE 
RETURN VARCHAR2
AS
    v_numb NUMBER;
    v_segment VARCHAR2(4);
    v_code VARCHAR2(19);
BEGIN
    v_numb    := floor(dbms_random.value(1,9999));
    v_code    := lpad(v_numb, 4, '0');
    v_numb    := floor(dbms_random.value(1,9999));
    v_segment := lpad(v_numb, 4, '0');
    v_code    := v_code || ' ' || v_segment;
    v_numb    := floor(dbms_random.value(1,9999));
    v_segment := lpad(v_numb, 4, '0');
    v_code    := v_code || ' ' || v_segment;
    v_numb    := floor(dbms_random.value(1,9999));
    v_segment := lpad(v_numb, 4, '0');
    v_code    := v_code || ' ' || v_segment;

    RETURN v_code;
END;
