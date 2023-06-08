/*

Разработка-193-Создание таблицы
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/96#note_71558

*/

ALTER TABLE "admin".t_letter_template DROP CONSTRAINT t_letter_template_fk_1;
ALTER TABLE "admin".t_letter_template ADD CONSTRAINT t_letter_template_fk_1 FOREIGN KEY (type_template) REFERENCES "admin".t_letter_template_type_template_enum("name") ON UPDATE CASCADE;
ALTER TABLE "admin".t_letter_template DROP CONSTRAINT t_letter_template_fk_2;
ALTER TABLE "admin".t_letter_template ADD CONSTRAINT t_letter_template_fk_2 FOREIGN KEY (acceptable_document_format) REFERENCES "admin".t_letter_template_acceptable_document_format_enum("name") ON UPDATE CASCADE;

UPDATE "admin".t_letter_template_type_template_enum tlttte SET description = name, name = 'DOCUMENT' WHERE id = 1 AND name = 'Документ';
UPDATE "admin".t_letter_template_type_template_enum tlttte SET description = name, name = 'TEMPLATE' WHERE id = 2 AND name = 'Шаблон';
