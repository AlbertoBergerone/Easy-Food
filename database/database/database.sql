-- MySQL Script generated by MySQL Workbench
-- 06/06/16 11:48:40
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema inf-5ogruppo5
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema inf-5ogruppo5
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `inf-5ogruppo5` DEFAULT CHARACTER SET utf8_general_ci ;
USE `inf-5ogruppo5` ;

-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`regioni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`regioni` (
  `codRegione` VARCHAR(3) NOT NULL,
  `nomeRegione` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`codRegione`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`province`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`province` (
  `codProvincia` VARCHAR(2) NOT NULL,
  `nomeProvincia` VARCHAR(35) NOT NULL,
  `codRegione` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`codProvincia`),
  INDEX `codRegioni_idx` (`codRegione` ASC),
  CONSTRAINT `codRegioni`
    FOREIGN KEY (`codRegione`)
    REFERENCES `inf-5ogruppo5`.`regioni` (`codRegione`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`comuni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`comuni` (
  `codCatastale` VARCHAR(4) NOT NULL,
  `nomeComune` VARCHAR(30) NOT NULL,
  `cap` INT NOT NULL,
  `codProvincia` VARCHAR(2) NOT NULL,
  PRIMARY KEY (`codCatastale`),
  INDEX `codProvincia_idx` (`codProvincia` ASC),
  CONSTRAINT `codProvincia`
    FOREIGN KEY (`codProvincia`)
    REFERENCES `inf-5ogruppo5`.`province` (`codProvincia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`ristoranti`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`ristoranti` (
  `codRistorante` INT NOT NULL AUTO_INCREMENT,
  `indirizzo` VARCHAR(50) NOT NULL,
  `nomeRistorante` VARCHAR(50) NOT NULL,
  `longitudine` DOUBLE NULL,
  `latitudine` DOUBLE NULL,
  `descrizione` TEXT NULL,
  `codCatastale` VARCHAR(45) NOT NULL,
  `telRistornate` VARCHAR(20) NULL,
  `emailRistorante` VARCHAR(45) NULL,
  PRIMARY KEY (`codRistorante`),
  INDEX `fk_ristoranti_comuni1_idx` (`codCatastale` ASC),
  CONSTRAINT `fk_ristoranti_comuni1`
    FOREIGN KEY (`codCatastale`)
    REFERENCES `inf-5ogruppo5`.`comuni` (`codCatastale`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`utente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`utente` (
  `codUtente` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `cognome` VARCHAR(45) NOT NULL,
  `indirizzo` VARCHAR(45) NULL,
  `emailUtente` VARCHAR(45) NOT NULL,
  `codCatastale` VARCHAR(4) NULL,
  `telUtente` VARCHAR(20) NULL,
  `utenteConfermato` TINYINT(1) NOT NULL DEFAULT 0,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`codUtente`),
  INDEX `fk_utente_comuni1_idx` (`codCatastale` ASC),
  UNIQUE INDEX `emailUtente_UNIQUE` (`emailUtente` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  CONSTRAINT `fk_utente_comuni1`
    FOREIGN KEY (`codCatastale`)
    REFERENCES `inf-5ogruppo5`.`comuni` (`codCatastale`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`proprietari`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`proprietari` (
  `codRistorante` INT NOT NULL,
  `codUtente` INT NOT NULL,
  PRIMARY KEY (`codUtente`, `codRistorante`),
  INDEX `fk_Proprietari_ristoranti1_idx` (`codRistorante` ASC),
  INDEX `fk_Proprietari_utente1_idx` (`codUtente` ASC),
  CONSTRAINT `fk_Proprietari_ristoranti1`
    FOREIGN KEY (`codRistorante`)
    REFERENCES `inf-5ogruppo5`.`ristoranti` (`codRistorante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Proprietari_utente1`
    FOREIGN KEY (`codUtente`)
    REFERENCES `inf-5ogruppo5`.`utente` (`codUtente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`tipologia_locale`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`tipologia_locale` (
  `codTipo` INT NOT NULL AUTO_INCREMENT,
  `Tipo` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`codTipo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`valutazioni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`valutazioni` (
  `codRistorante` INT NOT NULL,
  `codUtente` INT NOT NULL,
  `valutazione` SMALLINT(1) NOT NULL,
  `titoloCommento` VARCHAR(120) NULL,
  `commento` TINYTEXT NULL,
  INDEX `fk_Valutazioni_ristoranti1_idx` (`codRistorante` ASC),
  INDEX `fk_Valutazioni_utente1_idx` (`codUtente` ASC),
  PRIMARY KEY (`codRistorante`, `codUtente`),
  CONSTRAINT `fk_Valutazioni_ristoranti1`
    FOREIGN KEY (`codRistorante`)
    REFERENCES `inf-5ogruppo5`.`ristoranti` (`codRistorante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Valutazioni_utente1`
    FOREIGN KEY (`codUtente`)
    REFERENCES `inf-5ogruppo5`.`utente` (`codUtente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`immagini`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`immagini` (
  `codImmagine` INT NOT NULL AUTO_INCREMENT,
  `path` VARCHAR(255) NOT NULL,
  `isLogo` TINYINT(1) NOT NULL DEFAULT 0,
  `codRistorante` INT NOT NULL,
  PRIMARY KEY (`codImmagine`),
  INDEX `fk_immagini_ristoranti1_idx` (`codRistorante` ASC),
  CONSTRAINT `fk_immagini_ristoranti1`
    FOREIGN KEY (`codRistorante`)
    REFERENCES `inf-5ogruppo5`.`ristoranti` (`codRistorante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`tipologie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`tipologie` (
  `codTipo` INT NOT NULL,
  `codRistorante` INT NOT NULL,
  INDEX `fk_tipologie_tipologia_locale1_idx` (`codTipo` ASC),
  INDEX `fk_tipologie_ristoranti1_idx` (`codRistorante` ASC),
  PRIMARY KEY (`codTipo`, `codRistorante`),
  CONSTRAINT `fk_tipologie_tipologia_locale1`
    FOREIGN KEY (`codTipo`)
    REFERENCES `inf-5ogruppo5`.`tipologia_locale` (`codTipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tipologie_ristoranti1`
    FOREIGN KEY (`codRistorante`)
    REFERENCES `inf-5ogruppo5`.`ristoranti` (`codRistorante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`Orari`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`Orari` (
  `codOrario` INT NOT NULL AUTO_INCREMENT,
  `codRistorante` INT NOT NULL,
  `giorno` INT NULL,
  `apertura` TIME NULL,
  `chiusura` TIME NULL,
  INDEX `fk_Orari_ristoranti1_idx` (`codRistorante` ASC),
  PRIMARY KEY (`codOrario`),
  CONSTRAINT `fk_Orari_ristoranti1`
    FOREIGN KEY (`codRistorante`)
    REFERENCES `inf-5ogruppo5`.`ristoranti` (`codRistorante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inf-5ogruppo5`.`Giorni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inf-5ogruppo5`.`Giorni` (
  `codGiorno` INT NOT NULL,
  `giorno` VARCHAR(10) NULL,
  PRIMARY KEY (`codGiorno`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
