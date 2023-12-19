/*

Разработка-554-Администрирование. Бэк. Создать скрипт для заполнения производственного календаря в продуктивной системе
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/57
*/

--insert into t_calendar_production

INSERT INTO "admin".t_calendar_production
(id, "year", hour_work_count, month_work_hour_count, create_date, create_user, last_update_date, last_update_user)
VALUES(71005, 2023, 1973, 164.4, '2023-12-19 21:58:15.861', NULL, NULL, NULL);

INSERT INTO "admin".t_calendar_production
(id, "year", hour_work_count, month_work_hour_count, create_date, create_user, last_update_date, last_update_user)
VALUES(72005, 2024, 1979, 164.9, '2023-12-19 21:58:15.906', NULL, NULL, NULL);