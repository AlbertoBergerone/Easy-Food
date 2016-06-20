package com.example.alberto.easyfood.RestaurantModule;

import android.util.Log;

import com.example.alberto.easyfood.ServerCommunicationModule.CommunicationManager;
import com.example.alberto.easyfood.Utilities.DB_Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alberto on 14/06/2016.
 */
public class RestaurantManager {
    private static final String TAG = "RestaurantManager";
    /* URL of the server */
    private static final String SERVER_URL = "http://185.51.138.52:5005/serverForApplication/restaurant_info.php";
    private static final String RESTAURANTS = "ristoranti";
    private static final String RESTAURANT = "ristorante";
    private static final String QUERY_TEXT = "query_text";
    private static final String REQUEST = "request_type";
    private static final String RESTAURANTS_FOR_LIST_VIEW = "restaurants_for_list_view";
    private static final String RESTAURANT_INFORMATION = "restaurant_information";
    private static final String RESTAURANT_LOCATIONS = "restaurant_locations";

    public ArrayList<Restaurant> getRestaurantsForListView(String stringForQuery){
        ArrayList<Restaurant> restaurants = null;
        try {
            JSONObject json = new JSONObject();
            json.put(REQUEST, RESTAURANTS_FOR_LIST_VIEW);
            json.put(QUERY_TEXT, stringForQuery);
            json = CommunicationManager.postData(SERVER_URL, json);
            if(json != null){
                restaurants = fromJSONtoRestaurantArray(json);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Malformed json string: " + e.getMessage());
        }
        return restaurants;
    }

    private ArrayList<Restaurant> fromJSONtoRestaurantArray(JSONObject json) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        if(json != null) {
            try {
                JSONArray jsonArray = json.getJSONArray(RESTAURANTS);
                for (int i = 0; i < jsonArray.length(); i++){
                    Restaurant restaurant = new  Restaurant();

                    if(!jsonArray.getJSONObject(i).isNull(DB_Attributes.DB_RESTAURANT_ID))
                        restaurant.set_restaurantID(jsonArray.getJSONObject(i).getInt(DB_Attributes.DB_RESTAURANT_ID));

                    if(!jsonArray.getJSONObject(i).isNull(DB_Attributes.DB_RESTAURANT_NAME))
                        restaurant.set_restaurantName(jsonArray.getJSONObject(i).getString(DB_Attributes.DB_RESTAURANT_NAME));

                    if(!jsonArray.getJSONObject(i).isNull(DB_Attributes.DB_RESTAURANT_ADDRESS))
                        restaurant.set_address(jsonArray.getJSONObject(i).getString(DB_Attributes.DB_RESTAURANT_ADDRESS));

                    if(!jsonArray.getJSONObject(i).isNull(DB_Attributes.DB_RESIDENCE))
                        restaurant.set_residence(jsonArray.getJSONObject(i).getString(DB_Attributes.DB_RESIDENCE));

                    if(!jsonArray.getJSONObject(i).isNull(DB_Attributes.DB_PROVINCE_ID))
                        restaurant.set_provinceID(jsonArray.getJSONObject(i).getString(DB_Attributes.DB_PROVINCE_ID));

                    if(!jsonArray.getJSONObject(i).isNull(DB_Attributes.DB_RESTAURANT_RATING))
                        restaurant.set_rating(jsonArray.getJSONObject(i).getDouble(DB_Attributes.DB_RESTAURANT_RATING));

                    if(!jsonArray.getJSONObject(i).isNull(DB_Attributes.DB_LATITUDE))
                        restaurant.set_latitude(jsonArray.getJSONObject(i).getDouble(DB_Attributes.DB_LATITUDE));

                    if(!jsonArray.getJSONObject(i).isNull(DB_Attributes.DB_LONGITUDE))
                        restaurant.set_longitude(jsonArray.getJSONObject(i).getDouble(DB_Attributes.DB_LONGITUDE));

                    restaurants.add(restaurant);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error in getting restaurants from the json: " + json.toString() + e.getMessage());
            }
        }
        return restaurants;
    }

    public Restaurant getRestaurant(int restaurantID) {
        Restaurant retRestaurant = null;
        Log.e(TAG, String.valueOf(restaurantID));
        try {
            JSONObject json = new JSONObject();
            json.put(REQUEST, RESTAURANT_INFORMATION);
            json.put(DB_Attributes.DB_RESTAURANT_ID, restaurantID);
            json = CommunicationManager.postData(SERVER_URL, json);
            if(json != null){
                retRestaurant = fromJSONtoRestaurant(json.getJSONObject(RESTAURANT));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Malformed json string: " + e.getMessage());
        }
        return retRestaurant;
    }

    private Restaurant fromJSONtoRestaurant(JSONObject json) {
        Restaurant ret = new Restaurant();
        try {
            if(!json.isNull(DB_Attributes.DB_RESTAURANT_ID))
                ret.set_restaurantID(json.getInt(DB_Attributes.DB_RESTAURANT_ID));
            if(!json.isNull(DB_Attributes.DB_RESTAURANT_NAME))
                ret.set_restaurantName(json.getString(DB_Attributes.DB_RESTAURANT_NAME));
            if(!json.isNull(DB_Attributes.DB_RESTAURANT_ADDRESS))
                ret.set_address(json.getString(DB_Attributes.DB_RESTAURANT_ADDRESS));
            if(!json.isNull(DB_Attributes.DB_RESIDENCE))
                ret.set_residence(json.getString(DB_Attributes.DB_RESIDENCE));
            if(!json.isNull(DB_Attributes.DB_PROVINCE_ID))
                ret.set_provinceID(json.getString(DB_Attributes.DB_PROVINCE_ID));
            if(!json.isNull(DB_Attributes.DB_LATITUDE))
                ret.set_latitude(json.getDouble(DB_Attributes.DB_LATITUDE));
            if(!json.isNull(DB_Attributes.DB_LONGITUDE))
                ret.set_longitude(json.getDouble(DB_Attributes.DB_LONGITUDE));
            if(!json.isNull(DB_Attributes.DB_RESTAURANT_EMAIL))
                ret.set_email(json.getString(DB_Attributes.DB_RESTAURANT_EMAIL));
            if(!json.isNull(DB_Attributes.DB_RESTAURANT_PHONE))
                ret.set_phoneNumber(json.getString(DB_Attributes.DB_RESTAURANT_PHONE));
            if(!json.isNull(DB_Attributes.DB_RESTAURANT_DESCRIPTION))
                ret.set_description(json.getString(DB_Attributes.DB_RESTAURANT_DESCRIPTION));
            if(!json.isNull(DB_Attributes.DB_RESTAURANT_RATING))
                ret.set_rating(json.getDouble(DB_Attributes.DB_RESTAURANT_RATING));
            if(!json.isNull(DB_Attributes.DB_RESTAURANT_TYPE)){
                JSONArray types = json.getJSONArray(DB_Attributes.DB_RESTAURANT_TYPE);
                for(int i = 0; i < types.length(); i++)
                    ret.addType(types.getString(i));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Malformed json string: " + json.toString() + e.getMessage());
        }
        return ret;
    }
}
