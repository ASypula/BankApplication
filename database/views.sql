CREATE OR REPLACE VIEW v_bank_accounts AS
SELECT b.bank_account_id, b.account_no, b.account_types_type_id, s.balance, s.start_date, s.accum_period, s.interest, s.client_id, t.name
FROM bank_accounts b INNER JOIN SERVICES_INFO s USING(service_info_id) INNER JOIN Account_types t ON t.account_type_id = b.account_types_type_id;

CREATE OR REPLACE VIEW v_loans AS
SELECT l.loan_id, l.initial_value, s.balance, l.installment, s.start_date, l.end_date, s.accum_period, s.interest, s.client_id
FROM loans l INNER JOIN SERVICES_INFO s USING(service_info_id);

--address view
CREATE OR REPLACE VIEW v_addresses AS
SELECT a.address_id, a.street, a.apartment_no, c.city_name
FROM addresses a INNER JOIN cities c ON a.CITIES_CITY_ID = c.CITY_ID;

-- list of assistants with no. of clients assigned to them
CREATE OR REPLACE VIEW V_ASSISTANTS_CLIENTS ("EMPLOYEE_ID", "NO_OF_CLIENTS") AS 
SELECT e.employee_id, count(c.client_id) 
FROM employees e LEFT OUTER JOIN clients c ON c.employees_employee_id = e.employee_id
WHERE e.professions_profession_id = 
    (SELECT PROFESSION_ID FROM PROFESSIONS WHERE NAME = 'assistant')
GROUP BY e.employee_id
ORDER BY count(c.client_id);

-- view used to return the assistant whe the least number of clients assigned to him    
CREATE OR REPLACE VIEW v_needs_work AS
SELECT employee_id FROM v_assistants_clients WHERE rownum = 1;

-- view used to describe set of personal data, addresses and cities 
CREATE OR REPLACE VIEW v_personal_data AS
SELECT personal_data_id, p.name, p.surname, PESEL, PHONE_NO, HASHED_PSWD, ADDRESSES_ADDRESS_ID, STREET, APARTMENT_NO, CITIES_CITY_ID, CITY_NAME
FROM personal_data p join ADDRESSES a ON p.addresses_address_id = a.address_id join CITIES c on a.cities_city_id = c.city_id;
