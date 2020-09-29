-- MySQL Script generated by MySQL Workbench
-- Thứ ba, 29 Tháng 9 Năm 2020 19:09:01 +07
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`users` ;

CREATE TABLE IF NOT EXISTS `mydb`.`users` (
  `id` CHAR(36) NOT NULL,
  `username` VARCHAR(30) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `name` VARCHAR(45) NULL,
  `balance` BIGINT NULL,
  `last_updated` BIGINT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`messages` ;

CREATE TABLE IF NOT EXISTS `mydb`.`messages` (
  `id` CHAR(36) NOT NULL,
  `sender` CHAR(36) NULL,
  `receiver` CHAR(36) NULL,
  `message` TEXT NULL,
  `create_time` BIGINT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`transfers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`transfers` ;

CREATE TABLE IF NOT EXISTS `mydb`.`transfers` (
  `id` CHAR(36) NOT NULL,
  `sender` CHAR(36) NOT NULL,
  `receiver` CHAR(36) NOT NULL,
  `amount` BIGINT NULL,
  `recorded_time` BIGINT NULL,
  `description` VARCHAR(120) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_transactions_users1_idx` (`sender` ASC) VISIBLE,
  INDEX `fk_transactions_users2_idx` (`receiver` ASC) VISIBLE,
  CONSTRAINT `fk_transactions_users1`
    FOREIGN KEY (`sender`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transactions_users2`
    FOREIGN KEY (`receiver`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`account_logs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`account_logs` ;

CREATE TABLE IF NOT EXISTS `mydb`.`account_logs` (
  `id` CHAR(36) NOT NULL,
  `user_id` CHAR(36) NOT NULL,
  `transfer_id` CHAR(36) NOT NULL,
  `balance` BIGINT NULL,
  `transfer_type` ENUM('send', 'receive') NULL,
  `recorded_time` BIGINT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_transaction_logs_users_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_transaction_logs_transactions1_idx` (`transfer_id` ASC) VISIBLE,
  CONSTRAINT `fk_transaction_logs_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transaction_logs_transactions1`
    FOREIGN KEY (`transfer_id`)
    REFERENCES `mydb`.`transfers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`money_notifications`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`money_notifications` ;

CREATE TABLE IF NOT EXISTS `mydb`.`money_notifications` (
  `id` CHAR(36) NOT NULL,
  `user_id` CHAR(36) NOT NULL,
  `sender` CHAR(36) NULL,
  `amount` BIGINT NULL,
  `message` VARCHAR(120) NULL,
  `recorded_time` BIGINT NULL,
  `notification_type` ENUM('reminded', 'received') NULL,
  `status` ENUM('finished', 'unfinished') NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_transfer_reminds_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_transfer_reminds_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`friends`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`friends` ;

CREATE TABLE IF NOT EXISTS `mydb`.`friends` (
  `id` CHAR(36) NOT NULL,
  `users_id` CHAR(36) NOT NULL,
  `friend_id` CHAR(36) NOT NULL,
  `unread_messages` INT NULL,
  `last_message` TEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_friends_users1_idx` (`users_id` ASC) VISIBLE,
  INDEX `fk_friends_users2_idx` (`friend_id` ASC) VISIBLE,
  CONSTRAINT `fk_friends_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_friends_users2`
    FOREIGN KEY (`friend_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
