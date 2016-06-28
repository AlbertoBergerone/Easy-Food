-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: Giu 21, 2016 alle 09:38
-- Versione del server: 5.5.38-0ubuntu0.14.04.1
-- Versione PHP: 5.5.9-1ubuntu4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `inf-5ogruppo5`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `comuni`
--
-- Creazione: Giu 06, 2016 alle 21:37
--

CREATE TABLE IF NOT EXISTS `comuni` (
  `codCatastale` varchar(4) NOT NULL,
  `nomeComune` varchar(30) NOT NULL,
  `cap` int(11) NOT NULL,
  `codProvincia` varchar(2) NOT NULL,
  PRIMARY KEY (`codCatastale`),
  KEY `codProvincia_idx` (`codProvincia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `id_ristoranti`
--
-- Creazione: Giu 20, 2016 alle 12:55
--

CREATE TABLE IF NOT EXISTS `id_ristoranti` (
  `codRistorante` int(11) NOT NULL,
  `idRistorante` varchar(27) DEFAULT NULL,
  PRIMARY KEY (`codRistorante`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `immagini`
--
-- Creazione: Giu 06, 2016 alle 21:02
--

CREATE TABLE IF NOT EXISTS `immagini` (
  `codImmagine` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) NOT NULL,
  `isLogo` tinyint(1) NOT NULL DEFAULT '0',
  `codRistorante` int(11) NOT NULL,
  PRIMARY KEY (`codImmagine`),
  KEY `fk_immagini_ristoranti1_idx` (`codRistorante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `orari`
--
-- Creazione: Giu 20, 2016 alle 12:55
--

CREATE TABLE IF NOT EXISTS `orari` (
  `codOrario` int(11) NOT NULL AUTO_INCREMENT,
  `codRistorante` int(11) NOT NULL,
  `giorno` int(11) DEFAULT NULL,
  `apertura` time DEFAULT NULL,
  `chiusura` time DEFAULT NULL,
  PRIMARY KEY (`codOrario`),
  KEY `fk_Orari_ristoranti1_idx` (`codRistorante`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=898 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `proprietari`
--
-- Creazione: Giu 06, 2016 alle 21:02
--

CREATE TABLE IF NOT EXISTS `proprietari` (
  `codRistorante` int(11) NOT NULL,
  `codUtente` int(11) NOT NULL,
  PRIMARY KEY (`codUtente`,`codRistorante`),
  KEY `fk_Proprietari_ristoranti1_idx` (`codRistorante`),
  KEY `fk_Proprietari_utente1_idx` (`codUtente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `province`
--
-- Creazione: Giu 04, 2016 alle 17:51
--

CREATE TABLE IF NOT EXISTS `province` (
  `codProvincia` varchar(2) NOT NULL,
  `nomeProvincia` varchar(35) NOT NULL,
  `codRegione` varchar(3) NOT NULL,
  PRIMARY KEY (`codProvincia`),
  KEY `codRegioni_idx` (`codRegione`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `regioni`
--
-- Creazione: Giu 04, 2016 alle 17:50
--

CREATE TABLE IF NOT EXISTS `regioni` (
  `codRegione` varchar(3) NOT NULL,
  `nomeRegione` varchar(30) NOT NULL,
  PRIMARY KEY (`codRegione`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `ristoranti`
--
-- Creazione: Giu 20, 2016 alle 12:53
--

CREATE TABLE IF NOT EXISTS `ristoranti` (
  `codRistorante` int(11) NOT NULL AUTO_INCREMENT,
  `indirizzoRistorante` varchar(100) NOT NULL,
  `nomeRistorante` varchar(50) NOT NULL,
  `longitudine` double DEFAULT NULL,
  `latitudine` double DEFAULT NULL,
  `descrizione` text,
  `codCatastale` varchar(45) NOT NULL,
  `telRistorante` varchar(20) DEFAULT NULL,
  `emailRistorante` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`codRistorante`),
  KEY `fk_ristoranti_comuni1_idx` (`codCatastale`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=198 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `tipologia_locale`
--
-- Creazione: Giu 20, 2016 alle 12:34
--

CREATE TABLE IF NOT EXISTS `tipologia_locale` (
  `codTipo` int(11) NOT NULL AUTO_INCREMENT,
  `Tipo` varchar(30) NOT NULL,
  PRIMARY KEY (`codTipo`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=15 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `tipologie`
--
-- Creazione: Giu 20, 2016 alle 12:55
--

CREATE TABLE IF NOT EXISTS `tipologie` (
  `codTipo` int(11) NOT NULL,
  `codRistorante` int(11) NOT NULL,
  PRIMARY KEY (`codTipo`,`codRistorante`),
  KEY `fk_tipologie_tipologia_locale1_idx` (`codTipo`),
  KEY `fk_tipologie_ristoranti1_idx` (`codRistorante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `utente`
--
-- Creazione: Giu 20, 2016 alle 13:17
--

CREATE TABLE IF NOT EXISTS `utente` (
  `codUtente` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `indirizzoUtente` varchar(100) DEFAULT NULL,
  `emailUtente` varchar(45) NOT NULL,
  `codCatastale` varchar(4) DEFAULT NULL,
  `telUtente` varchar(20) DEFAULT NULL,
  `utenteConfermato` tinyint(1) NOT NULL DEFAULT '0',
  `username` varchar(45) NOT NULL,
  `passwordUtente` varchar(45) NOT NULL,
  PRIMARY KEY (`codUtente`),
  UNIQUE KEY `emailUtente_UNIQUE` (`emailUtente`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `username_password` (`username`,`passwordUtente`),
  KEY `fk_utente_comuni1_idx` (`codCatastale`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `valutazioni`
--
-- Creazione: Giu 20, 2016 alle 12:56
--

CREATE TABLE IF NOT EXISTS `valutazioni` (
  `codRistorante` int(11) NOT NULL,
  `codUtente` int(11) NOT NULL,
  `valutazioneAmbiente` smallint(1) NOT NULL,
  `valutazioneServizio` smallint(1) NOT NULL,
  `valutazioneCucina` smallint(1) NOT NULL,
  `valutazionePrezzo` smallint(1) NOT NULL,
  `valutazioneGenerica` smallint(1) NOT NULL,
  `titoloCommento` varchar(120) DEFAULT NULL,
  `commento` tinytext,
  PRIMARY KEY (`codRistorante`,`codUtente`),
  KEY `fk_Valutazioni_ristoranti1_idx` (`codRistorante`),
  KEY `fk_Valutazioni_utente1_idx` (`codUtente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `comuni`
--
ALTER TABLE `comuni`
  ADD CONSTRAINT `codProvincia` FOREIGN KEY (`codProvincia`) REFERENCES `province` (`codProvincia`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `id_ristoranti`
--
ALTER TABLE `id_ristoranti`
  ADD CONSTRAINT `id_ristoranti_ibfk_1` FOREIGN KEY (`codRistorante`) REFERENCES `ristoranti` (`codRistorante`);

--
-- Limiti per la tabella `immagini`
--
ALTER TABLE `immagini`
  ADD CONSTRAINT `fk_immagini_ristoranti1` FOREIGN KEY (`codRistorante`) REFERENCES `ristoranti` (`codRistorante`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `orari`
--
ALTER TABLE `orari`
  ADD CONSTRAINT `fk_Orari_ristoranti1` FOREIGN KEY (`codRistorante`) REFERENCES `ristoranti` (`codRistorante`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `proprietari`
--
ALTER TABLE `proprietari`
  ADD CONSTRAINT `fk_Proprietari_ristoranti1` FOREIGN KEY (`codRistorante`) REFERENCES `ristoranti` (`codRistorante`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Proprietari_utente1` FOREIGN KEY (`codUtente`) REFERENCES `utente` (`codUtente`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `province`
--
ALTER TABLE `province`
  ADD CONSTRAINT `codRegioni` FOREIGN KEY (`codRegione`) REFERENCES `regioni` (`codRegione`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `ristoranti`
--
ALTER TABLE `ristoranti`
  ADD CONSTRAINT `fk_ristoranti_comuni1` FOREIGN KEY (`codCatastale`) REFERENCES `comuni` (`codCatastale`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `tipologie`
--
ALTER TABLE `tipologie`
  ADD CONSTRAINT `fk_tipologie_ristoranti1` FOREIGN KEY (`codRistorante`) REFERENCES `ristoranti` (`codRistorante`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_tipologie_tipologia_locale1` FOREIGN KEY (`codTipo`) REFERENCES `tipologia_locale` (`codTipo`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `utente`
--
ALTER TABLE `utente`
  ADD CONSTRAINT `fk_utente_comuni1` FOREIGN KEY (`codCatastale`) REFERENCES `comuni` (`codCatastale`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `valutazioni`
--
ALTER TABLE `valutazioni`
  ADD CONSTRAINT `fk_Valutazioni_ristoranti1` FOREIGN KEY (`codRistorante`) REFERENCES `ristoranti` (`codRistorante`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Valutazioni_utente1` FOREIGN KEY (`codUtente`) REFERENCES `utente` (`codUtente`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
