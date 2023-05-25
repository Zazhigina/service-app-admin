/*
Разработка-193-Создание таблицы
#96

Саламатин Антон:
Прошу для поля letter_sample таблицы t_letter_template изменить формат на int8. По просьбе бэк-енд разработчика.

*/

ALTER TABLE "admin".t_letter_template ALTER COLUMN letter_sample TYPE int8 USING letter_sample::int8;
