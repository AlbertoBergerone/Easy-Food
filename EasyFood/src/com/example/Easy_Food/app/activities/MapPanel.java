package com.example.Easy_Food.app.activities;

import android.app.Activity;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.example.Easy_Food.app.GeolocationListener;
import com.example.Easy_Food.app.R;

import java.util.Locale;

/**
 * Created by inf.bergeronea1610 on 20/04/2016.
 * http://www.corsoandroid.it/passare_parametri_tra_due_activity_con_intent_espliciti.html
 */
public class MapPanel extends Activity implements OnMapReadyCallback {
    private String gpsProviderId = LocationManager.GPS_PROVIDER;
    private Geocoder geo = null;
    private LocationManager locationManager=null;
    private static final int MIN_DIST=20;
    private static final int MIN_PERIOD=20000;

    private LocationListener locationListener = new GeolocationListener(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        geo = new Geocoder(this, Locale.getDefault());
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        locationManager.requestLocationUpdates(gpsProviderId, MIN_PERIOD,MIN_DIST, locationListener);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (locationManager!=null)
            locationManager.removeUpdates(locationListener);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }
}