/*
Разработка-277-Добавление полей в таблицах администрирования
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/30

*/

--1 "admin".t_question-code

ALTER TABLE "admin".t_question ADD code varchar NULL;
COMMENT ON COLUMN "admin".t_question.code IS 'Уникальный код запроса';

ALTER TABLE "admin".t_question ADD CONSTRAINT t_question_un UNIQUE (code);

UPDATE "admin".t_question
	SET code='PARTICIPATION_INTEREST'
	WHERE id=21003;
UPDATE "admin".t_question
	SET code='WORK_EXPERIENCE'
	WHERE id=22003;
UPDATE "admin".t_question
	SET code='SEND_OFFER'
	WHERE id=23003;

ALTER TABLE "admin".t_question ALTER COLUMN code SET NOT NULL;

--2 "admin".t_answer_version-type

ALTER TABLE "admin".t_answer_version ADD "type" varchar NULL;
COMMENT ON COLUMN "admin".t_answer_version."type" IS 'Тип ответа';

UPDATE "admin".t_answer_version
	SET "type"='POSITIVE'
	WHERE id=26003;
UPDATE "admin".t_answer_version
	SET "type"='POSITIVE'
	WHERE id=28003;
UPDATE "admin".t_answer_version
	SET "type"='POSITIVE'
	WHERE id=24003;
UPDATE "admin".t_answer_version
	SET "type"='NEGATIVE'
	WHERE id=25003;
UPDATE "admin".t_answer_version
	SET "type"='NEGATIVE'
	WHERE id=27003;
UPDATE "admin".t_answer_version
	SET "type"='NEGATIVE'
	WHERE id=29003;

ALTER TABLE "admin".t_answer_version ALTER COLUMN "type" SET NOT NULL;