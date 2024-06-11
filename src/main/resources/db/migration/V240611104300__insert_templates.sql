/*
Разработка-970-Создание шаблонов писем для настройки заместителей
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/105
*/

-- Добавление шаблона назначения и удаления заместителя
INSERT INTO admin.t_letter_template
(letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES
    ('SETTING_DEPUTY_APPOINTMENT', 'Effect – Назначение заместителем', NULL, '2024-06-10 15:07:58.839', NULL, NULL, NULL, 'TEMPLATE', 'html', 'DRAFT'),
    ('SETTING_DEPUTY_DELETE', 'Effect – Удаление настройки заместителя', NULL, '2024-06-10 15:07:59.839', NULL, NULL, NULL, 'TEMPLATE', 'html', 'DRAFT');

-- Добавление параметров для шаблонов
INSERT INTO admin.t_variable
(name, description, default_val, create_date, create_user, last_update_date, last_update_user)
VALUES
    ('{Имя_отчество_заместителя}', 'Имя и отчество заместителя', NULL, '2024-06-10 15:21:27.402', NULL, NULL, NULL),
    ('{Имя_отчество_инициатора}', 'Имя и отчество инициатора', NULL, '2024-06-10 15:21:27.402', NULL, NULL, NULL),
    ('{Электронная_почта_инициатора}', 'Электронная почта инициатора', NULL, '2024-06-10 15:21:27.402', NULL, NULL, NULL),
    ('{Наименование_сервиса}', 'Наименование сервиса', NULL, '2024-06-10 15:21:27.402', NULL, NULL, NULL),
    ('{Предмет_закупки}', 'Предмет закупки', NULL, '2024-06-10 15:21:27.402', NULL, NULL, NULL),
    ('{Период_действия_замещения}', 'Период действия замещения', NULL, '2024-06-10 15:21:27.402', NULL, NULL, NULL),
    ('{Ссылка_сервиса}', 'Ссылка сервиса', NULL, '2024-06-10 15:21:27.402', NULL, NULL, NULL);