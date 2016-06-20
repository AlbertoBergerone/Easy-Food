<?php

/*** Opening the connection with the db ***/
include 'open_db_connection.php';
define('MIN_DIST', '0.2');
define('RESTAURANTS', 'ristoranti');
define('RESTAURANT', 'ristorante');
define('QUERY_TEXT', 'query_text');
define('REQUEST', 'request_type');
define('RESTAURANTS_FOR_LIST_VIEW', 'restaurants_for_list_view');
define('RESTAURANT_LOCATIONS', 'restaurant_locations');
define('RESTAURANT_INFORMATION', 'restaurant_information');

try {
	/*** Getting data ***/
	$json = file_get_contents("php://input");
	/*** Decoding json ***/
	$data = json_decode($json, TRUE);
	if($data != null){
		if($data[REQUEST] == RESTAURANTS_FOR_LIST_VIEW)
			print(json_encode(getRestaurantsInfoForListView($data[QUERY_TEXT])));
		else if($data[REQUEST] == RESTAURANT_LOCATIONS)
			print(json_encode(getRestaurantLocations($data[ATTR_LONGITUDE], $data[ATTR_LATITUDE])));
		else if($data[REQUEST] == RESTAURANT_INFORMATION){
			var_dump($data);
			print(json_encode(getRestaurant($data[ATTR_RESTAURANT_ID])));
		}
	}else return null;
	
} catch(Exception $e) {
    echo $e->getMessage();
}

$conn = null;

function getRestaurantsInfoForListView($search_text){
	global $conn;
	if(!empty($search_text)){
		$stmt = $conn->prepare('SELECT '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.', AVG('.ATTR_RATING.') AS '.ATTR_RATING.' FROM '.TABLE_RESTAURANTS.' JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' LEFT JOIN '.TABLE_RATINGS.' ON '.TABLE_RATINGS.'.'.ATTR_RESTAURANT_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.' WHERE '.ATTR_RESIDENCE_NAME.' LIKE ? OR '.ATTR_RESTAURANT_NAME.' LIKE ? GROUP BY '.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID);
		$search_text = "%$search_text%";
		/*** Binding parameters ***/
		$stmt->bindParam(1, $search_text);
		$stmt->bindParam(2, $search_text);
		try{
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
	if(!empty($restaurantID)){
		try{
			/*** Getting all the restaurant information with the same ID passed ***/
			$stmt = $conn->prepare('SELECT '.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.', AVG('.ATTR_RATING.') AS '.ATTR_RATING.', '.ATTR_RESTAURANT_EMAIL.', '.ATTR_RESTAURANT_PHONE.', '.ATTR_RESTAURANT_DESCRIPTION.', '.ATTR_LATITUDE.', '.ATTR_LONGITUDE.', '.' FROM'.TABLE_RESTAURANTS.' JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' JOIN '.TABLE_RATINGS.' ON '.TABLE_RATINGS.'.'.ATTR_RESTAURANT_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_RESTAURANT_ID.' WHERE '.ATTR_RESTAURANT_ID.' = ?');
			$stmt->bindParam(1, $restaurantID);
			$stmt->execute();
			$restaurant[RESTAURANT] = $stmt->FetchAll(PDO::FETCH_ASSOC);
			
			$stmt->prepare('SELECT '.ATTR_TYPE.' FROM'.TABLE_TYPOLOGIES.' JOIN '.TABLE_TYPOLOGY_RESTAURANT.' ON '.TABLE_TYPOLOGY_RESTAURANT.'.'.ATTR_TYPE_ID.' = '.TABLE_TYPOLOGIES.'.'.ATTR_TYPE_ID.' WHERE '.ATTR_RESTAURANT_ID.' = ?');
			$stmt->bindParam(1, $restaurantID);
			$stmt->execute();
			$restaurant[RESTAURANT][ATTR_TYPE] = $stmt->FetchAll(PDO::FETCH_ASSOC);
			return $restaurant;
		}catch(PDOException $e){}
	}else{
		return null;
	}
}

function getRestaurantLocations($longitude, $latitude){
	global $conn;
	if(!empty($latitude) && !empty($longitude)){
		$stmt->prepare('SELECT '.ATTR_RESTAURANT_ID.', '.ATTR_RESTAURANT_NAME.', '.ATTR_RESTAURANT_ADDRESS.', '.ATTR_RESIDENCE_NAME.', '.ATTR_PROVINCE_ID.', '.ATTR_LONGITUDE.', '.ATTR_LATITUDE.' FROM'.TABLE_RESTAURANTS.' JOIN '.TABLE_RESIDENCES.' ON '.TABLE_RESIDENCES.'.'.ATTR_CADASTRAL_ID.' = '.TABLE_RESTAURANTS.'.'.ATTR_CADASTRAL_ID.' HAVING SQRT(POW('.ATTR_LATITUDE.' - :latitude) + POW('.ATTR_LONGITUDE.' - :longitude)) < '.MIN_DIST);
		
		$stmt->bindParam(':latitude', $latitude);
		$stmt->bindParam(':longitude', $longitude);
		$stmt->execute();
		$restaurants[RESTAURANTS] = $stmt->FetchAll(PDO::FETCH_ASSOC);
		return $restaurants;
	}
}
?>