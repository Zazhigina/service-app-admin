/*
Разработка-16-Формирование вопросов поставщикам МТР - доработка БД
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/65
*/

update "admin".t_question
set owner = 'SERVICE_PRODUCT'
where owner is null;
