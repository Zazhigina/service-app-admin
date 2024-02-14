/*
    Разработка-38-Реализация ведения данных для справки в сервисе администрирования
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/77
*/

INSERT INTO "admin".t_letter_template
(id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES(101005, 'BP_USER_GUIDE_EDIT', 'Инструкция "Ведение записей Базы цен"', NULL, '2024-02-14 14:32:31.567', NULL, NULL, NULL, 'DOCUMENT', 'pdf', 'DRAFT');
INSERT INTO "admin".t_letter_template
(id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES(102005, 'BP_USER_GUIDE_VIEW', 'Инструкция "Просмотр записей Базы цен"', NULL, '2024-02-14 14:32:31.578', NULL, NULL, NULL, 'DOCUMENT', 'pdf', 'DRAFT');