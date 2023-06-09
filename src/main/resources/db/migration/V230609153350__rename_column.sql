/*

Разработка-192-Создание таблиц
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/102#note_71986

*/

ALTER TABLE "admin".t_answer_version RENAME COLUMN question_number TO order_no;
ALTER TABLE "admin".t_answer_version ALTER COLUMN order_no TYPE int4 USING order_no::int4;
