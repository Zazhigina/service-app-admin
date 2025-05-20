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
    CONSTRAINT t_mirror_service_pk PRIMARY KEY (id)
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
    service_date_id  BIGINT  NOT NULL, -- Индификатор сервиса
    url varchar(100) NOT NULL, -- Адрес запроса
    summary varchar(255) NULL, -- Краткое сожержание
    CONSTRAINT t_monitoring_pkey PRIMARY KEY (id),
    CONSTRAINT fk_monitoring_service FOREIGN KEY (service_date_id) REFERENCES admin.t_service_date(id)
);
COMMENT ON TABLE "admin".t_monitoring IS 'Мониторинг сервисов';

-- Column comments

COMMENT ON COLUMN "admin".t_monitoring.id IS 'ID';
COMMENT ON COLUMN "admin".t_monitoring.service_date_id IS 'Индификатор сервиса';
COMMENT ON COLUMN "admin".t_monitoring.url IS 'Адрес запроса';
COMMENT ON COLUMN "admin".t_monitoring.summary IS 'Краткое содержание';

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
    monitoring_id BIGINT NULL, -- Идентификатор мониторинга
    result_check varchar(100) NULL, -- Результат проверки
    create_date timestamp NULL, -- Дата и время создания
    CONSTRAINT t_monitoring_statistics_pk PRIMARY KEY (id),
    CONSTRAINT t_monitoring_statistics_fk FOREIGN KEY (monitoring_id)
        REFERENCES "admin".t_monitoring(id)
        ON DELETE SET NULL
);

COMMENT ON TABLE "admin".t_monitoring_statistics IS 'Файл проверок мониторинга';

-- Column comments

COMMENT ON COLUMN "admin".t_monitoring_statistics.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_monitoring_statistics.monitoring_id IS 'Идентификатор мониторинга';
COMMENT ON COLUMN "admin".t_monitoring_statistics.result_check IS 'Результат проверки';
COMMENT ON COLUMN "admin".t_monitoring_statistics.create_date IS 'Дата и время создания';


-- Table Triggers

CREATE TRIGGER tr_before_row
    BEFORE INSERT OR UPDATE
    ON admin.t_monitoring_statistics
    FOR EACH ROW
EXECUTE FUNCTION admin.fn_before_row();


/*
Материализованное представление для актуальных данных
*/

CREATE MATERIALIZED VIEW admin.monitoring_with_latest_stats AS
SELECT
    m.*,
    s.name AS service_name,
    ms.result_check AS last_result,
    ms.create_date AS last_check_date
FROM
    admin.t_monitoring m
        JOIN
    admin.t_service_date s ON m.service_date_id = s.id
        LEFT JOIN LATERAL (
        SELECT result_check, create_date
        FROM admin.t_monitoring_statistics
        WHERE monitoring_id = m.id
        ORDER BY create_date DESC
        LIMIT 1
        ) ms ON TRUE;


/*
Функция обновления представления
*/

CREATE OR REPLACE FUNCTION admin.refresh_monitoring_view()
    RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW admin.monitoring_with_latest_stats;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


/*
Триггеры для автоматического обновления представления
*/

CREATE TRIGGER tr_refresh_after_stats_change
    AFTER INSERT OR UPDATE OR DELETE ON admin.t_monitoring_statistics
    FOR EACH STATEMENT EXECUTE FUNCTION admin.refresh_monitoring_view();

CREATE TRIGGER tr_refresh_after_monitoring_change
    AFTER INSERT OR UPDATE OR DELETE ON admin.t_monitoring
    FOR EACH STATEMENT EXECUTE FUNCTION admin.refresh_monitoring_view();

/*
Индексы для оптимизации
*/

CREATE INDEX idx_monitoring_stats ON admin.t_monitoring_statistics(monitoring_id, create_date DESC);
CREATE UNIQUE INDEX idx_monitoring_view ON admin.monitoring_with_latest_stats(id);





