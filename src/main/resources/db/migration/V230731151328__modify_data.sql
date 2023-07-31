/*

1.
Разработка-199-Сценарий 4. Заполнение поставщиком КП Общий профиль (БД)
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/92#note_83619
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/92#note_83629

2.
Разработка-193-Значение переменной по умолчанию
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/121

*/

-- 1.
INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) VALUES(1058003, '{Значение_ID_запроса}', 'Значение ID запроса', NULL, '2023-07-31 15:16:30.647', NULL, NULL, NULL);
INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES(1059003, 'EP_COMMERCIAL_OFFER_RECEIVED', 'Effect. Анализ рынка - поступило коммерческое предложение от Участника', NULL, '2023-07-31 15:30:59.323', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');

-- 2.
UPDATE "admin".t_variable tv SET default_val = 'ПАО "Газпром нефть"' WHERE id = 1012002 ;
