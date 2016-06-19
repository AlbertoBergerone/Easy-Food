 package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.RestaurantModule.Restaurant;
import com.example.alberto.easyfood.RestaurantModule.RestaurantManager;
import com.example.alberto.easyfood.ServerCommunicationModule.InternetConnection;
import com.example.alberto.easyfood.Utilities.Key_Value;

import java.util.ArrayList;

public class RestaurantDetailsActivity extends AppCompatActivity {
    private static final String TAG = "RestaurantDetailsActivity";
    private static final String RESTAURANT_EXTRA_NAME = "restaurant";
    private Restaurant restaurant;
    private RestaurantManager restaurantManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        restaurantManager = new RestaurantManager();
        Intent myIntent = getIntent();
        restaurant = (Restaurant) myIntent.getSerializableExtra(RESTAURANT_EXTRA_NAME);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(InternetConnection.haveIInternetConnection(this)){
            restaurantManager.getRestaurant(restaurant.get_restaurantID());
            ArrayList<Key_Value> restaurantInfo = restaurant.toArrayList(this);
        }else{
            Toast.makeText(this, R.string.Missing_internet_connection_for_more_info, Toast.LENGTH_LONG).show();
        }
    }


}
