/*

Разработка-192-Переименование поля is_used в таблице admin.t_answer_version
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/20#note_76971

*/

-- На вский случай копируем данные (для универсальности подобных случаев)
-- Важно: для больших таблиц этого делать нельзя. Необходимо разбить обновление на порции.
UPDATE "admin".t_answer_version SET is_default = is_used WHERE is_default IS NULL AND is_used IS NOT NULL ;
-- Удаляем колонку
ALTER TABLE "admin".t_answer_version DROP COLUMN is_used ;
