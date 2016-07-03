<?php

/*** Opening the connection with the db ***/
include 'open_db_connection.php';

/*** Request types ***/
define('REQUEST_TYPE', 'request_type');
define('LOGIN', 'login_request');
define('SIGNUP', 'sigup_request');
define('USER_UPDATE_REQUEST', 'user_update_request');
define('USER_DELETE_REQUEST', 'user_delete_request');

/*** Used for getting JSON Object ***/
define('USER', 'user');


/*** Response associative-array names ***/
define('RESPONSE', 'response');

/*** Response constants ***/
define('ALREADY_USED', 'already_used');
define('ADDED', 'added');
define('NOT_ADDED', 'not_added');
define('DELETED', 'deleted');


try {
	/*** Getting data ***/
	$json = file_get_contents("php://input");
	/*** Decoding json ***/
	$data = json_decode($json, TRUE);
	$user = json_decode($data[USER]);
	/*** Checking the type of request ***/
	if(isset($data[REQUEST_TYPE])){
		if($data[REQUEST_TYPE] == LOGIN){
			/*** If it is a login request ***/
			print(json_encode(toUserArray(getUserInformation_credentials($user->{ATTR_USERNAME}, $user->{ATTR_PASSWORD}))));
		}else if($data[REQUEST_TYPE] == SIGNUP){
			/*** If it is a signup request ***/
			print(json_encode(insertNewUser($user)));
		}else if($data[REQUEST_TYPE] == USER_UPDATE_REQUEST){
			/*** If it is an update request ***/
			print(json_encode(updateUser($user)));
		}else if($data[REQUEST_TYPE] == USER_DELETE_REQUEST){
			if(deleteUser($user)){
				json_encode($response[RESPONSE] = DELETED);
			}
		}else return null;
	}else return null;
	
} catch(Exception $e) {
    echo $e->getMessage();
}

$conn = null;

/**
 * Parameters: an array containing the parameters
 * Function that returns an array containing a user properties
 */
function toUserArray($user){
	$tmp = null;
	/*** Checking if the user is not null ***/
	if($user != null){
		$tmp[USER][ATTR_USER_ID] = $user[ATTR_USER_ID];
		$tmp[USER][ATTR_USERNAME] = $user[ATTR_USERNAME];
		$tmp[USER][ATTR_PASSWORD] = $user[ATTR_PASSWORD];
		$tmp[USER][ATTR_NAME] = $user[ATTR_NAME];
		$tmp[USER][ATTR_LAST_NAME] = $user[ATTR_LAST_NAME];
		$tmp[USER][ATTR_USER_EMAIL] = $user[ATTR_USER_EMAIL];
		$tmp[USER][ATTR_USER_ADDRESS] = $user[ATTR_USER_ADDRESS];
		$tmp[USER][ATTR_USER_PHONE] = $user[ATTR_USER_PHONE];
		$tmp[USER][ATTR_CONFIRMED_USER] = $user[ATTR_CONFIRMED_USER];
		$tmp[USER][ATTR_RESIDENCE_NAME] = $user[ATTR_RESIDENCE_NAME];
		$tmp[USER][ATTR_PROVINCE_ID] = $user[ATTR_PROVINCE_ID];
	}
	return $tmp;
}

/**
 * Function that return an array containing a single user information
 * The search is made thanks to the username and password
 * Parameters:
 *	Username
 *	Password
 */
function getUserInformation_credentials($username, $password){
	global $conn;
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare('SELECT * FROM '.TABLE_USER.' LEFT JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_USER.'.'.ATTR_CADASTRAL_ID.' LEFT JOIN '.TABLE_PROVINCES.' ON '.TABLE_PROVINCES.'.'.ATTR_PROVINCE_ID.' = '.TABLE_RESIDENCES.'.'.ATTR_PROVINCE_ID.' LEFT JOIN '.TABLE_REGIONS.' ON '.TABLE_REGIONS.'.'.ATTR_REGION_ID.' = '.TABLE_PROVINCES.'.'.ATTR_REGION_ID.' WHERE '.ATTR_USERNAME.' = :username AND '.ATTR_PASSWORD.' = :password'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':username', $username);
	$stmt->bindParam(':password', $password);
	/*** exceute the query ***/
	$stmt->execute(); 
	$ret = $stmt->fetch(PDO::FETCH_ASSOC);
	/*** Checking if the query returned something ***/
	if(!$ret)
		$ret = null;
	/*** Fetching array (in this case it will be an associative array)***/
	return ($ret);
}

/**
 * Function that return an array containing a single user information
 * The search is made thanks to the user ID
 * Parameters:
 *	userID
 */
function getUserInformation_id($userID){
	global $conn;
	$stmt = $conn->prepare('SELECT * FROM '.TABLE_USER.' LEFT JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_USER.'.'.ATTR_CADASTRAL_ID.' LEFT JOIN '.TABLE_PROVINCES.' ON '.TABLE_PROVINCES.'.'.ATTR_PROVINCE_ID.' = '.TABLE_RESIDENCES.'.'.ATTR_PROVINCE_ID.' LEFT JOIN '.TABLE_REGIONS.' ON '.TABLE_REGIONS.'.'.ATTR_REGION_ID.' = '.TABLE_PROVINCES.'.'.ATTR_REGION_ID.' WHERE '.ATTR_USER_ID.' = :userID'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':userID', $userID);
	/*** exceute the query ***/
	$stmt->execute(); 
	/*** Fetching array (in this case it will be an associative array) ***/
	$ret = $stmt->fetch(PDO::FETCH_ASSOC);
	if($ret[USER_ID] == null || $ret[USER_ID] == 'null')
		$ret = null;
	return($ret);
}


/**
 * Function that add a new user in the database
 * Return an array containing the response
 */
function insertNewUser($user){
	global $conn;
	/*** If are set all the basic user information it will add him to the database ***/
	if(isset($user->{ATTR_USERNAME}) && !empty($user->{ATTR_USERNAME}) && isset($user->{ATTR_PASSWORD}) && !empty($user->{ATTR_PASSWORD}) && isset($user->{ATTR_NAME}) && !empty($user->{ATTR_NAME}) && isset($user->{ATTR_LAST_NAME}) && !empty($user->{ATTR_LAST_NAME}) && isset($user->{ATTR_USER_EMAIL}) && !empty($user->{ATTR_USER_EMAIL})){
		$response[RESPONSE] = ADDED;
		/*** Checking the username ***/
		if(isUsernameAlreadyUsed($user->{ATTR_USERNAME})){
			/*** The username is already used ***/
			$response[RESPONSE] = NOT_ADDED;
			$response[USER][ATTR_USERNAME] = ALREADY_USED;
		}
		/*** Checking the email ***/
		if(isEmailAlreadyUsed($user->{ATTR_USER_EMAIL})){
			/*** The email is already used ***/
			$response[RESPONSE] = NOT_ADDED;
			$response[USER][ATTR_USER_EMAIL] = ALREADY_USED;
		}
		
		if($response[RESPONSE] != NOT_ADDED){
			/*** If I am here, the username does not exist ***/
			/*** If I am here, there isn't anyone who used this email ***/
			
			/*** Adding the user information ***/
			/*** Preparing the SQL statement ***/
			$stmt = $conn->prepare('INSERT INTO '.TABLE_USER.' ('.ATTR_NAME.', '.ATTR_LAST_NAME.', '.ATTR_USER_EMAIL.', '.ATTR_USERNAME.', '.ATTR_PASSWORD.') VALUES (:nome, :cognome, :email, :username, :password)');
			/*** Binding parameters ***/
			$stmt->bindParam(':nome', $user->{ATTR_NAME});
			$stmt->bindParam(':cognome', $user->{ATTR_LAST_NAME});
			$stmt->bindParam(':email', $user->{ATTR_USER_EMAIL});
			$stmt->bindParam(':username', $user->{ATTR_USERNAME});
			$stmt->bindParam(':password', $user->{ATTR_PASSWORD});
			
			/*** exceuting the insert ***/
			$stmt->execute();
			$response[USER] = getUserInformation_credentials($user->{ATTR_USERNAME}, $user->{ATTR_PASSWORD});
		}
	}else
		$response[RESPONSE] = NOT_ADDED;
	
	return $response;
}


/**
 * Function that add a update a user information in the database
 * Return:
 *	null if the user ID is invalid
 *	an array containing the new user information
 */
function updateUser($user){
	global $conn;
	if(isset($user->{ATTR_USER_ID}) && !empty($user->{ATTR_USER_ID})){
		/*** Composing the SQL statement ***/
		$statement = 'UPDATE '.TABLE_USER.' SET '.ATTR_USER_ID.' = ? ';
		$binds[] = $user->{ATTR_USER_ID};
		if(isset($user->{ATTR_USER_EMAIL}) && !empty($user->{ATTR_USER_EMAIL})){
			if(!isEmailAlreadyUsed($user->{ATTR_USER_EMAIL})){
				/*** If I'm here the email is valid and it isn't already used ***/
				/*** Updating email ***/
				$statement .= ', '.ATTR_CONFIRMED_USER.' = false, '.ATTR_USER_EMAIL.' = ? ';
				$binds[] = $user->ATTR_USER_EMAIL;
			}
		}
		if(isset($user->{ATTR_NAME}) && !empty($user->{ATTR_NAME})){
			/*** Updating phone number ***/
			$statement .= ', '.ATTR_NAME.' = ? ';
			$binds[] = $user->{ATTR_NAME};
		}
		if(isset($user->{ATTR_LAST_NAME}) && !empty($user->{ATTR_LAST_NAME})){
			/*** Updating phone number ***/
			$statement .= ', '.ATTR_LAST_NAME.' = ? ';
			$binds[] = $user->{ATTR_LAST_NAME};
		}
		if(isset($user->{ATTR_USER_PHONE}) && !empty($user->{ATTR_USER_PHONE})){
			/*** Updating phone number ***/
			$statement .= ', '.ATTR_USER_PHONE.' = ? ';
			$binds[] = $user->{ATTR_USER_PHONE};
		}
		if(isset($user->{ATTR_USER_ADDRESS}) && !empty($user->{ATTR_USER_ADDRESS})){
			/*** Updating address ***/
			$statement .= ', '.ATTR_USER_ADDRESS.' = ? ';
			$binds[] = $user->{ATTR_USER_ADDRESS};
		}
		if(isset($user->{ATTR_PASSWORD}) && !empty($user->{ATTR_PASSWORD})){
			/*** Updating address ***/
			$statement .= ', '.ATTR_PASSWORD.' = ? ';
			$binds[] = $user->{ATTR_PASSWORD};
		}
		$statement .= ' WHERE '.ATTR_USER_ID.' = ? ';
		$binds[] = $user->{ATTR_USER_ID};
		/*** Preparing SQL statement ***/
		$stmt = $conn->prepare($statement);
		/*** Binding parameters ***/
		for($i=1; $i <= count($binds); $i=$i+1)
			/*** bindParam: first value has index = 1 ***/
			$stmt->bindParam($i, $binds[$i-1]);
		try{
			/*** If there are some information to change it will execute the insert statement ***/
			if($i!=1)
				$stmt->execute();
			return (toUserArray(getUserInformation_id($user->{ATTR_USER_ID})));
		}catch(PDOException $e){
			echo($e->getMessage());
		}
	}
	return null;
}

function deleteUser($user){
	global $conn;
	if(isset($user[ATTR_USER_ID]) && !empty($user[ATTR_USER_ID])){
		$stmt = $conn->prepare('DELETE  FROM'.TABLE_USER.' WHERE '.ATTR_USER_ID.' = :userID');
		$stmt->bindParam(':userID', $user[ATTR_USER_ID]);
		$stmt->execute();
		return true;
	}else return false;
}

/**
 * Function that checks if the username is already used
 * Return:
 *	true  --> it is used
 * 	false --> it isn't used
 */
function isUsernameAlreadyUsed($username){
	global $conn;
	$isAlreadyUsed = true;
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare('SELECT COUNT(*) AS ricorrenza FROM '.TABLE_USER.' WHERE '.ATTR_USERNAME.' = :username'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':username', $username, PDO::PARAM_STR);
	try{
		/*** exceute the query ***/
		$stmt->execute();
		/*** Setting the type of array (in this case it will be an associative array)***/
		$result = $stmt->fetch(PDO::FETCH_ASSOC);
		if($result['ricorrenza'] == 0)
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
	global $conn;
	$isAlreadyUsed = true;
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare('SELECT COUNT(*) AS ricorrenza FROM '.TABLE_USER.' WHERE '.ATTR_USER_EMAIL.' = :emailUtente'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':emailUtente', $userEmail);
	try{
		/*** exceute the query ***/
		$stmt->execute();
		/*** Setting the type of array (in this case it will be an associative array)***/
		$result = $stmt->fetch(PDO::FETCH_ASSOC);
		if($result['ricorrenza']==0)
			/*** The email is not already used.***/
			$isAlreadyUsed = false;
	}catch(PDOException $e){
		echo($e->getMessage());
	}
	return $isAlreadyUsed;
}
?>