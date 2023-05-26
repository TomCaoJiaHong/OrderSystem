-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema a4_mydb402
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema a4_mydb402
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `a4_mydb402` DEFAULT CHARACTER SET utf8 ;
USE `a4_mydb402` ;

-- -----------------------------------------------------
-- Table `a4_mydb402`.`X_Clients402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`X_Clients402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`X_Clients402` (
  `clientID402` INT NOT NULL,
  `clientName402` VARCHAR(45) NOT NULL,
  `clientCity402` VARCHAR(45) NOT NULL,
  `clientPassward402` VARCHAR(45) NOT NULL,
  `moneyOwed402` INT NOT NULL,
  PRIMARY KEY (`clientID402`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`X_POs402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`X_POs402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`X_POs402` (
  `poNo402` INT NOT NULL,
  `dateOfPO402` VARCHAR(45) NOT NULL,
  `status402` VARCHAR(45) NOT NULL,
  `clientID402` INT NOT NULL,
  PRIMARY KEY (`poNo402`),
  INDEX `fk_X_POs402_X_Clients402_idx` (`clientID402` ASC) VISIBLE,
  CONSTRAINT `fk_X_POs402_X_Clients402`
    FOREIGN KEY (`clientID402`)
    REFERENCES `a4_mydb402`.`X_Clients402` (`clientID402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`X_Parts402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`X_Parts402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`X_Parts402` (
  `partNo402` INT NOT NULL,
  `partName402` VARCHAR(45) NOT NULL,
  `currentPrice402` INT NOT NULL,
  `qoh402` INT NOT NULL,
  PRIMARY KEY (`partNo402`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`X_Lines402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`X_Lines402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`X_Lines402` (
  `lineNo402` INT NOT NULL,
  `qty402` INT NOT NULL,
  `priceOrdered402` INT NOT NULL,
  `poNo402` INT NOT NULL,
  `partNo402` INT NOT NULL,
  PRIMARY KEY (`lineNo402`, `poNo402`, `partNo402`),
  INDEX `fk_X_Lines402_X_POs4021_idx` (`poNo402` ASC) VISIBLE,
  INDEX `fk_X_Lines402_X_Parts4021_idx` (`partNo402` ASC) VISIBLE,
  CONSTRAINT `fk_X_Lines402_X_POs4021`
    FOREIGN KEY (`poNo402`)
    REFERENCES `a4_mydb402`.`X_POs402` (`poNo402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_X_Lines402_X_Parts4021`
    FOREIGN KEY (`partNo402`)
    REFERENCES `a4_mydb402`.`X_Parts402` (`partNo402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`Y_Clients402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`Y_Clients402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`Y_Clients402` (
  `clientID402` INT NOT NULL,
  `clientName402` VARCHAR(45) NOT NULL,
  `clientCity402` VARCHAR(45) NOT NULL,
  `clientPassward402` VARCHAR(45) NOT NULL,
  `moneyOwed402` INT NOT NULL,
  PRIMARY KEY (`clientID402`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`Y_POs402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`Y_POs402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`Y_POs402` (
  `poNo402` INT NOT NULL,
  `dateOfPO402` VARCHAR(45) NOT NULL,
  `status402` VARCHAR(45) NOT NULL,
  `clientID402` INT NOT NULL,
  PRIMARY KEY (`poNo402`),
  INDEX `fk_Y_POs402_Y_Clients4021_idx` (`clientID402` ASC) VISIBLE,
  CONSTRAINT `fk_Y_POs402_Y_Clients4021`
    FOREIGN KEY (`clientID402`)
    REFERENCES `a4_mydb402`.`Y_Clients402` (`clientID402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`Y_Parts402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`Y_Parts402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`Y_Parts402` (
  `partNo402` INT NOT NULL,
  `partName402` VARCHAR(45) NOT NULL,
  `currentPrice402` INT NOT NULL,
  `qoh402` INT NOT NULL,
  PRIMARY KEY (`partNo402`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`Y_Lines402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`Y_Lines402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`Y_Lines402` (
  `lineNo402` INT NOT NULL,
  `qty402` INT NOT NULL,
  `priceOrdered402` INT NOT NULL,
  `poNo402` INT NOT NULL,
  `partNo402` INT NOT NULL,
  PRIMARY KEY (`lineNo402`, `poNo402`, `partNo402`),
  INDEX `fk_Y_Lines402_Y_POs4021_idx` (`poNo402` ASC) VISIBLE,
  INDEX `fk_Y_Lines402_Y_Parts4021_idx` (`partNo402` ASC) VISIBLE,
  CONSTRAINT `fk_Y_Lines402_Y_POs4021`
    FOREIGN KEY (`poNo402`)
    REFERENCES `a4_mydb402`.`Y_POs402` (`poNo402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Y_Lines402_Y_Parts4021`
    FOREIGN KEY (`partNo402`)
    REFERENCES `a4_mydb402`.`Y_Parts402` (`partNo402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`Z_Clients402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`Z_Clients402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`Z_Clients402` (
  `clientID402` INT NOT NULL,
  `clientName402` VARCHAR(45) NOT NULL,
  `clientCity402` VARCHAR(45) NOT NULL,
  `clientPassward402` VARCHAR(45) NOT NULL,
  `moneyOwed402` INT NOT NULL,
  PRIMARY KEY (`clientID402`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`Z_POs402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`Z_POs402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`Z_POs402` (
  `poNo402` INT NOT NULL,
  `dateOfPO402` VARCHAR(45) NOT NULL,
  `status402` VARCHAR(45) NOT NULL,
  `clientID402` INT NOT NULL,
  PRIMARY KEY (`poNo402`),
  INDEX `fk_Z_POs402_Z_Clients4021_idx` (`clientID402` ASC) VISIBLE,
  CONSTRAINT `fk_Z_POs402_Z_Clients4021`
    FOREIGN KEY (`clientID402`)
    REFERENCES `a4_mydb402`.`Z_Clients402` (`clientID402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`Z_Lines402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`Z_Lines402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`Z_Lines402` (
  `lineNo402` INT NOT NULL,
  `qty402` INT NOT NULL,
  `priceOrdered402` INT NOT NULL,
  `poNo402` INT NOT NULL,
  `X_partNo402` INT NOT NULL,
  `Y_partNo402` INT NOT NULL,
  `partNo402` INT NOT NULL,
  PRIMARY KEY (`lineNo402`, `poNo402`, `X_partNo402`, `Y_partNo402`),
  INDEX `fk_Z_Lines402_Z_POs4021_idx` (`poNo402` ASC) VISIBLE,
  INDEX `fk_Z_Lines402_X_Parts4021_idx` (`X_partNo402` ASC) VISIBLE,
  INDEX `fk_Z_Lines402_Y_Parts4021_idx` (`Y_partNo402` ASC) VISIBLE,
  CONSTRAINT `fk_Z_Lines402_Z_POs4021`
    FOREIGN KEY (`poNo402`)
    REFERENCES `a4_mydb402`.`Z_POs402` (`poNo402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Z_Lines402_X_Parts4021`
    FOREIGN KEY (`X_partNo402`)
    REFERENCES `a4_mydb402`.`X_Parts402` (`partNo402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Z_Lines402_Y_Parts4021`
    FOREIGN KEY (`Y_partNo402`)
    REFERENCES `a4_mydb402`.`Y_Parts402` (`partNo402`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `a4_mydb402`.`log402`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `a4_mydb402`.`log402` ;

CREATE TABLE IF NOT EXISTS `a4_mydb402`.`log402` (
  `idlog402` INT NOT NULL,
  `company402` VARCHAR(45) NOT NULL,
  `qty402` INT NOT NULL,
  `clientID402` INT NOT NULL,
  `status402` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idlog402`))
ENGINE = InnoDB;

INSERT INTO `a4_mydb402`.`X_Parts402` (`partNo402`, `partName402`, `currentPrice402`, `qoh402`) VALUES ('0', '0', '0', '0');
INSERT INTO `a4_mydb402`.`Y_Parts402` (`partNo402`, `partName402`, `currentPrice402`, `qoh402`) VALUES ('0', '0', '0', '0');

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
