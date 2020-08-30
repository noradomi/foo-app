drop database if exists fooapp;
create database fooapp  character set utf8mb4 collate utf8mb4_unicode_ci;
use fooapp;

create table users
(
    id  char(36) primary key,
    username  varchar(30) not null unique,
    password  varchar(60) not null,
    fullname      varchar(45) not null,
    online boolean default true
);

create table friends
(
    id       char(36) primary key,
    user_id  char(36) not null,
    friend_id char(36) not null,
    constraint user_fk foreign key (user_id) references users (id),
    constraint friend_fk foreign key (friend_id) references users (id)
);

create table conversations
(
    id                char(36) primary key,
    conservation_name varchar(45),
    conservation_type enum ('SINGLE', 'GROUP') not null,
    creator_id          char(36),
    constraint conversations_owner_id foreign key (owner_id) references users (id)
);

create table user_conversation
(
    id              char(36) primary key,
    user_id         char(36) not null,
    conversation_id char(36) not null,
    constraint user_conservations_user_id foreign key (user_id) references users (id),
    constraint user_conservations_conservation_id foreign key (conservation_id) references conservations (id)
);

create table messages
(
    id              char(36) primary key,
    user_id         char(36)   not null,
    conversation_id char(36)   not null,
    create_date       timestamp not null,
    message         text      not null
);
