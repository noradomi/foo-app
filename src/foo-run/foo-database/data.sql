DROP DATABASE IF EXISTS fooapp;
CREATE DATABASE fooapp;

USE fooapp;

CREATE TABLE `users` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  -- INDEX (`username`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `messages` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `receiver` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` bigint NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `conversation` (`sender`,`receiver`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- create table friends
-- (
--     id       char(36) primary key,
--     user_id  char(36) not null,
--     friend_id char(36) not null,
--     constraint user_fk foreign key (user_id) references users (id),
--     constraint friend_fk foreign key (friend_id) references users (id)
-- );

-- create table conversations
-- (
--     id                char(36) primary key,
--     conservation_name varchar(45),
--     conservation_type enum ('SINGLE', 'GROUP') not null,
--     creator_id          char(36),
--     constraint conversations_owner_id foreign key (owner_id) references users (id)
-- );

-- create table user_conversation
-- (
--     id              char(36) primary key,
--     user_id         char(36) not null,
--     conversation_id char(36) not null,
--     constraint user_conservations_user_id foreign key (user_id) references users (id),
--     constraint user_conservations_conservation_id foreign key (conservation_id) references conservations (id)
-- );

-- create table messages
-- (
--     id              char(36) primary key,
--     user_id         char(36)   not null,
--     conversation_id char(36)   not null,
--     create_date       timestamp not null,
--     message         text      not null
-- );
