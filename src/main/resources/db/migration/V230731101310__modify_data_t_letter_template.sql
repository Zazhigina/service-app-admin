/*

Разработка-198-Добавление/удаление записей в таблицу admin.t_letter_template
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/120

*/

DELETE FROM "admin".t_letter_template WHERE letter_type IN ('EP_COMMERCIAL_OFFER_REPEAT_REQUEST_TS','EP_COMMERCIAL_OFFER_REPEAT_REQUEST_TS_TP') ;
INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES(1056003, 'EP_COMMERCIAL_OFFER_REQUEST_NON_ACTUAL', 'Effect. Анализ рынка - неактуальность запроса коммерческого предложения', NULL, '2023-07-31 10:23:29.685', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT') ;
