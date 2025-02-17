/*
  Таблица для ведения настроек матрицы Компания организатор / Организатор <> Заказчик / Инициатор
  https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/171
*/

CREATE TABLE admin.t_matrix (
	id int8 NOT NULL DEFAULT admin.get_id(),
	company_code varchar(4) NOT NULL,
	org_code varchar(3) NOT NULL,
	customer_code varchar NOT NULL,
	initiator_code varchar NOT NULL,
	create_date timestamp NULL,
	create_user varchar NULL,
	last_update_date timestamp NULL,
	last_update_user varchar NULL,
	CONSTRAINT t_matrix_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE admin.t_matrix IS 'Настройка матрицы Компания организатор / Организатор <> Заказчик / Инициатор"';

-- Column comments

COMMENT ON COLUMN admin.t_matrix.id IS 'Первичный ключ';
COMMENT ON COLUMN admin.t_matrix.company_code IS 'Код компании организатора';
COMMENT ON COLUMN admin.t_matrix.customer_code IS 'Код заказчика';
COMMENT ON COLUMN admin.t_matrix.initiator_code IS 'Код инициатора';
COMMENT ON COLUMN admin.t_matrix.org_code IS 'Код организатора';
COMMENT ON COLUMN admin.t_matrix.create_date IS 'Дата и время создания';
COMMENT ON COLUMN admin.t_matrix.create_user IS 'Автор создания';
COMMENT ON COLUMN admin.t_matrix.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN admin.t_matrix.last_update_user IS 'Автор изменения';

-- Table Triggers

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_matrix FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();