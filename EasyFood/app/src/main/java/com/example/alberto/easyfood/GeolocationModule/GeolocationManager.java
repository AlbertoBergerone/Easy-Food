package com.example.alberto.easyfood.GeolocationModule;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by inf.bergeronea1610 on 20/04/2016.
 */
public class GeolocationManager implements LocationListener{
    private LocationManager _locationManager;
    private Context _context;

    public GeolocationManager(Context context) {
        _context = context;
        _locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Method that checks if the GPS is enabled
     * @return (boolean) TRUE -> GPS enabled; FALSE -> GPS not enabled
     */
    public boolean isGPSEnabled() {
        /* Checking if the GPS is enabled */
        return (_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    /**
     * Method that returns my location
     * If user didn't grant permissions (ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), it returns null
     * @return (Location) my position
     */
    public Location getMyPosition() {
        Criteria criteria = new Criteria();
        /* Getting the bast provider near me */
        String provider = _locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = _locationManager.getLastKnownLocation(provider);
            if (location != null){
                onLocationChanged(location);
                return location;
            }
        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}

