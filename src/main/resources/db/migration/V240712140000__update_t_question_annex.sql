/*
Разработка-910-Добавление требований/файлов к каждому вопросу запроса заинтересованности.
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/115
*/

update "admin".t_question
set annex = 'COMMENT'
where owner = 'SERVICE_INTEREST_REQUEST'
and (order_no = 1 or order_no = 3);

update "admin".t_question
set annex = 'FILE'
where owner = 'SERVICE_INTEREST_REQUEST'
and order_no = 2;
