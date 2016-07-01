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

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by inf.bergeronea1610 on 20/04/2016.
 */
public class GeolocationManager {
    /**
     * Method that checks if the GPS is enabled
     * @return (boolean) TRUE -> GPS enabled; FALSE -> GPS not enabled
     */
    public static boolean isGPSEnabled(Context context) {
        /* Checking if the GPS is enabled */
        return (((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    public static boolean isLatitudeValid(double latitude){
        return (latitude >= -90 && latitude <= 90);
    }

    public static boolean isLongitudeValid(double longitude){
        return (longitude >= -180 && longitude <= 180);
    }
}

