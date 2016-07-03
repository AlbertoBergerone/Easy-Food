<?php

/*** Opening the connection with the db ***/
include 'open_db_connection.php';
define('MIN_DIST', '0.164');
define('RATING', 'valutazione');
define('RESTAURANTS', 'ristoranti');
define('RESTAURANT', 'ristorante');
define('QUERY_TEXT', 'query_text');
define('REQUEST', 'request_type');
define('RESTAURANTS_FOR_LIST_VIEW', 'restaurants_for_list_view');
define('RESTAURANT_LOCATIONS', 'restaurant_locations');
define('RESTAURANT_INFORMATION', 'restaurant_information');
define('OPEN_NOW_FILTER_ACTIVE', '1');
define('OPEN_NOW_FLAG', 'open_now_flag');
define('MIDNIGHT00', "'00:00:00'");
define('MIDNIGHT24', "'24:00:00'");

try {
	/*** Getting data ***/
	$json = file_get_contents("php://input");
	/*** Decoding json ***/
	$data = json_decode($json, TRUE);
	if($data != null){
		if($data[REQUEST] == RESTAURANTS_FOR_LIST_VIEW)
			print(json_encode(getRestaurantsInfoForListView($data[QUERY_TEXT])));
		else if($data[REQUEST] == RESTAURANT_LOCATIONS){
			/*** Checking if is active the open-now flag ***/
			if($data[OPEN_NOW_FLAG] == OPEN_NOW_FILTER_ACTIVE)
				$isOpenNowFilterActive = true;
			else
				$isOpenNowFilterActive = false;
			print(json_encode(getRestaurantLocations($data[ATTR_LONGITUDE], $data[ATTR_LATITUDE], $isOpenNowFilterActive)));
		} else if($data[REQUEST] == RESTAURANT_INFORMATION)
			print(json_encode(getRestaurant($data[ATTR_RESTAURANT_ID])));
		
	}else return null;
	
} catch(Exception $e) {
    echo $e->getMessage();
}

$conn = null;

function getRestaurantsInfoForListView($search_text){
	global $conn;
	if(!empty($search_text)){
		try{
			$stmt = $conn->prepare('SELECT '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.', AVG('.ATTR_RATING.') AS '.RATING.' FROM '.TABLE_RESTAURANTS.' JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' LEFT JOIN '.TABLE_RATINGS.' ON '.TABLE_RATINGS.'.'.ATTR_RESTAURANT_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.' WHERE '.ATTR_RESIDENCE_NAME.' LIKE ? OR '.ATTR_RESTAURANT_NAME.' LIKE ? GROUP BY '.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.' LIMIT 300');
			$search_text = "%$search_text%";
			/*** Binding parameters ***/
			$stmt->bindParam(1, $search_text);
			$stmt->bindParam(2, $search_text);
			/*** If there are some information to change it will execute the insert statement ***/
			$stmt->execute();
			$restaurants[RESTAURANTS] = $stmt->FetchAll(PDO::FETCH_ASSOC);
			return $restaurants;
		}catch(PDOException $e){}
	}
	
	return null;
}

function getRestaurant($restaurantID){
	global $conn;
	try{
		/*** Getting all the restaurant information with the same ID passed ***/
		$stmt = $conn->prepare('SELECT '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.', AVG('.ATTR_RATING.') AS '.RATING.', '.ATTR_RESTAURANT_EMAIL.', '.ATTR_RESTAURANT_PHONE.', '.ATTR_RESTAURANT_DESCRIPTION.', '.ATTR_LATITUDE.', '.ATTR_LONGITUDE.' FROM '.TABLE_RESTAURANTS.' JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' LEFT JOIN '.TABLE_RATINGS.' ON '.TABLE_RATINGS.'.'.ATTR_RESTAURANT_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.' WHERE '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.' = ?');

		$stmt->bindParam(1, $restaurantID);
		$stmt->execute();
		$restaurant[RESTAURANT] = $stmt->fetch(PDO::FETCH_ASSOC);
		
		$stmt = $conn->prepare('SELECT '.ATTR_TYPE.' FROM '.TABLE_TYPOLOGIES.' JOIN '.TABLE_TYPOLOGY_RESTAURANT.' ON '.TABLE_TYPOLOGY_RESTAURANT.'.'.ATTR_TYPE_ID.' = '.TABLE_TYPOLOGIES.'.'.ATTR_TYPE_ID.' WHERE '.ATTR_RESTAURANT_ID.' = ?');
		
		$stmt->bindParam(1, $restaurantID);
		$stmt->execute();
		$restaurant[RESTAURANT][ATTR_TYPE] = $stmt->fetchAll(PDO::FETCH_COLUMN);
		return $restaurant;
	}catch(PDOException $e){
		return null;
	}
}

function getRestaurantLocations($longitude, $latitude, $isOpenNowFilterActive){
	global $conn;
	if(!empty($latitude) && !empty($longitude)){
		if($isOpenNowFilterActive){
			/*** Searching restaurants near me that are opened now ***/
			/*** Getting the date (DAY,HH:II:SS) ***/
			$tmp = split(',', date('N,H:i:s'));
			$day = $tmp[0];
			$hour = $tmp[1];
			
			$stmt = $conn->prepare('SELECT '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.', '.ATTR_LONGITUDE.', '.ATTR_LATITUDE.' FROM '.TABLE_RESTAURANTS.' JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' JOIN '.TABLE_HOURS.' ON '.TABLE_HOURS.'.'.ATTR_RESTAURANT_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.' WHERE '.ATTR_DAY.' = :myDay AND '.ATTR_OPENING.' <= :myHour AND IF('.ATTR_CLOSURE.' = '.MIDNIGHT00.', '.MIDNIGHT24.','.ATTR_CLOSURE.') >= :myHour2 AND SQRT(POW('.ATTR_LATITUDE.' - :latitude , 2 ) + POW('.ATTR_LONGITUDE.' - :longitude , 2)) < '.MIN_DIST.' LIMIT 50');
			/*** Binding parameters ***/
			$stmt->bindParam(':myDay', $day);
			$stmt->bindParam(':myHour', $hour);
			$stmt->bindParam(':myHour2', $hour);
			$stmt->bindParam(':latitude', $latitude);
			$stmt->bindParam(':longitude', $longitude);
		}else{
			$stmt = $conn->prepare('SELECT '.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.', '.ATTR_LONGITUDE.', '.ATTR_LATITUDE.' FROM '.TABLE_RESTAURANTS.' JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' WHERE SQRT(POW('.ATTR_LATITUDE.' - :latitude , 2 ) + POW('.ATTR_LONGITUDE.' - :longitude , 2)) < '.MIN_DIST.' LIMIT 50');
			/*** Binding parameters ***/
			$stmt->bindParam(':latitude', $latitude);
			$stmt->bindParam(':longitude', $longitude);
		}
		$stmt->execute();
		$restaurants[RESTAURANTS] = $stmt->FetchAll(PDO::FETCH_ASSOC);
		return $restaurants;
	}
}
?>