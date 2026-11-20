-- liquibase formatted sql
-- changeset mylutece:update_db_mylutece-5.0.1-5.1.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key='mylutece.site_property.default_role_keys'
-- comment creates the datastore key of the default roles, read on each login: a missing key is not cached and costs a query on each login (LUT-33302)
INSERT INTO core_datastore(entity_key,entity_value) VALUES('mylutece.site_property.default_role_keys','');
-- changeset mylutece:update_db_mylutece-5.0.1-5.1.0.sql-connections-log-login
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'mylutece_connections_log' AND column_name = 'login'
-- comment stores the login typed in a failed connection: the failures are counted per login as well as per address
ALTER TABLE mylutece_connections_log ADD COLUMN login varchar(100) default NULL;
