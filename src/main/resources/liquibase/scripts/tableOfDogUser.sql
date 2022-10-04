-- liquibase formatted sql

-- changeSet nakremneva:1
create table  IF NOT EXISTS dog_users
(
    data_time_of_pet timestamp,
    role             varchar(255),
    id               bigint primary key,
    pet_id           bigint references pets(id) on delete set null
);

alter table dog_users
    owner to postgres;
