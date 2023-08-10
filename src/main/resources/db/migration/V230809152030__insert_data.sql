/*
Разработка-999-Добавление и изменение записей в таблицы схемы admin
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/28

*/

--1
INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) 
    VALUES(21005, 'EP_COMMERCIAL_OFFER_ACCEPTING_COMING_END_INITIATOR', 'Effect. Анализ рынка - завершается срок приема КП', NULL, '2023-08-10 12:20:12.844', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');

INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) 
    VALUES(22005, 'EP_COMMERCIAL_OFFER_ACCEPTING_COMPLETED_INITIATOR', 'Effect. Анализ рынка - завершен срок приема КП', NULL, '2023-08-10 12:20:12.860', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');

--2
UPDATE "admin".t_letter_template
	SET letter_type='EP_COMMERCIAL_OFFER_ACCEPTING_COMING_END_CONTRACTOR'
	WHERE id=1066003;

UPDATE "admin".t_letter_template
	SET letter_type='EP_COMMERCIAL_OFFER_ACCEPTING_COMPLETED_CONTRACTOR'
	WHERE id=1067003;

--3
INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) 
    VALUES(23005, '{Срок_приема_КП_поставщика}', 'Срок приема КП поставщика', NULL, '2023-08-10 12:21:27.402', NULL, NULL, NULL);