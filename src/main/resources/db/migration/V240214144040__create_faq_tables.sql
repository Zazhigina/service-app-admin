/*
    Разработка-38-Реализация ведения данных для справки в сервисе администрирования
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/77
*/

-- "admin".t_faq_question definition

-- Drop table

-- DROP TABLE "admin".t_faq_question;

CREATE TABLE "admin".t_faq_question (
	id int8 NOT NULL DEFAULT admin.get_id(), -- Первичный ключ
	service_name varchar NOT NULL, -- Наименование сервиса
	"name" varchar NOT NULL, -- Наименование
	question_type varchar NOT NULL, -- Тип вопроса
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_faq_question_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "admin".t_faq_question IS 'FAQ: вопросы';

-- Column comments

COMMENT ON COLUMN "admin".t_faq_question.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_faq_question.service_name IS 'Наименование сервиса';
COMMENT ON COLUMN "admin".t_faq_question."name" IS 'Наименование';
COMMENT ON COLUMN "admin".t_faq_question.question_type IS 'Тип вопроса';
COMMENT ON COLUMN "admin".t_faq_question.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_faq_question.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_faq_question.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_faq_question.last_update_user IS 'Автор изменения';

-- Table Triggers

create trigger tr_before_row before
insert
    or
update
    on
    admin.t_faq_question for each row execute function admin.fn_before_row();


-- "admin".t_faq_answer definition

-- Drop table

-- DROP TABLE "admin".t_faq_answer;

CREATE TABLE "admin".t_faq_answer (
	id int8 NOT NULL DEFAULT admin.get_id(), -- Первичный ключ
	question_id int8 NOT NULL, -- Идентификатор вопроса
	"name" varchar NULL, -- Наименование
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_faq_answer_pk PRIMARY KEY (id),
	CONSTRAINT t_faq_answer_fk FOREIGN KEY (question_id) REFERENCES "admin".t_faq_question(id)
);
COMMENT ON TABLE "admin".t_faq_answer IS 'FAQ: ответ на вопрос';

-- Column comments

COMMENT ON COLUMN "admin".t_faq_answer.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_faq_answer.question_id IS 'Идентификатор вопроса';
COMMENT ON COLUMN "admin".t_faq_answer."name" IS 'Наименование';
COMMENT ON COLUMN "admin".t_faq_answer.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_faq_answer.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_faq_answer.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_faq_answer.last_update_user IS 'Автор изменения';

-- Table Triggers

create trigger tr_before_row before
insert
    or
update
    on
    admin.t_faq_answer for each row execute function admin.fn_before_row();