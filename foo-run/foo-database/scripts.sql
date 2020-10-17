CREATE DATABASE `fooapp`;
USE `fooapp`;

-- -----------------------------------------------------
-- Table `fooapp`.`users`
-- -----------------------------------------------------
CREATE TABLE `fooapp`.`users`
(
  `id` CHAR(36) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `username` VARCHAR(30) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `balance` BIGINT NOT NULL,
  `last_updated` BIGINT NOT NULL,
  PRIMARY KEY(`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`messages`
-- -----------------------------------------------------
CREATE TABLE `fooapp`.`messages`
(
  `id` CHAR (36) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `sender` CHAR (36) NOT NULL,
  `receiver` CHAR (36) NOT NULL,
  `message` TEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` BIGINT NOT NULL,
  `message_type` INT DEFAULT 0,
  PRIMARY KEY(`id`),
  INDEX `conversation` (`sender`,`receiver`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`transfers`
-- -----------------------------------------------------
CREATE TABLE `fooapp`.`transfers`
(
  `id` CHAR(36) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `sender` CHAR(36) NOT NULL,
  `receiver` CHAR(36) NOT NULL,
  `amount` BIGINT NOT NULL,
  `description` VARCHAR(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `recorded_time` BIGINT NOT NULL,
  PRIMARY KEY(`id`),
  INDEX `fk_transactions_users1_idx` (`sender` ASC) VISIBLE,
  INDEX `fk_transactions_users2_idx` (`receiver` ASC) VISIBLE,
  CONSTRAINT `fk_transactions_users1` FOREIGN KEY (`sender`) REFERENCES `fooapp`.`users`(`id`),
  CONSTRAINT `fk_transactions_users2` FOREIGN KEY (`receiver`) REFERENCES `fooapp`.`users`(`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`account_logs`
-- -----------------------------------------------------
CREATE TABLE `fooapp`.`account_logs`
(
  `id` CHAR(36) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `user_id` CHAR(36) NOT NULL,
  `transfer_id` CHAR(36) NOT NULL,
  `balance` BIGINT NOT NULL,
  `transfer_type` INT NOT NULL,
  `recorded_time` BIGINT NOT NULL,
  PRIMARY KEY(`id`),
  INDEX `fk_transaction_logs_users_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_transaction_logs_transactions1_idx` (`transfer_id` ASC) VISIBLE,
  CONSTRAINT `fk_transaction_logs_users` FOREIGN KEY (`user_id`) REFERENCES `fooapp`.`users`(`id`),
  CONSTRAINT `fk_transaction_logs_transactions1` FOREIGN KEY (`transfer_id`) REFERENCES `fooapp`.`transfers`(`id`)
)ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`notifications`
-- -----------------------------------------------------
CREATE TABLE `fooapp`.`notifications`
(
  `id` CHAR(36) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `user_id` CHAR(36) NOT NULL,
  `sender` CHAR(36) NOT NULL,
  `amount` BIGINT NOT NULL,
  `message` VARCHAR (120) NOT NULL,
  `recorded_time` BIGINT NOT NULL,
  `notification_type` INT NOT NULL,
  `status` INT DEFAULT (0),
  PRIMARY KEY (`id`),
  INDEX `fk_transfer_reminds_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_transfer_reminds_users1` FOREIGN KEY (`user_id`) REFERENCES `fooapp`.`users`(`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`friends`
-- -----------------------------------------------------
CREATE TABLE `fooapp`.`friends`
(
  `id` CHAR(36) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `user_id` CHAR(36) NOT NULL,
  `friend_id` CHAR(36) NOT NULL,
  `unread_messages` INT NULL,
  `last_message` TEXT NULL,
  PRIMARY KEY(`id`),
  INDEX `fk_friends_users1_idx`(`users_id` ASC) VISIBLE,
  INDEX `fk_friends_users2_idx`(`friend_id` ASC) VISIBLE,
  CONSTRAINT `fk_friends_users1` FOREIGN KEY (`users_id`) REFERENCES `fooapp`.`users`(`id`),
  CONSTRAINT `fk_friends_users2` FOREIGN KEY (`friend_id`) REFERENCES `fooapp`.`users`(`id`)
)ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

