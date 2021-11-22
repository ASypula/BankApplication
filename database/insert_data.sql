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
--INSERT INTO personal_data VALUES (7001, 'Anna', 'Kowalska', '00148677354', '423756877', 'pwd', 1004, 5003, 6001);


-- table CARDS
-- table BANK_BRANCHES
-- table BANK_ACCOUNTS
-- table CLIENTS
-- table EMPLOYEES
-- table PERSONAL DATA
-- table TRANSACTION_HISTORY
