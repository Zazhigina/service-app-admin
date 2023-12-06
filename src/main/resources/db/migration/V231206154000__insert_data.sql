/*

Разработка-759-Новые параметры в администрировании
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/52
*/

--insert into a_param

INSERT INTO "admin".a_param
("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val)
VALUES('UPDATED_CONTRACTOR_COUNT', 'Поиск поставщиков, кол-во поставщиков обновляемых данными ДП', NULL, '2023-12-06 15:40:15.925', NULL, NULL, NULL, NULL);

INSERT INTO "admin".a_param
("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val)
VALUES('CONTRACTOR_DATA_RELEVANCE_LIMIT_DAYS', 'Количество дней актуальности данных контрагента с момента последнего обновления у дата провайдера', NULL, '2023-12-06 15:40:15.925', NULL, NULL, NULL, NULL);