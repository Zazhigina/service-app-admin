-- "admin".t_monitoring

-- Drop table

-- DROP TABLE "admin".t_monitoring;

CREATE TABLE "admin".t_monitoring (
    id BIGINT DEFAULT admin.get_id() NOT NULL, -- ID
    service_name varchar(50) NULL, -- Имя сервиса
    url varchar(100) NOT NULL, -- Адрес запроса
    summary varchar(255) NULL, -- Краткое сожержание
    is_active BOOLEAN DEFAULT TRUE NOT NULL, -- Флаг актуальности записи
    CONSTRAINT t_monitoring_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE "admin".t_monitoring IS 'Мониторинг сервисов';

-- Column comments

COMMENT ON COLUMN "admin".t_monitoring.id IS 'ID';
COMMENT ON COLUMN "admin".t_monitoring.service_name IS 'Наименование сервиса';
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
    service_name varchar(50) NULL, -- Имя сервиса
    url varchar(100) NULL, -- Адрес запроса
    summary varchar(255) NULL, -- Краткое сожержание
    result_check varchar(50) NULL, -- Результат проверки
    create_date timestamp NULL, -- Дата и время создания
    deleted BOOLEAN DEFAULT TRUE NOT NULL, -- Флаг удаления записи
    CONSTRAINT t_monitoring_statistics_pk PRIMARY KEY (id)
);

COMMENT ON TABLE "admin".t_monitoring_statistics IS 'Файл проверок мониторинга';

-- Column comments

COMMENT ON COLUMN "admin".t_monitoring_statistics.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_monitoring_statistics.service_name IS 'Наименование сервиса';
COMMENT ON COLUMN "admin".t_monitoring_statistics.url IS 'Адрес запроса';
COMMENT ON COLUMN "admin".t_monitoring_statistics.summary IS 'Краткое сожержание';
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
CREATE INDEX idx_monitoring_stats_composite ON admin.t_monitoring_statistics (url, create_date DESC);
CREATE INDEX idx_monitoring_url ON admin.t_monitoring(url);
CREATE INDEX idx_monitoring_deleted ON admin.t_monitoring_statistics (url, deleted, create_date DESC);


