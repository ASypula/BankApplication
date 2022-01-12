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

====================================================
Wybierz klientow pracownikow ktorzy mieszkaja w tym samym miescie co przypisany im asystent

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
Test dzialania triggera ustawiajacego domyslna date konca kredytu

INSERT INTO SERVICES_INFO (BALANCE, ACCUM_PERIOD, INTEREST, CLIENT_ID)
VALUES (30000, 3, 5, 11);
INSERT INTO LOANS (INSTALLMENT, INITIAL_VALUE, SERVICE_INFO_ID)
VALUES (1000, 30000, 8)
-- 8 jest w ramach testu, moze to byc inna wartosc przy wlasciwym testowaniu

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


==========================================================
Test dzialania funkcji do przewalutowywania 

DECLARE 
    v_curr_1   NUMBER := 4.1834;
    v_curr_2   NUMBER := 2.5731;
    amount     NUMBER := 100;
    new_amount NUMBER;
BEGIN
    new_amount := f_convert_acc_currency(amount, v_curr_1, v_curr_2);
    dbms_output.put_line('Waluta o kursie '|| v_curr_1 || ' zostala zamieniona na walute o kursie '|| v_curr_2 ||'. Ilosc ' || amount || ' zostala zamieniona na '||new_amount);
END;


===========================================================
Test dzialania funkcji generujacej nowy kod ktory moze posluzyc za nowy numer konta

DECLARE 
    v_acc_no BANK_ACCOUNTS.account_no%TYPE; 
BEGIN
    v_acc_no := f_generate_code;
    dbms_output.put_line('Nowy kod od zostal wygenerowany '|| v_acc_no);
    v_acc_no := f_generate_code;
    dbms_output.put_line('Nowy kod od zostal wygenerowany '|| v_acc_no);
    v_acc_no := f_generate_code;
    dbms_output.put_line('Nowy kod od zostal wygenerowany '|| v_acc_no);
    v_acc_no := f_generate_code;
    dbms_output.put_line('Nowy kod od zostal wygenerowany '|| v_acc_no);
END;

===========================================================
Test dzialania funkcji znajdujacej nowy unikalny numer konta

DECLARE 
    v_acc_no    BANK_ACCOUNTS.account_no%TYPE; 
    v_other_acc NUMBER;
BEGIN
    v_acc_no := f_find_unique_acc_no;
    dbms_output.put_line('Wygenrerowany kod to '|| v_acc_no );
    SELECT count(account_no) INTO v_other_acc FROM BANK_ACCOUNTS WHERE account_no = v_acc_no;
    dbms_output.put_line('Ilosc innych kont o takim samym kodzie wynosi '|| v_other_acc );
END;


===========================================================
Test funkcji zwracajacej wartosc ostatniej transakcji danego klienta 

DECLARE 
    v_val NUMBER;
BEGIN
    v_val := f_last_transact_val(1);
    dbms_output.put_line('Ostatnia transakcja tego klienta wynosila '|| v_val );
END;


===========================================================
Test sprawdzajacy date kiedy powinna nastapic kolejnsa splata raty kredytu - zakladamy ze raty powinny byc splacane co miesiac od daty zalozenia

DECLARE 
    v_val        DATE;
    v_start_date DATE;
    v_loan_id    NUMBER := 1;
BEGIN
    v_val := f_next_installment(v_loan_id);
    SELECT START_DATE INTO v_start_date FROM V_LOANS WHERE LOAN_ID = v_loan_id;
    dbms_output.put_line('Dzisiaj jest '|| CURRENT_DATE ||' Kredyt zostal zalozony ' || v_start_date || ' Nastepna rata powinna byc splacona w dniu '|| v_val );
END;


===========================================================
Test dzialania triggera liczacego ilosc pracownikow 

DECLARE 
    v_pd_id NUMBER;
    v_emp_no NUMBER;
BEGIN

    SELECT employees_no INTO v_emp_no FROM bank_branches WHERE bank_branch_id = 2;
    dbms_output.put_line('Oddzial 2 ma ' || v_emp_no || ' pracownikow');


    INSERT INTO PERSONAL_DATA (NAME, SURNAME, PESEL, PHONE_NO, HASHED_PSWD, ADDRESSES_ADDRESS_ID) 
    VALUES ('Tester', 'Testujacy', '11111111111', '111111111', 'Test', 1);
    
    SELECT personal_data_id INTO v_pd_id FROM personal_data WHERE name like 'Tester';
    
    INSERT INTO EMPLOYEES (SALARY, PROFESSIONS_PROFESSION_ID, PERSONAL_DATA_DATA_ID, BRANCH_ID)
    VALUES (5000, 4, v_pd_id, 2);
    
    SELECT employees_no INTO v_emp_no FROM bank_branches WHERE bank_branch_id = 2;
    dbms_output.put_line('Oddzial 2 ma ' || v_emp_no || ' pracownikow');
    
    DELETE FROM employees WHERE personal_data_data_id = v_pd_id;
    
    SELECT employees_no INTO v_emp_no FROM bank_branches WHERE bank_branch_id = 2;
    dbms_output.put_line('Oddzial 2 ma ' || v_emp_no || ' pracownikow');
    
    ROLLBACK;
    
END;


===========================================================
Test dzialania triggera sprawdzajacego poprawnosc pensji

DECLARE 
    v_pd_id NUMBER;
BEGIN
    INSERT INTO PERSONAL_DATA (NAME, SURNAME, PESEL, PHONE_NO, HASHED_PSWD, ADDRESSES_ADDRESS_ID) 
    VALUES ('Tester', 'Testujacy', '11111111111', '111111111', 'Test', 1);
    
    SELECT personal_data_id INTO v_pd_id FROM personal_data WHERE name like 'Tester';
    
    INSERT INTO EMPLOYEES (SALARY, PROFESSIONS_PROFESSION_ID, PERSONAL_DATA_DATA_ID, BRANCH_ID)
    VALUES (80000, 4, v_pd_id, 2);
    
    ROLLBACK;
END;


===========================================================
Test triggera ustawiajacego waluty dla transakcji

DECLARE 
    v_currency VARCHAR2(5);
    v_new_rec  VARCHAR2(5);
BEGIN
    SELECT abbreviation INTO v_currency FROM v_bank_accounts  WHERE BANK_ACCOUNT_ID = 1;
    dbms_output.put_line('Waluta tego konta to '|| v_currency );
    
    INSERT INTO TRANSACTION_HISTORY (AMOUNT, BANK_ACCOUNT_ID, TRANSACTION_TYPE_TYPE_ID)
    VALUES (100, 1, 2);
    
    SELECT CURRENCY INTO v_new_rec FROM TRANSACTION_HISTORY WHERE bank_account_id = 1 
    ORDER BY ("Date") DESC FETCH FIRST 1 ROWS ONLY;
    
    dbms_output.put_line('Waluta ostatniej transakcji to '|| v_new_rec );


    SELECT abbreviation INTO v_currency FROM v_bank_accounts  WHERE BANK_ACCOUNT_ID = 2;
    dbms_output.put_line('Waluta tego konta to '|| v_currency );
    
    INSERT INTO TRANSACTION_HISTORY (AMOUNT, BANK_ACCOUNT_ID, TRANSACTION_TYPE_TYPE_ID)
    VALUES (100, 2, 2);
    
    SELECT CURRENCY INTO v_new_rec FROM TRANSACTION_HISTORY WHERE bank_account_id = 2 
    ORDER BY ("Date") DESC FETCH FIRST 1 ROWS ONLY;
    
    dbms_output.put_line('Waluta ostatniej transakcji to '|| v_new_rec );
    
    ROLLBACK;
END;


===========================================================
Test triggera ustawiajacego waluty dla wplat rat kredytu
DECLARE 
    v_currency VARCHAR2(5);
    v_new_rec  VARCHAR2(5);
BEGIN
    SELECT abbreviation INTO v_currency FROM v_loans WHERE loan_ID = 1;
    dbms_output.put_line('Waluta tego kredytu to '|| v_currency );
    
    INSERT INTO PAYMENT_HISTORY (AMOUNT, LOAN_ID)
    VALUES (100, 1);
    
    SELECT CURRENCY INTO v_new_rec FROM PAYMENT_HISTORY WHERE loan_id = 1 
    ORDER BY Payment_Date DESC FETCH FIRST 1 ROWS ONLY;
    
    dbms_output.put_line('Waluta ostatniej wplaty to '|| v_new_rec );
    
    
    
    SELECT abbreviation INTO v_currency FROM v_loans WHERE loan_ID = 2;
    dbms_output.put_line('Waluta tego kredytu to '|| v_currency );
    
    INSERT INTO PAYMENT_HISTORY (AMOUNT, LOAN_ID)
    VALUES (100, 2);
    
    SELECT CURRENCY INTO v_new_rec FROM PAYMENT_HISTORY WHERE loan_id = 2 
    ORDER BY Payment_Date DESC FETCH FIRST 1 ROWS ONLY;
    
    dbms_output.put_line('Waluta ostatniej wplaty to '|| v_new_rec );
    
    ROLLBACK;
END;
