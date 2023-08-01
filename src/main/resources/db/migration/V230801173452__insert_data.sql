/*

Разработка-199-Сценарий 4. Заполнение поставщиком КП Общий профиль (БД)
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/92#note_83931

*/

INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES(1066003, 'EP_COMMERCIAL_OFFER_DEADLINE_COMING_END', 'Effect. Анализ рынка - завершается срок подачи коммерческого предложения', NULL, '2023-08-01 17:39:14.808', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');
INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES(1067003, 'EP_COMMERCIAL_OFFER_DEADLINE_COMPLETED', 'Effect. Анализ рынка - завершен срок подачи коммерческого предложения', NULL, '2023-08-01 17:39:14.808', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');
