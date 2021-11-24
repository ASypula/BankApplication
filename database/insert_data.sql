-- table OPENING_HOURS
INSERT INTO opening_hours VALUES (101, '08-20', '08-20', '08-20', '08-20', '08-20', null, null);
INSERT INTO opening_hours VALUES (102, '08-22', '08-22', '08-22', '08-20', '09-12', null, null);
INSERT INTO opening_hours VALUES (103, '08-20', '08-22', null, '08-20', '09-12', '10-12', '10-12');

-- table CITIES
INSERT INTO cities VALUES (1001, 'Warsaw');
INSERT INTO cities VALUES (1002, 'Lodz');
INSERT INTO cities VALUES (1003, 'Krakow');
INSERT INTO cities VALUES (1004, 'Gdansk');
INSERT INTO cities VALUES (1005, 'Wroclaw');

-- table ADDRESSES
INSERT INTO addresses VALUES (1001, 'Marszalkowska', 24, 1001);
INSERT INTO addresses VALUES (1002, 'Prosta', 4, 1001);
INSERT INTO addresses VALUES (1003, 'Koszykowa', 18, 1001);
INSERT INTO addresses VALUES (1004, 'Okopowa', 28, 1003);
INSERT INTO addresses VALUES (1005, 'Jasna', 273, 1003);
INSERT INTO addresses VALUES (1006, 'Jasna', 283, 1003);
INSERT INTO addresses VALUES (1007, 'Majowa', 2, 1002);
INSERT INTO addresses VALUES (1008, 'Kwiatowa', 3, 1002);
INSERT INTO addresses VALUES (1009, 'Chmielna', 73, 1004);
INSERT INTO addresses VALUES (1010, 'Zelazna', 31, 1005);
INSERT INTO addresses VALUES (1011, 'Szczesliwa', 27, 1003);
INSERT INTO addresses VALUES (1012, 'Prosta', 5, 1001);
INSERT INTO addresses VALUES (1013, 'Prosta', 6, 1001);

INSERT INTO addresses VALUES (1101, 'Zwirowa', 46, 1001);
INSERT INTO addresses VALUES (1102, 'Polna', 77, 1001);
INSERT INTO addresses VALUES (1103, 'Brukowa', 68, 1002);
commit;

-- table ACCOUNT_TYPES
INSERT INTO account_types VALUES (2001, 'personal');
INSERT INTO account_types VALUES (2002, 'savings');
INSERT INTO account_types VALUES (2003, 'company');
INSERT INTO account_types VALUES (2004, 'recur_deposit');
INSERT INTO account_types VALUES (2005, 'fixed_deposit');
INSERT INTO account_types VALUES (2006, 'salary');
INSERT INTO account_types VALUES (2007, 'student');
INSERT INTO account_types VALUES (2008, 'senior');

-- table CARD_TYPES
INSERT INTO card_types VALUES (3001, 'credit');
INSERT INTO card_types VALUES (3002, 'debit');
INSERT INTO card_types VALUES (3003, 'savings');
INSERT INTO card_types VALUES (3004, 'student');

-- table TRANSACTION_TYPE
INSERT INTO transaction_type VALUES (4001, 'withdrawal');
INSERT INTO transaction_type VALUES (4002, 'payment');
INSERT INTO transaction_type VALUES (4003, 'return');
INSERT INTO transaction_type VALUES (4004, 'transfer_in');
INSERT INTO transaction_type VALUES (4005, 'transfer_out');

-- table PROFESSIONS
INSERT INTO professions VALUES (401, 'clerk', 3000, 9000);
INSERT INTO professions VALUES (402, 'accountant', 2000, 7000);
INSERT INTO professions VALUES (403, 'programmer', 10000, 20000);
INSERT INTO professions VALUES (404, 'assistant', 3200, 7200);
INSERT INTO professions VALUES (405, 'cleaner', 1500, 3000);
INSERT INTO professions VALUES (406, 'banker', 5500, 9000);
INSERT INTO professions VALUES (407, 'analyst', 7500, 12000);
INSERT INTO professions VALUES (408, 'manager', 15000, 30000);

commit;

-- table PERSONAL DATA
INSERT INTO personal_data VALUES (7001, 'Beata', 'Jablonska', '46548379354', '486756114', 'pwd', 1002);
INSERT INTO personal_data VALUES (7002, 'Bartosz', 'Nowak', '05148377354', '487756971', 'pwd', 1002);
INSERT INTO personal_data VALUES (7003, 'Anna', 'Kowalska', '00148671354', '223756877', 'pwd', 1003);
INSERT INTO personal_data VALUES (7004, 'Celina', 'Makowska', '70345671354', '175756877', 'pwd', 1006);
INSERT INTO personal_data VALUES (7005, 'Darek', 'Malinowski', '70345678897', '225346877', 'pwd', 1005);
INSERT INTO personal_data VALUES (7006, 'Daria', 'Malinowska', '70145623297', '708346247', 'pwd', 1005);
INSERT INTO personal_data VALUES (7007, 'Ewelina', 'Kasprzak', '24335623497', '900346247', 'pwd', 1001);
INSERT INTO personal_data VALUES (7008, 'Eryk', 'Konieczny', '70115623465', '717566247', 'pwd', 1007);
commit;

-- table EMPLOYEES
INSERT INTO employees VALUES (6001, 4500, 401, 7002);
INSERT INTO employees VALUES (6002, 5700, 401, 7006);

-- table CLIENTS
INSERT INTO clients VALUES (5001, 7001, 6001);
INSERT INTO clients VALUES (5002, 7003, 6002);
INSERT INTO clients VALUES (5003, 7004, 6001);
INSERT INTO clients VALUES (5004, 7005, 6001);
INSERT INTO clients VALUES (5005, 7007, 6002);
INSERT INTO clients VALUES (5006, 7008, 6001);

-- table BANK_BRANCHES
INSERT INTO bank_branches VALUES (4001, 2, 1101, 102);
INSERT INTO bank_branches VALUES (4002, 2, 1102, 101);
INSERT INTO bank_branches VALUES (4003, 1, 1103, 103);
commit;

-- dalsze niesprawdzone :( wina sql'a
-- table BANK_ACCOUNTS
INSERT INTO bank_accounts VALUES (9001, 20000, 1, TO_DATE('06-08-2000', 'DD-MM-YYYY'), null, 3, null, null, 2006, 5001);
INSERT INTO bank_accounts VALUES (9002, 80000, 2, TO_DATE('06-02-1980', 'DD-MM-YYYY'), TO_DATE('06-02-2022', 'DD-MM-YYYY'), 4, null, null, 2002, 5001);
INSERT INTO bank_accounts VALUES (9003, 15600, 1, TO_DATE('09-05-2009', 'DD-MM-YYYY'), null, 2, null, null, 2001, 5002);

-- table CARDS
INSERT INTO cards VALUES (901, TO_DATE('06-06-2023', 'DD-MM-YYYY'), 'hash', 'hash', 3001, 9001);
INSERT INTO cards VALUES (902, TO_DATE('01-01-2023', 'DD-MM-YYYY'), 'hash', 'hash', 3002, 9001);
INSERT INTO cards VALUES (903, TO_DATE('11-12-2023', 'DD-MM-YYYY'), 'hash', 'hash', 3002, 9003);

-- table TRANSACTION_HISTORY
INSERT INTO transaction_history VALUES (10000, 20, TO_DATE('06-06-2021', 'DD-MM-YYYY'), 9001, 4002);
INSERT INTO transaction_history VALUES (10001, 200, TO_DATE('07-06-2021', 'DD-MM-YYYY'), 9001, 4002);
INSERT INTO transaction_history VALUES (10002, 50, TO_DATE('07-06-2021', 'DD-MM-YYYY'), 9001, 4001);
INSERT INTO transaction_history VALUES (10003, 64, TO_DATE('09-06-2021', 'DD-MM-YYYY'), 9001, 4002);
INSERT INTO transaction_history VALUES (10004, 670, TO_DATE('16-06-2021', 'DD-MM-YYYY'), 9001, 4002);

commit;