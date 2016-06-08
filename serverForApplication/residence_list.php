<?php

/*** Opening the connection with the db ***/
include 'open_db_connection.php';

try {
	/*** Getting data ***/
	$json = file_get_contents("php://input");
	/*** Decoding json ***/
	$data = json_decode($json, TRUE);
	$residence = $data[ATTR_RESIDENCE_NAME];
	if($residence != ""){
		var_dump(getResidences($residence));
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
	$stmt = $conn->prepare('SELECT * FROM '.{TABLE_RESIDENCES}.' WHERE '.ATTR_RESIDENCE_NAME.' LIKE :residence'); 
	/*** Binding parameters ***/
	$stmt->bindParam(':residence', '%'.$residence.'%');
	/*** exceute the query ***/
	$conn->exec($stmt); 
	
	/*** Setting the type of array (in this case it will be an associative array)***/
	return $stmt->FetchAll(PDO::FETCH_ASSOC);
}

?>