/*
добавление столбца file_store к таблице t_feedback
*/
ALTER TABLE "admin".t_feedback ADD file_store BYTEA; -- BLOB BYTEA;
COMMENT ON COLUMN "admin".t_feedback.file_store IS 'Хранение файла';