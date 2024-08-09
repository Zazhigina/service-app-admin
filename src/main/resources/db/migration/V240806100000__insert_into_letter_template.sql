/*
Разработка-1028-Поиск. Список избранных поставщиков. Создание параметра шаблона загрузки
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/127
*/

-- Добавление шаблона загрузки избранных поставщиков
INSERT INTO admin.t_letter_template
(letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES
    ('FAVOURITE_CONTRACTOR_DOWNLOAD_TEMPLATE', 'Шаблон загрузки избранных поставщиков', NULL, '2024-08-06 09:57:58.839', NULL, NULL, NULL, 'DOCUMENT', NULL, 'DRAFT');