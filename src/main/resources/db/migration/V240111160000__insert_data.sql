/*

Разработка-763-Продление срока КП поставщиком с внешней страницы. БД. Добавление шаблонов и переменных
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/64
*/

--insert into t_letter_template
INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES
(81005, 'EP_COMMERCIAL_OFFER_PROLONGATION_INITIATOR', 'Effect. Анализ рынка - поступил запрос на продление срока подачи Коммерческого предложения', NULL, '2024-01-11 16:07:36.710', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(82005, 'EP_COMMERCIAL_OFFER_PROLONGATION_CONTRACTOR', 'Effect. Анализ рынка - Запрос на продление срока подачи коммерческого предложения отправлен', NULL, '2024-01-11 16:07:36.710', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');

--insert into t_variable
INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) VALUES
(83005, '{Дата_время_отправки_запроса_продления_срока}', 'Дата/время отправки запроса продления срока', NULL, '2024-01-11 16:12:02.481', NULL, NULL, NULL),
(84005, '{Срок_продления}', 'Срок продления', NULL, '2024-01-11 16:12:02.481', NULL, NULL, NULL),
(85005, '{Причина_продления}', 'Причина продления срока', NULL, '2024-01-11 16:12:02.481', NULL, NULL, NULL);