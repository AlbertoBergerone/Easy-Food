<?php
/*** mysql hostname ***/
$hostname = 'localhost';
/*** mysql username ***/
$username = 'user';
/*** mysql password ***/
$password = '';
/*** mysql database name ***/
$dbname = 'easyfooddb';

/*** Request types ***/
$REQUEST_TYPE = 'request_type';
$LOGIN = 'login_request';
$SIGNUP = 'sigup_request';
$USER_UPDATE_REQUEST = 'user_update_request';

/*** Used for getting JSON Object ***/
$USER = 'user';

/*** database table ***/
$TABLE_USER = 'utente';
$TABLE_PROVINCES = 'province';
$TABLE_REGIONS = 'regioni';
$TABLE_MUNICIPALITIES = 'comuni';

/*** database attributes ***/
$ATTR_NAME = 'nome';
$ATTR_LAST_NAME = 'cognome';
$ATTR_USERNAME = 'username';
$ATTR_PASSWORD = 'password';
$ATTR_USER_EMAIL = 'emailUtente';
$ATTR_ADDRESS = 'indirizzo';
$ATTR_USER_PHONE = 'telUtente';
$ATTR_USER_ID = 'codUtente';
$ATTR_CONFIRMED_USER = 'utenteConfermato';
$ATTR_CADASTRAL_ID = 'codCatastale';
$ATTR_PROVINCE_ID = 'codProvincia';
$ATTR_REGION_ID = 'codRegione';

/*** Response associative-array names ***/
$RESPONSE = 'response';

/*** Response constants ***/
$ALREADY_USED = 'already_used';
$ADDED = 'added';
$NOT_ADDED = 'not_added';

try {
	/*** Trying to open a connection with the database ***/
    $conn = new PDO("mysql:host=$hostname;dbname=$dbname", $username, $password);
	/*** set the PDO error mode to exception ***/
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	/*** Decoding json ***/
	$data = json_decode($_POST, TRUE);
	
	/*** Checking the type of request ***/
	if(isset($data[$REQUEST_TYPE]){
		if($data[$REQUEST_TYPE] == $LOGIN){
			/*** If it is a login request ***/
			print(json_encode("prova login riuscita"));
		}else if($data[$REQUEST_TYPE] == $SIGNUP){
			/*** If it is a signup request ***/
			print(json_encode("prova registrazione riuscita"));
		}else if($data[$REQUEST_TYPE] == $USER_UPDATE_REQUEST){
			/*** If it is an update request ***/
			print(json_encode("prova aggiornamento riuscita"));
		}
	}
	else print(json_encode(""));
	
} catch(PDOException $e) {
    echo $e->getMessage();
}

?>