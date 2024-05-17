/*
Разработка-940-Создать новые параметры для задачи по рассылке уведомлений о новых сообщениях в чате
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/108
*/

--новые параметры для настройки запуска фонового задания в чатах
INSERT INTO "admin".a_param ("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val) values
('MESSAGE_SENDING_PERIOD', 'Периодичность запуска рассылки писем по новым сообщениям в чате (в минутах)', '60', NULL, NULL, NULL, null, '60'),
('USER_INACTIVITY_PERIOD', 'Период бездействия пользователя в системах (в минутах)', '60', NULL, NULL, NULL, null, '60');
