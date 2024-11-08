/*
добавление столбца last_update_date к таблице t_feedback
*/
ALTER TABLE "admin".t_feedback ADD last_update_date timestamp NULL; -- Дата и время изменения;
COMMENT ON COLUMN "admin".t_feedback.last_update_date IS 'Дата и время изменения';