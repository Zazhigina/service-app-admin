/*
Разработка-975-Доработка создания шаблона КП для Общего профиля для интеграции со справочником расценок. Администрирование
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-ep/-/issues/348
*/

INSERT INTO admin.a_param
(key, name, val, create_date, create_user, last_update_date, last_update_user, default_val)
VALUES('PRCAT_INTEGRATION.START_DATE', 'Дата начала использования сервиса Справочник расценок', NULL, '2024-07-31 12:40:06.751', NULL, NULL, NULL, NULL);