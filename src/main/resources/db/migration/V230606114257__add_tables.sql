-- "admin".t_question definition

-- Drop table

-- DROP TABLE "admin".t_question;

CREATE TABLE "admin".t_question (
	id int8 NOT NULL DEFAULT admin.get_id(), -- Первичный ключ
	"name" varchar NULL, -- Наименование
	order_no int4 NULL, -- Номер вопроса
	actual_to timestamp NULL, -- Срок действия
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_question_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "admin".t_question IS 'Преднастроенный вопрос';

-- Column comments

COMMENT ON COLUMN "admin".t_question.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_question."name" IS 'Наименование';
COMMENT ON COLUMN "admin".t_question.order_no IS 'Номер вопроса';
COMMENT ON COLUMN "admin".t_question.actual_to IS 'Срок действия';
COMMENT ON COLUMN "admin".t_question.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_question.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_question.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_question.last_update_user IS 'Автор изменения';

-- Table Triggers

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_question FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();


-- "admin".t_answer_version definition

-- Drop table

-- DROP TABLE "admin".t_answer_version;

CREATE TABLE "admin".t_answer_version (
	id int8 NOT NULL DEFAULT admin.get_id(), -- Первичный ключ
	question_id int8 NOT NULL, -- Преднастроенный вопрос
	"name" varchar NULL, -- Наименование
	question_number varchar NULL, -- Номер варианта ответа
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_answer_version_pk PRIMARY KEY (id),
	CONSTRAINT t_answer_version_fk FOREIGN KEY (question_id) REFERENCES "admin".t_question(id)
);
COMMENT ON TABLE "admin".t_answer_version IS 'Вариант ответа на вопрос';

-- Column comments

COMMENT ON COLUMN "admin".t_answer_version.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_answer_version.question_id IS 'Преднастроенный вопрос';
COMMENT ON COLUMN "admin".t_answer_version."name" IS 'Наименование';
COMMENT ON COLUMN "admin".t_answer_version.question_number IS 'Номер варианта ответа';
COMMENT ON COLUMN "admin".t_answer_version.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_answer_version.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_answer_version.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_answer_version.last_update_user IS 'Автор изменения';

-- Table Triggers

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_answer_version FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();