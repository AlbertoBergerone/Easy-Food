package com.example.alberto.easyfood.ServerCommunicationModule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alberto on 18/03/2016.
 */
public class InternetConnection extends BroadcastReceiver {

    /**
     * Method which verify if the device is connected to the internet.
     * If it is connected it returns true. Else it returns false.
     */
    public static boolean haveIInternetConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        /* return true if a internet connection is available */
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        haveIInternetConnection(context);
    }
}
