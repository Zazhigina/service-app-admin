/*

@SalamatinA
Сергей, в дополнение к задаче, просьба добавить в таблицу ma.t_request_contractor поле GUID_CO, varchar, 
"GUID для ссылки запроса заинтересованности"

*/

ALTER TABLE "admin".t_letter_template ADD CONSTRAINT t_letter_template_un UNIQUE (letter_type);
