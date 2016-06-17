package com.example.alberto.easyfood.ServerCommunicationModule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alberto on 18/03/2016.
 */
public class InternetConnection {
    private static final String wifiString = "WIFI";
    private static final String mobileString = "MOBILE";

    /**
     * Method which verify if the device is connected to the internet.
     * If it is connected it returns true. Else it returns false.
     */
	 /*
    public static boolean haveIInternetConnection(Context context){
        boolean haveWiFiConnection = false;
        boolean haveMobileConnection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo net_info : networkInfo){
            if(net_info.getTypeName().equalsIgnoreCase(wifiString))
                /* Verified if the device is connected to a WiFi connection 
                if(net_info.isConnected())
                    haveWiFiConnection = true;
            if(net_info.getTypeName().equalsIgnoreCase(mobileString))
                /* Verified if the device is connected to a WiFi connection 
                if(net_info.isConnected())
                    haveMobileConnection = true;
        }
        /* return true if the mobile is connected to the internet 
        return haveMobileConnection || haveWiFiConnection;
    }
	*/
	
	public static boolean haveINetworkTypeConnection(Context context, String type){
		boolean haveConnection = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo net_info : networkInfo){
			if(net_info.getTypeName().equalsIgnoreCase(type)){
				haveConnection = true;
			}
		}
		return haveConnection;
	}
	
	
    /**
     * Method which verify if the device is connected to the internet.
     * If it is connected it returns true. Else it returns false.
     */
    public static boolean haveIInternetConnection(Context context){
        boolean haveWiFiConnection = false;
        boolean haveMobileConnection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        /* return true if a internet connection is available */
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
