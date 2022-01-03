TESTY

===============================================
Test dzialania procedury do zmiany kursu walut:

alter session set NLS_NUMERIC_CHARACTERS='.,'; 

exec p_alter_currencies(4.0349, 4.5886, 5.4241, 5.4598, 0.1844, 0.0539, 0.3032, 0.6538, 0.4459);
SELECT NAME, EXCHANGE_RATE FROM CURRENCIES;


================================================
Test dzialania przewalutowywania:

alter session set NLS_NUMERIC_CHARACTERS='.,'; 

SELECT c.currency_id, balance,  name, exchange_rate  FROM services_info s JOIN CURRENCIES c on c.currency_id = s.currency_id WHERE SERVICE_INFO_ID = 1;
exec p_convert_currency(1,2);
SELECT c.currency_id, balance,  name, exchange_rate  FROM services_info s JOIN CURRENCIES c on c.currency_id = s.currency_id WHERE SERVICE_INFO_ID = 1;
exec p_convert_currency(1,1);
SELECT c.currency_id, balance,  name, exchange_rate  FROM services_info s JOIN CURRENCIES c on c.currency_id = s.currency_id WHERE SERVICE_INFO_ID = 1;


==================================================
Kursor numer 1:

DECLARE 
CURSOR c_next_accum IS
    SELECT * FROM services_info;
v_rec_service_info services_info%ROWTYPE;
v_date DATE;
v_accum_period services_info.accum_period%TYPE;
BEGIN
    OPEN c_next_accum;
    LOOP
        EXIT WHEN c_next_accum%NOTFOUND;
        FETCH c_next_accum INTO v_rec_service_info;
        v_date := v_rec_service_info.START_DATE;
        v_accum_period := v_rec_service_info.ACCUM_PERIOD;
        WHILE v_date < CURRENT_DATE LOOP
            v_date := add_months(v_date, v_accum_period);
        END LOOP;
        dbms_output.put_line('Next accumulation period for service with id: '||v_rec_service_info.service_info_id||' is on '||v_date);
    END LOOP;
END;


===================================================
Kursor numer 2:

DECLARE 
CURSOR c_year_installments IS
    SELECT * FROM v_loans;
v_rec_loan v_loans%ROWTYPE;
v_sum_installments v_loans.balance%TYPE;
BEGIN
    OPEN c_year_installments;
    LOOP
        EXIT WHEN c_year_installments%NOTFOUND;
        FETCH c_year_installments INTO v_rec_loan;
        v_sum_installments := f_year_installments(v_rec_loan.loan_id);
        dbms_output.put_line('Sum of installments for loan with id '||v_rec_loan.loan_id||' is equal to '||v_sum_installments);
    END LOOP;
END;
