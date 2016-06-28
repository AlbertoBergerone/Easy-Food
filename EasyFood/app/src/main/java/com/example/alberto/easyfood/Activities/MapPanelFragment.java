package com.example.alberto.easyfood.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alberto.easyfood.GeolocationModule.GeolocationManager;
import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.RestaurantModule.Restaurant;
import com.example.alberto.easyfood.RestaurantModule.RestaurantManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Alberto on 30/05/2016.
 */
public class MapPanelFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MapPanelFragment";
    private GoogleMap myMap;
    private SupportMapFragment mapFragment;
    private View myView;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Marker> markers;
    private GoogleApiClient myGoogleApiClient;
    Location myLastLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializingMap();
        myView = inflater.inflate(R.layout.fragment_map_panel, container, false);
        markers = new ArrayList<>();
        /* Setting the toolbar */
        Toolbar toolbar = (Toolbar) myView.findViewById(R.id.default_toolbar);
        ((HomeActivity)MapPanelFragment.this.getActivity()).setSupportActionBar(toolbar);
        /* Hiding the title of the toolbar */
        ((HomeActivity)MapPanelFragment.this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        /* Displaying the hamburger icon */
        ((HomeActivity)MapPanelFragment.this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity)MapPanelFragment.this.getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        setHasOptionsMenu(true);

        buildGoogleApiClient();
        if (myGoogleApiClient != null)
            myGoogleApiClient.connect();
        else
            Toast.makeText(this.getActivity(), "connection failed cause null object", Toast.LENGTH_LONG).show();

        return myView;
    }

    private void initializingMap() {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.map_container, mapFragment, "mapFragment");
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.default_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!GeolocationManager.isGPSEnabled(this.getActivity())){
            Toast.makeText(MapPanelFragment.this.getActivity(), R.string.gps_not_enabled, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            myMap = googleMap;
            /* Checking if permission are granted */
            if (ActivityCompat.checkSelfPermission(MapPanelFragment.this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapPanelFragment.this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        myLastLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleApiClient);

        if(myLastLocation != null)
            /* Getting - with a thread - an array list containing restaurant locations that are near me */
            new GetRestaurantLocations().execute(new LatLng(myLastLocation.getLatitude(), myLastLocation.getLongitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this.getActivity(), "connection suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this.getActivity(), "connection failed", Toast.LENGTH_LONG).show();
    }

    protected synchronized void buildGoogleApiClient(){
        if(myGoogleApiClient == null){
            myGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private class GetRestaurantLocations extends AsyncTask<LatLng, Void, ArrayList<Restaurant>>{

        @Override
        protected ArrayList<Restaurant> doInBackground(LatLng... myLatsLngs) {
            ArrayList<Restaurant> restaurantsArray = null;
            if(myLatsLngs != null) {
                /* Communicating with the server */
                RestaurantManager restaurantManager = new RestaurantManager();
                restaurantsArray = restaurantManager.getRestaurantLocations(myLatsLngs[0]);
            }
            return restaurantsArray;
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurantsArray) {
            /* when I get the restaurants array I will show their locations on the map if it isn't null */
            if(restaurantsArray != null) {
                restaurants = restaurantsArray;
                addMarkersToMap();
            } else
                Toast.makeText(getActivity(), R.string.No_restaurant_found_near_you, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Method that removes all the markers and then adds to the Map new markers with locations taken from the restaurant array
     */
    private void addMarkersToMap(){
        /* Clearing the map */
        myMap.clear();
        /* Clearing the map */
        markers.clear();
        /* Adding the markers to the map */
        for(int i = 0; i < restaurants.size(); i++){
            markers.add(myMap.addMarker(new MarkerOptions()
                    .position(restaurants.get(i).get_position())
                    .snippet(restaurants.get(i).get_full_address())
                    .title(restaurants.get(i).get_restaurantName())));
            Log.e(TAG, markers.get(i).toString());
        }
    }

}
