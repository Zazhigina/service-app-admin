/*
Разработка-16-Формирование вопросов поставщикам МТР - доработка БД
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/65
*/

ALTER TABLE "admin".t_question ADD owner varchar(30) NULL;
COMMENT ON COLUMN "admin".t_question.owner IS 'Владелец, enum';
