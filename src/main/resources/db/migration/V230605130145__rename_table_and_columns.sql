/*

Разработка-193-Создание таблицы t_letter_template_variables в репозитории ADMIN
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/103#note_70766

*/


ALTER TABLE "admin".t_letter_template_variables RENAME TO t_letter_template_variable;
ALTER TABLE "admin".t_letter_template_variable RENAME COLUMN variable TO "name";
ALTER TABLE "admin".t_letter_template_variable RENAME COLUMN variable_name TO val;
