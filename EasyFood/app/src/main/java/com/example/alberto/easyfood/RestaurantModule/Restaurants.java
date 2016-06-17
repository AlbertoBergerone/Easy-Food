package com.example.alberto.easyfood.RestaurantModule;

import android.util.Log;

import com.example.alberto.easyfood.ServerCommunicationModule.CommunicationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alberto on 14/06/2016.
 */
public class Restaurants {
    private static final String TAG = "Restaurants";
    /* URL of the server */
    private static final String SERVER_URL = "http://185.51.138.52:5005/serverForApplication/restaurant_info.php";
    public ArrayList<Restaurant> getRestaurantsFromServer(String stringForQuery){
        try {
            JSONObject json = CommunicationManager.postData(SERVER_URL, new JSONObject(stringForQuery));
            if(json != null){
                fromJSONtoRestaurantArray(json);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Malformed json string: " + e.getMessage());
        }
        return null;
    }

    private ArrayList<Restaurant> fromJSONtoRestaurantArray(JSONObject json) {
        //for (int i = 0; i < json.getJSONArray("I_DONT_KNOW"); i++)
        return null;
    }
}
