create table client
(
    id         bigserial not null primary key,
    name       varchar(50)
);

create table address
(
    id     bigserial not null primary key,
    street varchar(64),
    client_id bigint  not null,
    foreign key (client_id) references client(id)
);

create table phone
(
    id     bigserial not null primary key,
    number varchar(64),
    client_id bigint   not null,
    foreign key (client_id) references client(id)
);