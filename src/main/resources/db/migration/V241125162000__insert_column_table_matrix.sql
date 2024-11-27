/*
добавление столбца is_deleted к таблице t_matrix
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/171
*/
ALTER TABLE "admin".t_matrix ADD is_deleted BOOLEAN NOT NULL DEFAULT FALSE;
COMMENT ON COLUMN "admin".t_matrix.is_deleted IS 'Статус удаления';