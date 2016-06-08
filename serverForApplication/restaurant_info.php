<?php

/*** Opening the connection with the db ***/
include 'open_db_connection.php';

try {
	/*** Getting data ***/
	$json = file_get_contents("php://input");
	/*** Decoding json ***/
	$data = json_decode($json, TRUE);
	
	if($data != null){
		var_dump(getRestaurants($data));
	}else return null;
	
} catch(Exception $e) {
    echo $e->getMessage();
}

$conn = null;

function getRestaurants($data){
	global $conn;
	//SELECT * FROM ristoranti JOIN comuni ON comuni.codCatastale = ristoranti.codCatastale WHERE 1 AND ristoranti.codCatastale = (SELECT codCatastale FROM comuni WHERE nomeComune = 'Margarita' AND codProvincia = 'CN')
	$statement = 'SELECT * FROM'.TABLE_RESTAURANTS.' JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' WHERE 1 ';
	if(isset($data[ATTR_CADASTRAL_ID]) && !empty($data[ATTR_CADASTRAL_ID])){
		$statement .= ' AND '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' = ?';
		$binds[] = $data[ATTR_CADASTRAL_ID];
	}else if(isset($data[ATTR_RESIDENCE_NAME]) && !empty($data[ATTR_RESIDENCE_NAME]) && isset($data[ATTR_PROVINCE_ID]) && !empty($data[ATTR_PROVINCE_ID])){
		/*** The ATTR_RESIDENCE_NAME is set ***/
		$statement .= ' AND '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' = (SELECT '.ATTR_CADASTRAL_ID.' FROM '.TABLE_RESIDENCES.' WHERE '.ATTR_RESIDENCE_NAME.' = ? AND '.ATTR_PROVINCE_ID.'= ?) ';
		$binds[] = $data[ATTR_RESIDENCE_NAME];
		$binds[] = $data[ATTR_PROVINCE_ID];
	}
	for($i=1; $i <= count($binds); $i=$i+1)
			/*** bindParam: first value has index = 1 ***/
			$stmt->bindParam($i, $binds[$i-1]);
	try{
		/*** If there are some information to change it will execute the insert statement ***/
		if($i!=1)
			$stmt->execute();
		return $stmt->FetchAll(PDO::FETCH_ASSOC);
	}catch(PDOException $e){
		echo($e->getMessage());
	}
	return null;
}

?>