package com.example.Easy_Food.app.activities;

import android.app.Activity;
import android.os.Bundle;
import com.example.Easy_Food.app.R;
import com.example.Easy_Food.app.Restaurant;

/**
 * Created by inf.bergeronea1610 on 20/04/2016.
 */
public class RestaurantActivity extends Activity{
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_informations);
        /* getting the extras which were passed with the intent */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String restaurantJson;
            restaurantJson = extras.getString("selectedRestaurant");
            Restaurant myObject = new Gson().fromJson(restaurantJson, Restaurant.class);
        }else{
            /* there is something wrong */
        }

    }
}
