DROP DATABASE IF EXISTS fooapp;
CREATE DATABASE fooapp;

USE fooapp;

CREATE TABLE `users` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`username`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `messages` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `receiver_id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_date` bigint NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`sender_id`,`receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `users` VALUES ('1408f25f-fe9b-4b69-b1f5-d0b6462982db','shank','$2a$05$v1s9/q8kUHNN5nwPsUkm9.pEVx95BJONvm.V.1KiOZ4QFz8uyvSP.','Shanks Red-Haired'),('29e940e0-209e-497e-96d4-757fa7e068ac','sengoku','$2a$05$kdPcdbowyHV/6WOyqqzeb..SrCX0epu4wRG4uKNMxqttXL1aQpIAO','Sengoku'),('43deed96-b2cc-4ae8-8d8f-3256e3f0c851','golder','$2a$05$5V8ctNujQnpiUfN3yNwrUuP6h1tIPGwDSA.pQrKXYM0s01Jcd7wJa','Gol D. Roger'),('880d6cbb-eeae-42e4-99d5-1a950a83ef60','sasuke','$2a$05$b1Iqo.9VrFjXmgXuYKAGCOFczNAd2do2cmyx.jDdL.QKpElmRN2we','Sasuke'),('8b69b0ad-22ab-4182-9549-81ecc18c6b2a','luffy','$2a$05$/v5ItVcA40APaktiERyzcu5ZMwTgzzDi9iZVfnfnM05b34YUrTBqa','Luffy'),('a1ae07bb-a4e6-466d-b810-d00c79ffb2de','naruto','$2a$05$cPdP70KVJfVqJ/wD3y2nFOAisVKprQXipB/o8WQ/WCi8cY1.Y8sMO','Naruto'),('b045aa81-5433-41ca-93e7-630141858ca1','yagami','$2a$05$WbZzQ0HGLo7fF432j5wobe7cWS1EAQOPm4fYyIinTZQ3j/l5toPky','Light Yagami'),('ca5db011-67a4-4aff-8ffa-f379a6c82f12','kuroko','$2a$05$G1pU8L2nNFyfLFq6/wm69eH3pMQk0jQ.vbOTD3IZGiVw7pk/Csdz.','Tetsuya Kuroko'),('e76a2741-e891-45b1-b3ff-acd5a4e4d506','saitama','$2a$05$OfWoLG9UMHEX5EqG87gfSuiG/Bobp0LuZ4/hpPnmHQhkeSGMfvfDy','Saitama'),('fef6d42d-c2bc-4b06-83cd-ce19590cfbe8','nami','$2a$05$OIqZjv1NlkeNOxBzHFKJK.vUJIHkOS8Fzmztrue991QoQxZNi7qaS','Nami');

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
