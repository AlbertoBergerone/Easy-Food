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
$ALREADY_USED = '';
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
			print(json_encode(getUserInformation($data[$USER][$ATTR_USERNAME], $data[$USER][$ATTR_PASSWORD])));
		}else if($data[$REQUEST_TYPE] == $SIGNUP){
			/*** If it is a signup request ***/
			print(json_encode(insertNewUserTest($data)));
		}else if($data[$REQUEST_TYPE] == $USER_UPDATE_REQUEST){
			/*** If it is an update request ***/
			print(json_encode(updateUser($data)));
		}
	}else return null;
	
} catch(PDOException $e) {
    echo $e->getMessage();
}

/**
 * Function that return an array containing a single user information
 * The search is made thanks to the username and password
 * Parameters:
 *	Username
 *	Password
 */
function getUserInformation($username, $password){
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare("SELECT * FROM $TABLE_USER JOIN $TABLE_MUNICIPALITIES ON $TABLE_MUNICIPALITIES.$ATTR_CADASTRAL_ID=$TABLE_USER.$ATTR_CADASTRAL_ID JOIN $TABLE_PROVINCES ON $TABLE_PROVINCES.$ATTR_PROVINCE_ID=$TABLE_MUNICIPALITIES.$ATTR_PROVINCE_ID JOIN $TABLE_REGIONS ON $TABLE_REGIONS.$ATTR_REGION_ID=$TABLE_PROVINCES.$ATTR_REGION_ID WHERE $ATTR_USERNAME=:username AND $ATTR_PASSWORD=:password"); 
	/*** Binding parameters ***/
	$stmt->bindParam(':username', $username);
	$stmt->bindParam(':password', $password);
	/*** exceute the query ***/
	$stmt->execute(); 
	
	/*** Setting the type of array (in this case it will be an associative array)***/
	return $stmt->setFetchMode(PDO::FETCH_ASSOC);
}

/**
 * Function that return an array containing a single user information
 * The search is made thanks to the user ID
 * Parameters:
 *	userID
 */
 function getUserInformation($userID){
	$stmt = $conn->prepare("SELECT * FROM $TABLE_USER WHERE $ATTR_USER_ID=:userID"); 
	/*** Binding parameters ***/
	$stmt->bindParam(':userID', $userID);
	/*** exceute the query ***/
	$stmt->execute();
	return $stmt->setFetchMode(PDO::FETCH_ASSOC);
 }


/**
 * Function that add a new user in the database
 * Return an array containing the response
 */
function insertNewUser($data){
	/*** If are set all the basic user information it will add him to the database ***/
	if(isset($data[$USER][$ATTR_USERNAME]) && !empty($data[$USER][$ATTR_USERNAME]) && isset($data[$USER][$ATTR_PASSWORD]) && !empty($data[$USER][$ATTR_PASSWORD]) && isset($data[$USER][$ATTR_NAME]) && !empty($data[$USER][$ATTR_NAME]) && isset($data[$USER][$ATTR_LAST_NAME]) && !empty($data[$USER][$ATTR_LAST_NAME]) && isset($data[$USER][$ATTR_USER_EMAIL]) && !empty($data[$USER][$ATTR_USER_EMAIL])){
		/*** Checking the username ***/
		if(isUsernameAlreadyUsed($data[$USER][$ATTR_USERNAME])){
			/*** The username is already used ***/
			$response[$ATTR_USERNAME] = $ALREADY_USED;
			$response[$RESPONSE] = $NOT_ADDED;
		}
		/*** Checking the email ***/
		if(isEmailAlreadyUsed($data[$USER][$ATTR_USER_EMAIL])){
			/*** The email is already used ***/
			$response[$ATTR_USER_EMAIL] = $ALREADY_USED;
			$response[$RESPONSE] = $NOT_ADDED;
		}
		if($response[$RESPONSE] != $NOT_ADDED){
			/*** If I am here, the username does not exist ***/
			/*** If I am here, there isn't anyone who used this email ***/
			
			/*** Adding the user information ***/
			/*** Preparing the SQL statement ***/
			$stmt = $conn->prepare("INSERT INTO $TABLE_USER ($ATTR_NAME, $ATTR_LAST_NAME, $ATTR_USER_EMAIL, $ATTR_USERNAME, $ATTR_PASSWORD) VALUES (:nome, :cognome, :email, :username, :password)");
			/*** Binding parameters ***/
			$stmt->bindParam(':nome', $data[$USER][$ATTR_NAME]);
			$stmt->bindParam(':cognome', $data[$USER][$ATTR_LAST_NAME]);
			$stmt->bindParam(':email', $data[$USER][$ATTR_USER_EMAIL]);
			$stmt->bindParam(':username', $data[$USER][$ATTR_USERNAME]);
			$stmt->bindParam(':password', $data[$USER][$ATTR_PASSWORD]);
			
			try{
				/*** exceuting the insert ***/
				$conn->exec($stmt);
				
				$tmp = $stmt->fetch(PDO::FETCH_ASSOC);
				$response[$RESPONSE] = $ADDED;
				$response[$ATTR_USER_ID] = $tmp["$ATTR_USER_ID"]);
			}catch(PDOException $e){
			  echo($e->getMessage());
			}
		}
	}else
		$response[$RESPONSE] = $NOT_ADDED;
	
	return $response;
}

/**
 * Test
 */
function insertNewUserTest($data){
	/*** If are set all the basic user information it will add him to the database ***/
	if(isset($data[$USER][$ATTR_USERNAME]) && !empty($data[$USER][$ATTR_USERNAME]) && isset($data[$USER][$ATTR_PASSWORD]) && !empty($data[$USER][$ATTR_PASSWORD]) && isset($data[$USER][$ATTR_NAME]) && !empty($data[$USER][$ATTR_NAME]) && isset($data[$USER][$ATTR_LAST_NAME]) && !empty($data[$USER][$ATTR_LAST_NAME]) && isset($data[$USER][$ATTR_USER_EMAIL]) && !empty($data[$USER][$ATTR_USER_EMAIL])){
		$response[$RESPONSE] = $ADDED;
		$response[$ATTR_USER_ID] = 1;
		$response[$ATTR_USERNAME] = $data[$USER][$ATTR_USERNAME];
		$response[$ATTR_PASSWORD] = $data[$USER][$ATTR_PASSWORD];
		$response[$ATTR_NAME] = $data[$USER][$ATTR_NAME];
		$response[$ATTR_LAST_NAME] = $data[$USER][$ATTR_LAST_NAME];
		$response[$ATTR_USER_EMAIL] = $data[$USER][$ATTR_USER_EMAIL];
	}else
		$response[$RESPONSE] = $NOT_ADDED;
	
	return $response;
}


/**
 * Function that add a update a user information in the database
 * Return:
 *	null if the user ID is invalid
 *	an array containing the new user information
 */
function updateUser($data){
	$response[] = null;
	if(isset($data[$USER][$ATTR_USER_ID]) && !empty($data[$USER][$ATTR_USER_ID])){
		/*** Composing the SQL statement ***/
		$statement = "UPDATE $TABLE_USER SET ";
		if(isset($data[$USER][$ATTR_USER_EMAIL]) && !empty($data[$USER][$ATTR_USER_EMAIL])){
			if(!isEmailAlreadyUsed($data[$USER][$ATTR_USER_EMAIL])){
				/*** If I'm here the email is valid and it isn't already used ***/
				/*** Updating email ***/
				$statement .= "$ATTR_CONFIRMED_USER = false, $ATTR_USER_EMAIL = ? ";
				$binds[] = $data[$USER][$ATTR_USER_EMAIL];
			}
		}
		if(isset($data[$USER][$ATTR_USER_PHONE]) && !empty($data[$USER][$ATTR_USER_PHONE])){
			/*** Updating phone number ***/
			$statement .= "$ATTR_USER_PHONE = ? ";
			$binds[] = $data[$USER][$ATTR_USER_PHONE];
		}
		if(isset($data[$USER][$ATTR_ADDRESS]) && !empty($data[$USER][$ATTR_ADDRESS])){
			/*** Updating address ***/
			$statement .= "$ATTR_ADDRESS = ? ";
			$binds[] = $data[$USER][$ATTR_ADDRESS];
		}
		if(isset($data[$USER][$ATTR_PASSWORD]) && !empty($data[$USER][$ATTR_PASSWORD])){
			/*** Updating password ***/
			$statement .= "$ATTR_PASSWORD = ? ";
			$binds[] = $data[$USER][$ATTR_PASSWORD];
		}
		/*** Preparing SQL statement ***/
		$stmt = $conn->prepare($query);
		/*** Binding parameters ***/
		for($i=1; $i <= count($binds); $i=$i+1)
			/*** bindParam: first value has index = 1 ***/
			$stmt->bindParam($i, $binds[$i-1]);
		try{
			/*** If there are some information to change it will execute the insert statement ***/
			if($i!=1)
				$stmt->execute();
			return getUserInformation($data[$USER][$ATTR_USER_ID]);
		}catch(PDOException $e){
			echo($e->getMessage());
		}
	}
	return null;
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
	$stmt = $conn->prepare("SELECT COUNT(*) AS ricorrenza FROM $TABLE_USER WHERE $ATTR_USERNAME=:username"); 
	/*** Binding parameters ***/
	$stmt->bindParam(':username', $username);
	try{
		/*** exceute the query ***/
		$stmt->execute();
		/*** Setting the type of array (in this case it will be an associative array)***/
		$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
		if(count($result)==0)
			/*** The email is not already used.***/
			$isAlreadyUsed = false;
	}catch(PDOException $e){
		echo($e->getMessage());
	}
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
	try{
		/*** exceute the query ***/
		$stmt->execute();
		/*** Setting the type of array (in this case it will be an associative array)***/
		$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
		if(count($result)==0)
			/*** The email is not already used.***/
			$isAlreadyUsed = false;
	}catch(PDOException $e){
		echo($e->getMessage());
	}
	return $isAlreadyUsed;
}
?>