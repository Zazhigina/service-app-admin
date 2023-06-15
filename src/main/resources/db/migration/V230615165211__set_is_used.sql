/*

Разработка-192-Заполнение вопросов/ответов в БД
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/108#note_72735

*/

UPDATE "admin".t_answer_version tav SET name = 'Да, заинтересованность в участии подтверждаем' WHERE id = 24003 ;
UPDATE "admin".t_answer_version tav SET is_used = TRUE WHERE order_no = 1;
UPDATE "admin".t_answer_version tav SET is_used = FALSE WHERE order_no = 2;
