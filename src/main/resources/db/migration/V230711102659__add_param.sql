/*

Разработка-198-Добавление нового параметра в таблицу
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/114

*/

INSERT INTO "admin".a_param
("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val)
VALUES('PARTNER_EXCHANGE.EMAIL_SENDER', 'Технический почтовый ящик для отправки писем запросов поставщикам', 'effect_zakupki@gazprom-neft.ru', '2023-07-11 10:31:46.351', NULL, NULL, NULL, NULL);
