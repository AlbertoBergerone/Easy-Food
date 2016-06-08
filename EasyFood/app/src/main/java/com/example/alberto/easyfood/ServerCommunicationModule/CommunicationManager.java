package com.example.alberto.easyfood.ServerCommunicationModule;


import android.os.StrictMode;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.String.valueOf;

/**
 * Created by inf.bergeronea1610 on 21/03/2016.
 * Class that permits to communicate with a server.
 * Methods:
 *	- JSONObject postData(String, JSONObject);
 */
public class CommunicationManager {
   	private static final String UTF_8 = "UTF-8";
	private static final String TAG = "CommunicationManager";
	private static final String POST_METHOD = "POST";
	private static final int READ_TIMEOUT = 8000; /* 8000 milliseconds */
	private static final int CONNECTION_TIMEOUT = 10000; /* 10000 milliseconds */

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
            StringBuilder stringBuilder = new StringBuilder();
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
                connection.connect();

                /* Getting the response */
                int responseCode = connection.getResponseCode();
                if(responseCode == HttpsURLConnection.HTTP_OK) {
                    /* If the response is a 200 http response it will get the message the server sent */
                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + '\n');
                    }

					jsonObject = new JSONObject(stringBuilder.toString());
                }

                connection.disconnect();
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (JSONException e) {
				Log.e(TAG, "Could not parse malformed JSON: " + e.getMessage() + "\njson string: "+ stringBuilder);
			}

        }
        /* Return a JSON Object*/
		return jsonObject;
	
    }
}
