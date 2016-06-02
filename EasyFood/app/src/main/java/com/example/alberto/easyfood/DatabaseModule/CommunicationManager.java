package com.example.alberto.easyfood.DatabaseModule;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.example.alberto.easyfood.R;


import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.String.valueOf;

/**
 * Created by inf-5ogruppo5 on 21/03/2016.
 */
public class CommunicationManager {
   	//private static final String STANDARD_ISO = "iso-8859-1";
   	private static final String UTF_8 = "UTF-8";
	private static final String TAG = "CommunicationManager";
	private static final String POST_METHOD = "POST";
	private static final int READ_TIMEOUT = 10000;
	private static final int CONNECTION_TIMEOUT = 15000;

	/**
	 * Method that sends data using post method to a php page and it returns a JsonArray containig the response data
	 * Parameters:
	 *	- String (url of the php page);
	 *	- JSONObject  containing data to be sent;
	 * Return:
	 * 	- JSONObject containing the server response
	 */
	public static JSONObject postData(String url, JSONObject dataToBeSent){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		JSONObject jsonObject = null;
        if(dataToBeSent != null) {
            URL serverURL = null;
            String response = "";
            try {
                serverURL = new URL(url);
                /* Opening a connection */
                HttpURLConnection connection = (HttpURLConnection) serverURL.openConnection();
                /* Setting timeout */
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                /* Using the POST method sending data */
                connection.setRequestMethod(POST_METHOD);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(dataToBeSent.toString());
                outputStream.flush();
                outputStream.close();
                outputStream.close();
                connection.connect();

                /* Getting the response */
                int responseCode = connection.getResponseCode();
                if(responseCode == HttpsURLConnection.HTTP_OK) {
                    /* If the response is a 200 http response it will get the message the server sent */
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    response = stringBuilder.toString();
                }

                connection.disconnect();
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }

        }
        /* Return a JSON Object*/
		return jsonObject;

	/*http://stackoverflow.com/questions/20298656/android-error-parsing-json-end-of-input-at-character-0-of

	}*/
	
    }
/*
    @Override
    protected JSONObject doInBackground(UrlAndJSON... urlAndJson){
        JSONObject jsonObject = null;
        if(urlAndJson[0].getJson() != null && urlAndJson[0].getUrl() != null) {
            URL serverURL = null;
            String response = "";
            try {
                serverURL = new URL(urlAndJson[0].getUrl());

                HttpURLConnection connection = (HttpURLConnection) serverURL.openConnection();

                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.setRequestMethod(POST_METHOD);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(urlAndJson[0].getJson().toString());
                outputStream.flush();
                outputStream.close();
                outputStream.close();
                connection.connect();


                int responseCode = connection.getResponseCode();
                if(responseCode == HttpsURLConnection.HTTP_OK) {

                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    response = stringBuilder.toString();
                }

                connection.disconnect();
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }

        }

        return jsonObject;
    }*/
}
