/*
Разработка-975-Доработка создания шаблона КП для Общего профиля для интеграции со справочником расценок. Администрирование
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-ep/-/issues/348
*/

DELETE FROM admin.a_param
WHERE key = 'PRCAT_INTEGRATION.START_DATE';