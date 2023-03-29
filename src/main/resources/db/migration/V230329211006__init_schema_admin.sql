CREATE SCHEMA IF NOT EXISTS admin;
COMMENT ON SCHEMA admin IS 'Ведение настроек';



CREATE SEQUENCE IF NOT EXISTS admin.global_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 10
	NO CYCLE;
COMMENT ON SEQUENCE admin.global_id_seq IS 'Общая последовательность';



CREATE OR REPLACE FUNCTION admin.get_id()
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE 
    v_seq_id bigint = NULL;
    v_prefix varchar(3) = '023';
BEGIN
    SELECT nextval('admin.global_id_seq') || v_prefix AS id INTO v_seq_id;
    RETURN v_seq_id;
END;
$function$
;
COMMENT ON FUNCTION admin.get_id() IS 'Общая функция для получения ID';



CREATE OR REPLACE FUNCTION admin.fn_before_row()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
BEGIN
    -- INSERT 
    IF TG_OP = 'INSERT' THEN
    	IF to_jsonb(NEW) ? 'create_date' THEN 
            IF NEW.create_date IS NULL THEN 
                NEW.create_date = current_timestamp;
            END IF;
        END IF;
    END IF;

    -- UPDATE 
    IF TG_OP = 'UPDATE' THEN
    	IF to_jsonb(NEW) ? 'last_update_date' THEN 
        	NEW.last_update_date = current_timestamp;
        END IF;
    END IF;

    RETURN NEW;

EXCEPTION WHEN OTHERS THEN
    RETURN NEW;
END;
$function$
;
COMMENT ON FUNCTION admin.fn_before_row() IS 'Триггерная функция для заполнения атрибутов CREATE_DATE, LAST_UPDATE_DATE';
--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 15.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: a_param; Type: TABLE; Schema: admin; Owner: mirror
--

CREATE TABLE admin.a_param (
    key character varying(50) NOT NULL,
    name character varying(1000) NOT NULL,
    val character varying(50),
    create_date timestamp without time zone,
    create_user character varying(100),
    last_update_date timestamp without time zone,
    last_update_user character varying(100),
    default_val character varying(50)
);


ALTER TABLE admin.a_param OWNER TO mirror;

--
-- Name: TABLE a_param; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON TABLE admin.a_param IS 'Ведение параметров приложения';


--
-- Name: COLUMN a_param.key; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON COLUMN admin.a_param.key IS 'Первичный ключ';


--
-- Name: COLUMN a_param.name; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON COLUMN admin.a_param.name IS 'Наименование';


--
-- Name: COLUMN a_param.val; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON COLUMN admin.a_param.val IS 'Значение';


--
-- Name: COLUMN a_param.create_date; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON COLUMN admin.a_param.create_date IS 'Дата и время создания';


--
-- Name: COLUMN a_param.create_user; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON COLUMN admin.a_param.create_user IS 'Автор создания';


--
-- Name: COLUMN a_param.last_update_date; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON COLUMN admin.a_param.last_update_date IS 'Дата и время изменения';


--
-- Name: COLUMN a_param.last_update_user; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON COLUMN admin.a_param.last_update_user IS 'Автор изменения';


--
-- Name: COLUMN a_param.default_val; Type: COMMENT; Schema: admin; Owner: mirror
--

COMMENT ON COLUMN admin.a_param.default_val IS 'Значение по умолчанию';


--
-- Name: a_param a_param_pk; Type: CONSTRAINT; Schema: admin; Owner: mirror
--

ALTER TABLE ONLY admin.a_param
    ADD CONSTRAINT a_param_pk PRIMARY KEY (key);


--
-- Name: a_param tr_before_row; Type: TRIGGER; Schema: admin; Owner: mirror
--

CREATE TRIGGER tr_before_row BEFORE INSERT OR UPDATE ON admin.a_param FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();


--
-- PostgreSQL database dump complete
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 15.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: a_param; Type: TABLE DATA; Schema: admin; Owner: mirror
--

COPY admin.a_param (key, name, val, create_date, create_user, last_update_date, last_update_user, default_val) FROM stdin;
SEARCH_BY_OKVED.MAX_CONTRACTOR	Поиск по ОКВЭД2, максимальное число уч-ков в выборке	65	\N	\N	2023-03-24 12:34:14.299432	mirror_adm	100
SEARCH_BY_SERVICE.MIN_THRESHOLD	Поиск по услуге, минимальный порог в % для поиска уч-ков	80	\N	\N	2023-03-06 15:44:28.728787	test_adm_user	65
SEARCH_BY_SERVICE.MAX_CONTRACTOR	Поиск по услуге, максимальное число уч-ков в выборке	102	\N	\N	2023-03-14 14:21:32.563417	test_adm_user	100
SEARCH_BY_OKPD.MIN_THRESHOLD	Поиск по ОКПД2 минимальный порог в % для поиска уч-ков	64	\N	\N	2023-03-14 14:24:09.378113	test_adm_user	65
SEARCH_BY_SUBJECT.MAX_CONTRACTOR	Поиск по предмету закупок, максимальное число уч-ков в выборке	101	\N	\N	2023-03-14 15:04:39.689273	test_adm_user	100
SEARCH_BY_SUBJECT.MIN_THRESHOLD	Поиск по предмету закупок, минимальный порог в % для поиска уч-ков	73	\N	\N	2023-03-15 13:50:37.792077	test_adm_user	65
SEARCH_BY_OKPD.MAX_CONTRACTOR	Поиск по ОКПД2, максимальное число уч-ков в выборке	92	\N	\N	2023-03-15 13:50:51.327435	test_adm_user	100
SEARCH_BY_OKVED.MIN_THRESHOLD	Поиск по ОКВЭД2, минимальный порог в % для поиска уч-ков	65	\N	\N	2023-03-23 12:08:43.344747	test_adm_user	65
\.


--
-- PostgreSQL database dump complete
--

