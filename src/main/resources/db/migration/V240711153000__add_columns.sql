/*
Разработка-910-Добавление требований/файлов к каждому вопросу запроса заинтересованности.
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/115
*/

ALTER TABLE "admin".t_question ADD annex varchar(15) NULL;
COMMENT ON COLUMN "admin".t_question.annex IS 'Приложение к ответу на вопрос';
