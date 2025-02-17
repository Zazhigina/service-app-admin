/*
    Разработка-1062-РЦЗ. Добавление параметра в сервис администрирования
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/159
*/

INSERT INTO "admin".a_param ("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val) values
('INFLATION_RATE', 'Коэффициент инфляции в %', '4', NULL, NULL, NULL, null, NULL);
