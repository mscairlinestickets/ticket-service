ALTER TABLE airlines
    ADD COLUMN created_by varchar(255) ;

ALTER TABLE airlines
    ADD COLUMN last_modified_by varchar(255) ;


ALTER TABLE aircraft
    ADD COLUMN created_by varchar(255) ;

ALTER TABLE aircraft
    ADD COLUMN last_modified_by varchar(255) ;

ALTER TABLE flight
    ADD COLUMN created_by varchar(255) ;

ALTER TABLE flight
    ADD COLUMN last_modified_by varchar(255) ;