/*
Таблица названий сервисов
*/


--"admin".t_service_date

-- Drop table

-- DROP TABLE "admin".t_service_date;

CREATE TABLE "admin".t_service_date (
    id BIGINT  DEFAULT admin.get_id() NOT NULL, --Первичный ключ
    name varchar NOT NULL, -- Наименование сервиса
    description varchar NOT NULL, -- Описание
    CONSTRAINT t_mirror_service_pk PRIMARY KEY (id),
    CONSTRAINT uk_service_name UNIQUE (name)

);
COMMENT ON TABLE "admin".t_service_date IS 'Файл проверок мониторинга';


-- Column comments

COMMENT ON COLUMN "admin".t_service_date.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_service_date.name IS 'Наименование сервиса';
COMMENT ON COLUMN "admin".t_service_date.description IS 'Описание';


-- Table Triggers

CREATE TRIGGER tr_before_row
    BEFORE INSERT OR UPDATE
    ON admin.t_service_date
    FOR EACH ROW
EXECUTE FUNCTION admin.fn_before_row();

-- insert into t_service_date

INSERT INTO "admin".t_service_date  (name, description)
VALUES('MA', 'Поиск поставщиков'),
('BP', 'База цен'),
('EP', 'Расчет цены закупки'),
('PRCAT', 'Справочник наименований расценок'),
('MTR', 'МТР'),
('ANALYTICS', 'Аналитика'),
('PDD', 'Конструктор документов'),
('USERPROFILE', 'Профиль пользователя'),
('FAVOURITE', 'Избранное'),
('Notification', 'Уведомление'),
('CHATS', 'ЧАТЫ'),
('EBASE', 'Базы отдыха'),
('HELP', 'Сервис помощи');




-- "admin".t_monitoring

-- Drop table

-- DROP TABLE "admin".t_monitoring;

CREATE TABLE "admin".t_monitoring (
    id BIGINT DEFAULT admin.get_id() NOT NULL, -- ID
    service_date_id  BIGINT NOT NULL , -- Индификатор сервиса
    url varchar(100) NOT NULL, -- Адрес запроса
    summary varchar(255) NULL, -- Краткое сожержание
    is_active BOOLEAN DEFAULT TRUE NOT NULL, -- Флаг актуальности записи
    CONSTRAINT t_monitoring_pkey PRIMARY KEY (id),
    CONSTRAINT fk_monitoring_service FOREIGN KEY (service_date_id) REFERENCES admin.t_service_date(id) ON DELETE CASCADE
);
COMMENT ON TABLE "admin".t_monitoring IS 'Мониторинг сервисов';

-- Column comments

COMMENT ON COLUMN "admin".t_monitoring.id IS 'ID';
COMMENT ON COLUMN "admin".t_monitoring.service_date_id IS 'Индификатор сервиса';
COMMENT ON COLUMN "admin".t_monitoring.url IS 'Адрес запроса';
COMMENT ON COLUMN "admin".t_monitoring.summary IS 'Краткое содержание';
COMMENT ON COLUMN "admin".t_monitoring.is_active IS 'Флаг актуальности записи';


-- Trigger: tr_before_row

-- DROP TRIGGER IF EXISTS tr_before_row ON admin.t_monitoring;

CREATE TRIGGER tr_before_row
    BEFORE INSERT OR UPDATE
    ON admin.t_monitoring
    FOR EACH ROW
EXECUTE FUNCTION admin.fn_before_row();

/*
Таблица для хранения данных по Мониторингу сервисов
*/

-- "admin".t_monitoring_statistics definition

-- Drop table

-- DROP TABLE "admin".t_monitoring_statistics;

CREATE TABLE "admin".t_monitoring_statistics (
    id BIGINT NOT NULL DEFAULT admin.get_id(), -- Первичный ключ
    monitoring_id BIGINT NULL ,
    service_name varchar(50) NULL, --имя сервиса
    url varchar(100) NULL, -- Адрес запроса
    result_check varchar(50) NULL, -- Результат проверки
    create_date timestamp NULL, -- Дата и время создания
    deleted BOOLEAN DEFAULT TRUE NOT NULL, -- Флаг удаления записи
    CONSTRAINT t_monitoring_statistics_pk PRIMARY KEY (id),
    CONSTRAINT t_monitoring_statistics_fk FOREIGN KEY (monitoring_id)
        REFERENCES "admin".t_monitoring(id)
        ON DELETE SET NULL
);

COMMENT ON TABLE "admin".t_monitoring_statistics IS 'Файл проверок мониторинга';

-- Column comments

COMMENT ON COLUMN "admin".t_monitoring_statistics.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_monitoring_statistics.monitoring_id IS 'Идентификатор мониторинга';
COMMENT ON COLUMN "admin".t_monitoring_statistics.service_name IS 'Наименование сервиса';
COMMENT ON COLUMN "admin".t_monitoring_statistics.url IS 'Адрес запроса';
COMMENT ON COLUMN "admin".t_monitoring_statistics.result_check IS 'Результат проверки';
COMMENT ON COLUMN "admin".t_monitoring_statistics.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_monitoring_statistics.deleted IS 'Флаг удаления записи';

-- Table Triggers

CREATE TRIGGER tr_before_row
    BEFORE INSERT OR UPDATE
    ON admin.t_monitoring_statistics
    FOR EACH ROW
EXECUTE FUNCTION admin.fn_before_row();


-- Индексы
CREATE INDEX idx_monitoring_stats_composite ON admin.t_monitoring_statistics (monitoring_id, create_date DESC);
CREATE INDEX idx_monitoring_service ON admin.t_monitoring(service_date_id);
CREATE INDEX idx_monitoring_url ON admin.t_monitoring(url);
CREATE INDEX idx_monitoring_deleted ON admin.t_monitoring_statistics (monitoring_id, deleted, create_date DESC);


