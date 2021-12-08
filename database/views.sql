CREATE OR REPLACE VIEW v_bank_accounts AS
SELECT b.bank_account_id, s.balance, s.start_date, s.accum_period, s.interest, s.client_id, t.name
FROM bank_accounts b INNER JOIN SERVICES_INFO s USING(service_info_id) INNER JOIN Account_types t ON t.account_type_id = b.account_types_type_id;

CREATE OR REPLACE VIEW v_loans AS
SELECT l.loan_id, l.initial_value, s.balance, l.installment, s.start_date, l.end_date, s.accum_period, s.interest, s.client_id
FROM loans l INNER JOIN SERVICES_INFO s USING(service_info_id);

--address view
CREATE OR REPLACE VIEW v_addresses AS
SELECT a.address_id, a.street, a.apartment_no, c.city_name
FROM addresses a INNER JOIN cities c ON a.CITIES_CITY_ID = c.CITY_ID;
