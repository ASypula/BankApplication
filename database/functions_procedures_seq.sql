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
