INSERT INTO services_info VALUES(1, 10000, to_date('21-JAN-20'), 20, 2, 1);
update bank_accounts set service_info_id=1 where bank_account_id=1;
INSERT INTO services_info VALUES(2, 15000, to_date('21-JUN-20'), 15, 3, 2);
update bank_accounts set service_info_id=2 where bank_account_id=2;
INSERT INTO services_info VALUES(3, 3000, to_date('20-JUN-20'), 14, 1, 3);
update bank_accounts set service_info_id=3 where bank_account_id=3;
commit;
