																	TESTY

===============================================
Test działania procedury do zmiany kursu walut:

alter session set NLS_NUMERIC_CHARACTERS='.,'; 

exec p_alter_currencies(4.0349, 4.5886, 5.4241, 5.4598, 0.1844, 0.0539, 0.3032, 0.6538, 0.4459);
SELECT NAME, EXCHANGE_RATE FROM CURRENCIES;


================================================
Test działania przewalutowywania:

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

====================================================
Wybierz klientow pracownikow ktorzy mieszkaja w tym samym mieście co przypisany im asystent

SELECT e.name||' '||e.surname as asystent, c.name||' '||c.surname as klient, e.city_name
FROM (
    SELECT v1.name, v1.surname, em.employee_id, v1.city_name
    FROM v_personal_data v1 JOIN employees em ON v1.personal_data_id = em.personal_data_data_id 
    WHERE em.PROFESSIONS_PROFESSION_ID = (
        SELECT PROFESSION_ID 
        FROM PROFESSIONS
        WHERE NAME like 'assistant')) e
JOIN
    (SELECT name, surname, employees_employee_id, city_name
    FROM v_personal_data v2 JOIN clients cl ON v2.personal_data_id = cl.personal_data_data_id) c
ON e.employee_id = c.employees_employee_id
WHERE e.city_name = c.city_name
ORDER BY asystent;

=====================================================
Wybierz wszystkich klientow ktorzy dokonali conajmniej jednej transakcji z innym kontem

SELECT name, surname 
FROM personal_data 
WHERE personal_data_id IN (
    SELECT personal_data_data_id
    FROM clients
    WHERE client_id IN (
        SELECT client_id 
        FROM services_info 
        WHERE service_info_id IN (
            SELECT service_info_id 
            FROM bank_accounts
            WHERE bank_account_id IN (
                SELECT bank_account_id
                FROM transaction_history 
                WHERE transaction_type_type_id = (SELECT TRANSACTION_TYPE_ID FROM TRANSACTION_TYPE WHERE TYPE_NAME LIKE 'transfer_out')
                GROUP BY bank_account_id
                HAVING count(transaction_type_type_id) > 0
    ))));
	
=======================================================
Test dzialania triggera ustawiającego domyslna date konca kredytu

INSERT INTO SERVICES_INFO (BALANCE, ACCUM_PERIOD, INTEREST, CLIENT_ID)
VALUES (30000, 3, 5, 11);
INSERT INTO LOANS (INSTALLMENT, INITIAL_VALUE, SERVICE_INFO_ID)
VALUES (1000, 30000, 8)
-- 8 jest w ramach testu, może to byc inna wartosc przy wlasciwym testowaniu

========================================================
Test dodawania drugiego rekordu przy przelewach miedzy dwoma kontami

SELECT * FROM TRANSACTION_HISTORY WHERE BANK_ACCOUNT_ID = 3 OR TARGET_ACC_NO = 3;
INSERT INTO TRANSACTION_HISTORY (AMOUNT, BANK_ACCOUNT_ID, TRANSACTION_TYPE_TYPE_ID, TARGET_ACC_NO)
VALUES (200, 3, 4, 2);
SELECT * FROM TRANSACTION_HISTORY WHERE BANK_ACCOUNT_ID = 3 OR TARGET_ACC_NO = 3;

=========================================================
Test dzialania funkji obliczajacej wysokosc rat przewidzianych na ten rok -> pokaz imie i nazwisko klientow ktorzy prawdopodobnie zamkna swoje kredyty

SELECT name, surname 
FROM CLIENTS c JOIN PERSONAL_DATA p ON c.personal_data_data_id = p.personal_data_id
WHERE c.client_id IN (
    SELECT s.client_id 
    FROM services_info s JOIN loans l on l.service_info_id = s.service_info_id
    WHERE s.balance = F_YEAR_INSTALLMENTS(l.loan_id) 
    );
