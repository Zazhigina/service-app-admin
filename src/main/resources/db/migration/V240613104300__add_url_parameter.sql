/*
Разработка-970-Создание шаблонов писем для настройки заместителей
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/105
*/

-- Добавление параметра с URL для генерации ссылки перехода в сервис
INSERT INTO admin.a_param
(key, name, val, create_date, create_user, last_update_date, last_update_user, default_val)
VALUES('URL_SERVICE_BASE', 'Шаблон URL для генерации ссылки перехода в сервис', NULL, '2024-06-13 10:27:44.140', NULL, NULL, NULL, NULL);