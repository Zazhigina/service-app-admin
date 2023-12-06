/*

Разработка-007-добавление 2-х переменных для рассылки запросов поставщикам МТР
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/51
*/

--insert into t_variable

INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) 
VALUES(61005, '{Дата_начала_проведения_расчета}', 'Дата начала проведения расчета', NULL, '2023-12-06 15:19:15.925', NULL, NULL, NULL);

INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) 
VALUES(62005, '{Дата_окончания_проведения_расчета}', 'Дата окончания проведения расчета', NULL, '2023-12-06 15:19:15.925', NULL, NULL, NULL);

--insert into a_param

INSERT INTO "admin".a_param
("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val)
VALUES('URL_FOR_COMPANY.MTR', 'Шаблон URL для генерации ссылки доступа поставщика MTR', NULL, '2023-12-06 15:19:15.925', NULL, NULL, NULL, NULL);

INSERT INTO "admin".a_param
("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val)
VALUES('REPLY_MESSAGE.URL_MTR', 'Шаблон URL для генерации ссылки доступа инициатора в запрос MTR', NULL, '2023-12-06 15:19:15.925', NULL, NULL, NULL, NULL);
