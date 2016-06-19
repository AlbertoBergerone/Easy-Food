<?php

/*** Opening the connection with the db ***/
include 'open_db_connection.php';

define('RESIDENCES', 'comuni');

try {
	/*** Getting data ***/
	$json = file_get_contents("php://input");
	/*** Decoding json ***/
	$data = json_decode($json, TRUE);
	$residence = $data[ATTR_RESIDENCE_NAME];
	if($residence != ""){
		print(json_encode((getResidences($residence))));
	}else return null;
	
} catch(Exception $e) {
    echo $e->getMessage();
}

$conn = null;



/**
 * Function that return an array of arrays containing information about residences begginning with the same initials letters that are passed at this functon.
 * For each residence there will be also the province id.
 * Parameters:
 *	Residence (it can be the full name or only the initials letters)
 */
function getResidences($residence){
	global $conn;
	/*** Preparing the SQL statement ***/
	$stmt = $conn->prepare('SELECT '.ATTR_CADASTRAL_ID.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.' FROM '.TABLE_RESIDENCES.' WHERE '.ATTR_RESIDENCE_NAME.' LIKE :residence ORDER BY '.ATTR_RESIDENCE_NAME.' LIMIT 10');
	/*** Binding parameters ***/
	$param = "$residence%";
	$stmt->bindParam(':residence', $param);
	/*** exceute the query ***/
	$stmt->execute(); 
	
	/*** Setting the type of array (in this case it will be an associative array)***/
	$residences[RESIDENCES] = $stmt->FetchAll(PDO::FETCH_ASSOC);
	return $residences;
}

?> 