<?php
/*
http://www.helloandroid.com/tutorials/connecting-mysql-database
https://code.google.com/archive/p/droidactivator/
https://fahmirahman.wordpress.com/2011/04/21/connection-between-php-server-and-android-client-using-http-and-json/

http://stackoverflow.com/questions/13635395/how-to-send-variable-from-php-to-an-android-app
*/

/*** mysql hostname ***/
$hostname = 'localhost';
/*** mysql username ***/
$username = 'user';
/*** mysql password ***/
$password = '';
/*** mysql database name ***/
$dbname = 'easyfooddb';

/*** Request types ***/
$LOGIN = 'login_request';
$SIGNUP = 'sigup_request';

try {
	/*** Trying to open a connection with the database ***/
    $conn = new PDO("mysql:host=$hostname;dbname=$dbname", $username, $password);
	/*** set the PDO error mode to exception ***/
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	/*** Checking the type of request ***/
	if($_POST['tipo_richiesta'] == $LOGIN){
		/*** If it is a login request ***/
		
		$result = searchUserInformation()
		/*** If the User exists it will return a json object containing his information. Else it will return null ***/
		if(count($result)==0)
			return null;
		else
			/*** return a json with user information ***/
			print(json_encode($result));
		
	}else if($_POST['tipo_richiesta'] == $SIGNUP){
		/*** If it is a signup request ***/
		$response["registrazione_segnalazione"] = insertNewUser();
		print(json_encode($response));
	}
} catch(PDOException $e) {
    echo $e->getMessage();
}


/*** Function that search the user information using his username and his password ***/
function searchUserInformation(){
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare('SELECT * FROM utente JOIN comuni ON comuni.codCatastale=utente.codCatastale JOIN province ON province.codProvincia=comuni.codProvincia JOIN regioni ON regioni.codRegione=province.codRegione WHERE username=:username AND password=:password'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':username', $_POST['username']);
	$stmt->bindParam(':password', $_POST['password']);
	/*** exceute the query ***/
	$stmt->execute(); 
	
	/*** Setting the type of array (in this case it will be an associative array)***/
	$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
	return $result;
}


/**
 * Function that add a new user in the database
 * return:
 *	 0	--> the user was added
 *	-1	--> there wasn't all basic information
 *	-2	--> the username is already used
 *	-3	--> the email is already used
 */
function insertNewUser(){
	/*** If are set all the basic user information it will add him to the database ***/
	if(isset($_POST['username']) && !empty($_POST['username']) && isset($_POST['password']) && !empty($_POST['password']) && isset($_POST['nome']) && !empty($_POST['nome']) && isset($_POST['cognome']) && !empty($_POST['cognome']) && isset($_POST['emailUtente']) && !empty($_POST['emailUtente'])){
		/*** Checking the username ***/
		if(isUsernameAlreadyUsed($_POST['username']))
			/*** The username is already used. It will return -2 ***/
			$ret = -2;
		else{
			/*** If I am here, the username does not exist ***/
			
			/*** Checking the email ***/
			if(isEmailAlreadyUsed($_POST['emailUtente']))
				/*** The email is already used. It will return -3 ***/
				$ret = -3;
			else{
				/*** If I am here, there isn't anyone who used this email ***/
				
				/*** Adding the user information ***/
				/*** Preparing the SQL statement ***/
				$stmt = $conn->prepare('INSERT INTO utente (nome, cognome, emailUtente, username, password) VALUES (:nome, :cognome, :email, :username, :password)');
				/*** Binding parameters ***/
				$stmt->bindParam(':username', $_POST['nome']);
				$stmt->bindParam(':username', $_POST['cognome']);
				$stmt->bindParam(':email', $_POST['emailUtente']);
				$stmt->bindParam(':username', $_POST['username']);
				$stmt->bindParam(':password', $_POST['password']);
				/*** exceuting the insert ***/
				$conn->exec($stmt);
				
				/*** Commit the transactions ***/
				$dbh->commit();
				/*** If all goes well it will return 0 ***/
				$ret = 0;
			}

		}
	}else
		/*** If there wasn't all the information it will return -1 ***/
		$ret = -1;
	
	return $ret;
	
	
/**
 * Function that add a update a user information in the database
 * Return:
 *	 0	--> the user was updated
 */
function updateUser(){
	if(isset($_POST['codUtente']) && !empty($_POST['codUtente'])){
		$statement = 'UPDATE utente SET ';
		if(isset($_POST['emailUtente']) && !empty($_POST['emailUtente']) && !isEmailAlreadyUsed($_POST['emailUtente'], $_POST['codUtente'])){
			/*** If I'm here the email is valid and it isn't already used ***/
			/*** Adding email update ***/
			$statement .= "utenteConfermato=false, emailUtente=:emailUtente ";
			
		}
		
	}
	
}

/**
 * Function that checks if the username is already used
 * Return:
 *	true  --> it is used
 * 	false --> it isn't used
 */
function isUsernameAlreadyUsed($username){
	$isAlreadyUsed = true;
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare('SELECT COUNT(*) AS ricorrenza FROM utente WHERE username=:username'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':username', $username);
	/*** exceute the query ***/
	$stmt->execute();
	/*** Setting the type of array (in this case it will be an associative array)***/
	$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
	if(count($result)==0)
		/*** The email is not already used.***/
		$isAlreadyUsed = false;
	return $isAlreadyUsed;
}

/**
 * Function that checks if the email is already used
 * Return:
 *	true  --> it is used
 * 	false --> it isn't used
 */
function isEmailAlreadyUsed($userEmail){
	$isAlreadyUsed = true;
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare('SELECT COUNT(*) AS ricorrenza FROM utente WHERE emailUtente=:emailUtente'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':emailUtente', $userEmail);
	/*** exceute the query ***/
	$stmt->execute();
	/*** Setting the type of array (in this case it will be an associative array)***/
	$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
	if(count($result)==0)
		/*** The email is not already used.***/
		$isAlreadyUsed = false;
	return $isAlreadyUsed;
}

/**
 * Function that checks if the email is already used, if it is used by a
 * Return:
 *	 0 --> it isn't used
 * 	-1 --> it is used by the same user
 * 	-2 --> it is used by another
 */
function isEmailAlreadyUsed($userEmail, $codUtente){
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare('SELECT codUtente FROM utente WHERE emailUtente=:emailUtente'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':emailUtente', $userEmail);
	/*** exceute the query ***/
	$stmt->execute();
	/*** Setting the type of array (in this case it will be an associative array)***/
	$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
	if(count($result) == 0)
		/*** The email is not used ***/
		$ret = 0;
	else if(count($result) == 0 && $result[0]['codUtente'] == $codUtente)
		/*** The email is already used by the same user ***/
		$ret = -1;
	else
		/*** The email is used by another user ***/
		$ret = -2;
	
	return $ret;
}
?>