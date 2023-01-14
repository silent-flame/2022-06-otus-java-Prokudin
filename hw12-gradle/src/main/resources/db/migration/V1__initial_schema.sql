-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

create table address
(
    id     bigint not null primary key,
    street varchar(64)
);

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table client
(
    id         bigint not null primary key,
    name       varchar(50),
    address_id bigint    not null,
    foreign key (address_id) REFERENCES address(id)
);

create table phone
(
    id     bigint not null primary key,
    number varchar(64),
    client_id bigint    not null,
    foreign key (client_id) REFERENCES client(id)
);