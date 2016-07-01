 package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.alberto.easyfood.GeolocationModule.GeolocationManager;
import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.RestaurantModule.Restaurant;
import com.example.alberto.easyfood.RestaurantModule.RestaurantManager;
import com.example.alberto.easyfood.ServerCommunicationModule.InternetConnection;
import com.example.alberto.easyfood.Utilities.Key_Value;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class RestaurantDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String RESTAURANT_EXTRA_NAME = "restaurant";
    private Restaurant restaurant;
    private RestaurantManager restaurantManager;
    private ListView myListView;
    private SupportMapFragment mapFragment;
    private GoogleMap myMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializingMap();
        setContentView(R.layout.activity_restaurant_details);

        restaurantManager = new RestaurantManager();
        Intent myIntent = getIntent();
        restaurant = (Restaurant) myIntent.getSerializableExtra(RESTAURANT_EXTRA_NAME);
        myListView = (ListView) findViewById(R.id.restaurant_info_list);
        /* Setting the toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        this.setSupportActionBar(toolbar);
        /* Hiding the title of the toolbar */
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        /* Displaying the hamburger icon */
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    private void initializingMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.restaurant_info_map_container, mapFragment, "mapFragment");
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        //myMap.getUiSettings().setScrollGesturesEnabled(false);
        //myMap.getUiSettings().setZoomGesturesEnabled(false);
        if(InternetConnection.haveIInternetConnection(this)){
            Restaurant tmp = restaurantManager.getRestaurant(restaurant.get_restaurantID());
            if(tmp != null){
                restaurant = tmp;
                displayRestaurantInformation();
                displayRestaurantOnMap();
            }
        }else{
            Toast.makeText(this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void displayRestaurantOnMap(){
        if(myMap != null) {
            /* Clearing the map */
            myMap.clear();
            if(GeolocationManager.isLatitudeValid(restaurant.get_latitude()) && GeolocationManager.isLongitudeValid(restaurant.get_longitude())){
                /* Adding the marker to the map */
                BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker);
                MarkerOptions marker = new MarkerOptions()
                        .position(restaurant.get_position())
                        .title(restaurant.get_restaurantName())
                        .icon(markerIcon);
                myMap.addMarker(marker);
                float zoom = 15;
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurant.get_position(), zoom));
            }
        }
    }

    private void displayRestaurantInformation(){
        ArrayList<Key_Value> restaurantInfo = restaurant.toArrayList(RestaurantDetailsActivity.this.getApplicationContext());
        if(restaurantInfo != null){
            BindDictionary<Key_Value> restaurantBindDictionary = new BindDictionary<>();
            restaurantBindDictionary.addStringField(R.id.list_item_key, new StringExtractor<Key_Value>() {
                @Override
                public String getStringValue(Key_Value restaurant, int position) {
                    return restaurant.getKey();
                }
            });
            restaurantBindDictionary.addStringField(R.id.list_item_value, new StringExtractor<Key_Value>() {
                @Override
                public String getStringValue(Key_Value restaurant, int position) {
                    return restaurant.getValue();
                }
            });

            FunDapter listAdapter = new FunDapter(RestaurantDetailsActivity.this.getApplicationContext(), restaurantInfo, R.layout.key_value_list_item, restaurantBindDictionary);
            myListView.setAdapter(listAdapter);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
