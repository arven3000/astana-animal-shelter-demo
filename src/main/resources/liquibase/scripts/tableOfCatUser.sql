-- liquibase formatted sql

-- changeSet nakremneva:1
create table IF NOT EXISTS cat_users
(
    data_time_of_pet timestamp,
    role             varchar(255),
    id               bigint primary key,
    pet_id           bigint references pets(id) on delete set null
);

alter table cat_users
    owner to postgres;