DECLARE 
v_maks_id NUMBER;
BEGIN
    --1
    SELECT ADDRESS_ID INTO v_maks_id FROM ADDRESSES ORDER BY ADDRESS_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271359".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji adresy');
    END LOOP;
    
    --2
    SELECT BANK_ACCOUNT_ID INTO v_maks_id FROM BANK_ACCOUNTS ORDER BY BANK_ACCOUNT_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271361".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji konta');
    END LOOP;
    
    --3
    SELECT BANK_BRANCH_ID INTO v_maks_id FROM BANK_BRANCHES ORDER BY BANK_BRANCH_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271363".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji branches');
    END LOOP;
    
    --4
    SELECT CARD_ID INTO v_maks_id FROM CARDS ORDER BY CARD_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271365".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji karty');
    END LOOP;
    
    --5
    SELECT CITY_ID INTO v_maks_id FROM CITIES ORDER BY CITY_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271369".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji miasta');
    END LOOP;
    
    --6
    SELECT CLIENT_ID INTO v_maks_id FROM CLIENTS ORDER BY CLIENT_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271371".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji klienci');
    END LOOP;
    
    --7
    SELECT CURRENCY_ID INTO v_maks_id FROM CURRENCIES ORDER BY CURRENCY_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271373".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji waluty');
    END LOOP;
    
    --8
    SELECT EMPLOYEE_ID INTO v_maks_id FROM EMPLOYEES ORDER BY EMPLOYEE_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271375".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji pracownicy');
    END LOOP;
    
    --9
    SELECT LOAN_ID INTO v_maks_id FROM LOANS ORDER BY LOAN_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271377".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji kredyty');
    END LOOP;
    
    --10
    SELECT OPENING_HOURS_ID INTO v_maks_id FROM OPENING_HOURS ORDER BY OPENING_HOURS_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271379".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji godziny otwarcia');
    END LOOP;
    
    --11
    SELECT PAYMENT_ID INTO v_maks_id FROM PAYMENT_HISTORY ORDER BY PAYMENT_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271381".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji splaty rat');
    END LOOP;
    
    --12
    SELECT PERSONAL_DATA_ID INTO v_maks_id FROM PERSONAL_DATA ORDER BY PERSONAL_DATA_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271383".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji informacje osobiste');
    END LOOP;
    
    --13
    SELECT PROFESSION_ID INTO v_maks_id FROM PROFESSIONS ORDER BY PROFESSION_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271385".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji zawody');
    END LOOP;
    
    --14
    SELECT SERVICE_INFO_ID INTO v_maks_id FROM SERVICES_INFO ORDER BY SERVICE_INFO_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271387".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji informacje o uslugach');
    END LOOP;
    
    --15
    SELECT TRANSACTION_ID INTO v_maks_id FROM TRANSACTION_HISTORY ORDER BY TRANSACTION_ID DESC FETCH FIRST 1 ROWS ONLY;
    WHILE "Z08"."ISEQ$$_271389".nextval < v_maks_id LOOP
        dbms_output.put_line('Zmieniono wartosc sekwencji transakcje');
    END LOOP;
    
END;
