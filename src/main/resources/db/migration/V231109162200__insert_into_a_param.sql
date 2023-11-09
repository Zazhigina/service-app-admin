/*

Разработка-454-запуск фонового задания в соответствии с параметром в администрировании
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/40
*/

INSERT INTO "admin".a_param ("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val) 
VALUES('SHEDULLER.MAILING_ALLOWED', 'Разрешить рассылку', '0', '2023-11-09 16:27:22.862', NULL, NULL, NULL, NULL);