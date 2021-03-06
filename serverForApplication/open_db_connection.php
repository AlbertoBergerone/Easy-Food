<?php
/*** database table ***/
define('TABLE_USER', 'utente');
define('TABLE_REGIONS', 'regioni');
define('TABLE_PROVINCES', 'province');
define('TABLE_RESIDENCES', 'comuni');
define('TABLE_RESTAURANTS', 'ristoranti');
define('TABLE_HOURS', 'orari');
define('TABLE_RATINGS', 'valutazioni');
define('TABLE_TYPOLOGIES', 'tipologie');
define('TABLE_TYPOLOGY_RESTAURANT', 'tipologia_locale');
define('TABLE_OWNERS', 'proprietari');
define('TABLE_PICS', 'immagini');

/*** database attributes ***/
define('ATTR_NAME', 'nome');
define('ATTR_LAST_NAME', 'cognome');
define('ATTR_USERNAME', 'username');
define('ATTR_PASSWORD', 'passwordUtente');
define('ATTR_USER_EMAIL', 'emailUtente');
define('ATTR_USER_ADDRESS', 'indirizzoUtente');
define('ATTR_USER_PHONE', 'telUtente');
define('ATTR_USER_ID', 'codUtente');
define('ATTR_CONFIRMED_USER', 'utenteConfermato');
define('ATTR_CADASTRAL_ID', 'codCatastale');
define('ATTR_RESIDENCE_NAME', 'nomeComune');
define('ATTR_PROVINCE_ID', 'codProvincia');
define('ATTR_REGION_ID', 'codRegione');
define('ATTR_CAP', 'cap');
define('ATTR_PROVINCE_NAME', 'nomeProvincia');
define('ATTR_RESTAURANT_ID', 'codRistorante');
define('ATTR_RESTAURANT_NAME', 'nomeRistorante');
define('ATTR_RESTAURANT_ADDRESS', 'indirizzoRistorante');
define('ATTR_LONGITUDE', 'longitudine');
define('ATTR_LATITUDE', 'latitudine');
define('ATTR_RESTAURANT_DESCRIPTION', 'descrizione');
define('ATTR_RESTAURANT_PHONE', 'telRistorante');
define('ATTR_RESTAURANT_EMAIL', 'emailRistorante');
define('ATTR_RATING', 'valutazioneGenerica');
define('ATTR_COMMENT_TITLE', 'titoloCommento');
define('ATTR_COMMENT', 'commento');
define('ATTR_TYPE_ID', 'codTipo');
define('ATTR_TYPE', 'Tipo');
define('ATTR_DAY', 'giorno');
define('ATTR_OPENING', 'apertura');
define('ATTR_CLOSURE', 'chiusura');


/*** mysql hostname ***/
$hostname = 'localhost';
/*** mysql username ***/
$username = 'inf-5ogruppo5';
/*** mysql password ***/
$password = '16ottobre1997';
/*** mysql database name ***/
$dbname = 'inf-5ogruppo5';

$conn = null;
try {
	/*** Trying to open a connection with the database ***/
    $conn = new PDO("mysql:host=$hostname;dbname=$dbname", $username, $password);
	/*** set the PDO error mode to exception ***/
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    echo $e->getMessage();
}

?>