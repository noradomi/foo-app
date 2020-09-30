DROP DATABASE IF EXISTS `fooapp`;
CREATE DATABASE `fooapp`;
USE `fooapp`
;

-- -----------------------------------------------------
-- Table `fooapp`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fooapp`.`users` ;

CREATE TABLE
IF NOT EXISTS `fooapp`.`users`
(
  `id` CHAR
(36) CHARACTER
SET 'utf8mb4'
COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `username` VARCHAR
(30) NOT NULL,
  `password` VARCHAR
(60) NOT NULL,
  `name` VARCHAR
(45) NOT NULL,
  `balance` BIGINT NOT NULL,
  `last_updated` BIGINT NOT NULL,
  PRIMARY KEY
(`id`),
  UNIQUE INDEX `username_UNIQUE`
(`username` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER
SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fooapp`.`messages` ;

CREATE TABLE
IF NOT EXISTS `fooapp`.`messages`
(
  `id` CHAR
(36) CHARACTER
SET 'utf8mb4'
COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `sender` CHAR
(36) NULL,
  `receiver` CHAR
(36) NULL,
  `message` TEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` BIGINT NULL,
  PRIMARY KEY
(`id`))
ENGINE = InnoDB
DEFAULT CHARACTER
SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`transfers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fooapp`.`transfers` ;

CREATE TABLE
IF NOT EXISTS `fooapp`.`transfers`
(
  `id` CHAR
(36) CHARACTER
SET 'utf8mb4'
COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `sender` CHAR
(36) NOT NULL,
  `receiver` CHAR
(36) NOT NULL,
  `amount` BIGINT NOT NULL,
  `description` VARCHAR
(120) COLLATE utf8mb4_unicode_ci NULL,
  `recorded_time` BIGINT NOT NULL,
  PRIMARY KEY
(`id`),
  INDEX `fk_transactions_users1_idx`
(`sender` ASC) VISIBLE,
  INDEX `fk_transactions_users2_idx`
(`receiver` ASC) VISIBLE,
  CONSTRAINT `fk_transactions_users1`
    FOREIGN KEY
(`sender`)
    REFERENCES `fooapp`.`users`
(`id`)
    ON
DELETE NO ACTION
    ON
UPDATE NO ACTION,
  CONSTRAINT `fk_transactions_users2`
    FOREIGN KEY
(`receiver`)
    REFERENCES `fooapp`.`users`
(`id`)
    ON
DELETE NO ACTION
    ON
UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER
SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`account_logs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fooapp`.`account_logs` ;

CREATE TABLE
IF NOT EXISTS `fooapp`.`account_logs`
(
  `id` CHAR
(36) CHARACTER
SET 'utf8mb4'
COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `user_id` CHAR
(36) NOT NULL,
  `transfer_id` CHAR
(36) NOT NULL,
  `balance` BIGINT NOT NULL,
  `transfer_type` ENUM
('send', 'receive') NULL,
  `recorded_time` BIGINT NOT NULL,
  PRIMARY KEY
(`id`),
  INDEX `fk_transaction_logs_users_idx`
(`user_id` ASC) VISIBLE,
  INDEX `fk_transaction_logs_transactions1_idx`
(`transfer_id` ASC) VISIBLE,
  CONSTRAINT `fk_transaction_logs_users`
    FOREIGN KEY
(`user_id`)
    REFERENCES `fooapp`.`users`
(`id`)
    ON
DELETE NO ACTION
    ON
UPDATE NO ACTION,
  CONSTRAINT `fk_transaction_logs_transactions1`
    FOREIGN KEY
(`transfer_id`)
    REFERENCES `fooapp`.`transfers`
(`id`)
    ON
DELETE NO ACTION
    ON
UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER
SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`notifications`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fooapp`.`notifications` ;

CREATE TABLE
IF NOT EXISTS `fooapp`.`notifications`
(
  `id` CHAR
(36) CHARACTER
SET 'utf8mb4'
COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `user_id` CHAR
(36) NOT NULL,
  `sender` CHAR
(36) NULL,
  `amount` BIGINT NULL,
  `message` VARCHAR
(120) NULL,
  `recorded_time` BIGINT NULL,
  `notification_type` ENUM
('reminded', 'received') NULL,
  `status` ENUM
('finished', 'unfinished') NULL,
  PRIMARY KEY
(`id`),
  INDEX `fk_transfer_reminds_users1_idx`
(`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_transfer_reminds_users1`
    FOREIGN KEY
(`user_id`)
    REFERENCES `fooapp`.`users`
(`id`)
    ON
DELETE NO ACTION
    ON
UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER
SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `fooapp`.`friends`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fooapp`.`friends` ;

CREATE TABLE
IF NOT EXISTS `fooapp`.`friends`
(
  `id` CHAR
(36) CHARACTER
SET 'utf8mb4'
COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `users_id` CHAR
(36) NOT NULL,
  `friend_id` CHAR
(36) NOT NULL,
  `unread_messages` INT NULL,
  `last_message` TEXT NULL,
  PRIMARY KEY
(`id`),
  INDEX `fk_friends_users1_idx`
(`users_id` ASC) VISIBLE,
  INDEX `fk_friends_users2_idx`
(`friend_id` ASC) VISIBLE,
  CONSTRAINT `fk_friends_users1`
    FOREIGN KEY
(`users_id`)
    REFERENCES `fooapp`.`users`
(`id`)
    ON
DELETE NO ACTION
    ON
UPDATE NO ACTION,
  CONSTRAINT `fk_friends_users2`
    FOREIGN KEY
(`friend_id`)
    REFERENCES `fooapp`.`users`
(`id`)
    ON
DELETE NO ACTION
    ON
UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER
SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
