/*
    DEV-168-SRV | ebase | Параметры для инструкций ebase, добавление ebase в вопросы-ответы
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/187
*/

INSERT INTO "admin".t_letter_template
    (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES
    (517005, 'EBASE_USER_GUIDE_READ', 'Инструкция по просмотру данных в Базе опыта', NULL, '2025-03-11 17:45:01.176', NULL, NULL, NULL, 'DOCUMENT', 'pdf', 'DRAFT'),
    (518005, 'EBASE_USER_GUIDE_CRUD', 'Инструкция по ведению данных в Базе опыта', NULL, '2025-03-11 17:45:01.274', NULL, NULL, NULL, 'DOCUMENT', 'pdf', 'DRAFT'),
    (519005, 'EBASE_USER_GUIDE_ADM', 'Инструкция для Администратора Базы опыта', NULL, '2025-03-11 17:45:01.320', NULL, NULL, NULL, 'DOCUMENT', 'pdf', 'DRAFT');