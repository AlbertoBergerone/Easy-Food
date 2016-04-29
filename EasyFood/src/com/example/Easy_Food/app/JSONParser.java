package com.example.Easy_Food.app;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by inf-5ogruppo5 on 21/03/2016.
 */
public class JSONParser {
    private static final String STANDARD_ISO = "iso-8859-1";
    private JSONObject _jsonObject = null;
    private String _json = "";

    /*
    public JSONObject getJSONFromUrl(String url){
        // Making HTTP request
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {}
        catch (ClientProtocolException e) {}
        catch (IOException e) {}

        try{
            // Saving the input stream - received in the HTTP answer - in a String variable
            BufferedReader reader = new BufferedReader(new InputStreamReader(_inputStream, STANDARD_ISO), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Cycle until I read a NULL character
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            _jsonObject = new JSONObject(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return _jsonObject;
    }
*/

	
	/**
	 * Method that sends data to a php page and it returns a JsonArray containig the response data
	 * Parameters:
	 *	- String (url of the php page);
	 *	- ArrayList<NameValuePair> (array containig data to be sent);
	 */
	public static JSONArray send_receiveToWebServer(String url, ArrayList<NameValuePair> dataToBeSent){
		JSONArray jsonArray = null;
		 
		/* http post */
		try{
				HttpClient httpclient = new DefaultHttpClient();
				/* Using post method to send data */
				HttpPost httppost = new HttpPost(url);
				httppost.setEntity(new UrlEncodedFormEntity(dataToBeSent));
				/* Executing the http request */
				HttpResponse response = httpclient.execute(httppost);
				/* Getting the response */
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
		}catch(Exception e){
				Log.e("log_tag", "Error in http connection " + e.toString());
		}
		/* Convert response to string */
		try{
			InputStream inputStream = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, STANDARD_ISO), 8);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			/* Reading response lines */
			while((line = reader.readLine()) != null){
				stringBuilder.append(line + "\n");
			}
			inputStream.close();
			
			/* Parsing the response into a JSON array*/
			jsonArray = new JSONArray(stringBuilder.toString());
		}catch(Exception e){
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		/* Return a JSON Array*/
		return jsonArray;
	}
	
}
