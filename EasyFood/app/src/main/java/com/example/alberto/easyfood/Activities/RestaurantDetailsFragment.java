package com.example.alberto.easyfood.Activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.alberto.easyfood.GeolocationModule.GeolocationManager;
import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.RestaurantModule.Restaurant;
import com.example.alberto.easyfood.Utilities.Key_Value;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Alberto on 02/07/2016.
 */
public class RestaurantDetailsFragment extends Fragment implements OnMapReadyCallback {
    private static final String RESTAURANT_EXTRA_NAME = "restaurant";
    private Restaurant restaurant;
    //private RestaurantManager restaurantManager;
    private ListView myListView;
    private SupportMapFragment mapFragment;
    private GoogleMap myMap = null;
    private View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializingMap();
        myView = inflater.inflate(R.layout.fragment_restaurant_details, container, false);

        /* Getting restaurant information from the activity holder */
        restaurant = ((RestaurantActivity) getActivity()).getRestaurant();
        myListView = (ListView) myView.findViewById(R.id.restaurant_details_list);

        return myView;
    }

    /**
     * Method that is used to initialize the map if it is a null-object
     */
    private void initializingMap() {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.restaurant_map_container, mapFragment, "mapFragment");
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        displayRestaurantInformation();
        if(GeolocationManager.isLatitudeValid(restaurant.get_latitude()) && GeolocationManager.isLongitudeValid(restaurant.get_longitude()))
            displayRestaurantOnMap();
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
        ArrayList<Key_Value> restaurantInfo = restaurant.toArrayList(RestaurantDetailsFragment.this.getActivity());
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

            FunDapter listAdapter = new FunDapter(RestaurantDetailsFragment.this.getActivity(), restaurantInfo, R.layout.key_value_list_item, restaurantBindDictionary);
            myListView.setAdapter(listAdapter);

        }
    }

}
