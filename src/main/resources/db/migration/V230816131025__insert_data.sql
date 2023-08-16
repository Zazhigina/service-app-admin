/*
Разработка-999-Добавление и изменение записей в таблицы схемы admin
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/28

*/

INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) 
    VALUES(31005, '{Максимальная_дата_время_отправки_запроса}', 'Максимальная дата и время отправки по запросу', NULL, '2023-08-16 13:12:46.177', NULL, NULL, NULL);