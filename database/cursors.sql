Cursor number 1:

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
Cursor number 2:

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
