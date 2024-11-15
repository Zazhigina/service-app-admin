/*
    Разработка-1092-ПП.Отправка письма о завершении срока ответов на запрос заинтересованности
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/169
*/

INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES
(488005, 'МА_INTEREST_RESULT_ACCEPTING_COMPLETED_INITIATOR', 'Effect. Анализ рынка - завершен срок приема ответов на ЗЗ', NULL, '2024-11-15 12:32:42.119', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(489005, 'МА_INTEREST_RESULT_ACCEPTING_COMPLETED_CONTRACTOR', 'Effect. Анализ рынка - завершен срок ответа на запрос заинтересованности', NULL, '2024-11-15 12:32:42.119', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(490005, 'МА_INTEREST_RESULT_ACCEPTING_COMPLETED_INITIATOR_NOTICE', 'Effect – Поиск поставщиков', NULL, '2024-11-15 12:32:42.119', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(491005, 'МА_INTEREST_RESULT_ACCEPTING_COMING_END_INITIATOR', 'Effect. Анализ рынка - завершается срок приема ответов на ЗЗ', NULL, '2024-11-15 12:32:42.119', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(492005, 'МА_INTEREST_RESULT_ACCEPTING_COMING_END_CONTRACTOR', 'Effect. Анализ рынка - завершается срок ответа на запрос заинтересованности', NULL, '2024-11-15 12:32:42.119', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(493005, 'MA_INTEREST_RESULT_ACCEPTING_COMING_END_INITIATOR_NOTICE', 'Effect – Поиск поставщиков', NULL, '2024-11-15 12:32:42.119', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');