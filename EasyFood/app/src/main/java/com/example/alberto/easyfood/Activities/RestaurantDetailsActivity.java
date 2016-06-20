 package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
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
    private ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        restaurantManager = new RestaurantManager();
        Intent myIntent = getIntent();
        restaurant = (Restaurant) myIntent.getSerializableExtra(RESTAURANT_EXTRA_NAME);
        myListView = (ListView) findViewById(R.id.restaurant_info_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(InternetConnection.haveIInternetConnection(this)){
            restaurant = restaurantManager.getRestaurant(restaurant.get_restaurantID());
            if(restaurant != null){
                ArrayList<Key_Value> restaurantInfo = restaurant.toArrayList(this);
                if(restaurantInfo != null){
                    BindDictionary<Key_Value> restaurantBindDictionary = new BindDictionary<>();
                    restaurantBindDictionary.addStringField(R.id.list_item_key, new StringExtractor<Key_Value>() {
                        @Override
                        public String getStringValue(Key_Value restaurant, int position) {
                            return restaurant.getKey();
                        }
                    });
                    restaurantBindDictionary.addStringField(R.id.txt_list_location, new StringExtractor<Key_Value>() {
                        @Override
                        public String getStringValue(Key_Value restaurant, int position) {
                            return restaurant.getValue();
                        }
                    });

                    FunDapter funAdapter = new FunDapter(this, restaurantInfo, R.layout.key_value_list_item, restaurantBindDictionary);
                    myListView.setAdapter(funAdapter);
                }
            }
        }else{
            Toast.makeText(this, R.string.Missing_internet_connection_for_more_info, Toast.LENGTH_LONG).show();
        }
    }


}
