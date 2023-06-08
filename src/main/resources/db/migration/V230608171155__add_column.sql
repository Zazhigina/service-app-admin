/*

Разработка-192-Создание таблиц
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/102#note_71663

*/

ALTER TABLE "admin".t_answer_version ADD is_used bool NULL;
COMMENT ON COLUMN "admin".t_answer_version.is_used IS 'Индикатор выбора ответа';
