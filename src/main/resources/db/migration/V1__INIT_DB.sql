create sequence hibernate_sequence start 1 increment 1;

create table message (
    id int4 not null,
    filename varchar(255),
    tag varchar(255),
    text varchar(2048) not null,
    author_id int4,
    primary key (id));

create table user_role (
    user_id int4 not null,
    roles varchar(255));
create table usr (
    id  int4 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id));

create table usr_message (
    user_id int4 not null,
    message_id int4 not null);

alter table usr_message add constraint usr_message_message_fk unique (message_id);
alter table message add constraint message_author_fk foreign key (author_id) references usr;
alter table user_role add constraint user_role_user_id_fk foreign key (user_id) references usr;
alter table usr_message add constraint usr_message_message_id foreign key (message_id) references message;
alter table usr_message add constraint usr_message_user_id foreign key (user_id) references usr;