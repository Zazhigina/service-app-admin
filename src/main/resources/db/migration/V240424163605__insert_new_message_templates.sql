/*
Разработка-940-Добавление параметров, переменных в бд для реализации отправки уведомлений о новых сообщений в чатах
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/99
*/

--новые переменные
INSERT INTO "admin".t_variable 
(id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) values
(234005, '{Дата_время_получения_сообщения}', 'Дата и время получения сообщения в чате', NULL, '2024-04-24 16:39:00.398', NULL, NULL, NULL),
(235005, '{Значение_ID_чата}', 'ID чата', NULL, '2024-04-24 16:39:00.398', NULL, NULL, NULL),
(236005, '{Ссылка_инициатора_чат}', 'Ссылка на чат для инициатора', NULL, '2024-04-24 16:39:00.398', NULL, NULL, NULL);

--новый параметр
INSERT INTO "admin".a_param ("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val) 
VALUES('URL_FOR_CHAT_INITIATOR', 'Шаблон URL для генерации ссылки доступа инициатора на чат с поставщиком', 'https://mirror-chat.inlinegroup-c.ru/chats?id=', '2024-04-24 16:39:00.398', NULL, NULL, NULL, NULL);

--новые шаблоны
INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) values
(232005, 'NEW_CONTRACTOR_CHAT_MESSAGE', 'Effect. Анализ рынка – новое сообщение', NULL, '2024-04-24 16:39:00.398', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(233005, 'NEW_INITIATOR_CHAT_MESSAGE', 'Effect. Анализ рынка – новое сообщение от участника', NULL, '2024-04-24 16:39:00.398', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');