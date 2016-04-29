package com.example.Easy_Food.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by inf.bergeronea1610 on 20/04/2016.
 */
public class GeolocationListener implements LocationListener{
    private Context _context = null;

    public GeolocationListener(Context context) {
        this._context = context;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(_context, R.string.missing_geolocalization, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

