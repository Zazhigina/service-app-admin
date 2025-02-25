/*
Разработка-1109-ПП. Новая переменная для рассылки писем инициаторам о завершении приема ответов по ЗЗ
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/176
*/

INSERT INTO "admin".t_variable 
(id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) values
(497005, '{Количество_поставщиков_отправлен_запрос}', 'Количество поставщиков, которым отправлен запрос', NULL, '2024-12-06 12:55:08.837', NULL, NULL, NULL);