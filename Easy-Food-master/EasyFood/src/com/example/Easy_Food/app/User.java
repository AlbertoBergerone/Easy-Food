package com.example.Easy_Food.app;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Alberto on 13/04/2016.
 */
public class User {
    private int _userID;
    private String _username;
    private String _password;
    private String _name;
    private String _last_name;
    private String _email;
	private String _address;
	private String _phone;
	private String _region;
	private String _province;
	private String _municipality;

	/* Constructors */
    public User(String username, String password){
        this._username = username;
        this._password = password;
    }
    public User(String username, String password, String name, String last_name, String email){
        this._username = username;
        this._password = password;
        this._name = name;
        this._last_name = last_name;
        this._email = email;
    }

	
    /**
     * Method that contacts a php page to authenticate the user.
     * Return:
	 * False --> The user doesn't exist
	 * True  --> The user exists and now he is logged
	 */
    public boolean loginMe(){
		boolean amILogged = false;
		/* User information to send */
		ArrayList<NameValuePair> loginInformation = new ArrayList<NameValuePair>();
		
		loginInformation.add(new BasicNameValuePair(String.valueOf(R.string.REQUEST_TYPE), R.constants.LOGIN_REQUEST));
		loginInformation.add(new BasicNameValuePair(String.valueOf(R.string.DB_USERNAME), this._username));
		loginInformation.add(new BasicNameValuePair(String.valueOf(R.string.DB_PASSWORD), this._password));
		/* Sending username and password to get other information if the user exist */
		JsonArray userInfo = JSONParser.send_receiveToWebServer(String.valueOf(R.string.URL_LOGIN_SIGNUP), loginInformation);
		if(userInfo != null){
			try{
				JSONObject json_data = userInfo.getJSONObject(0);
                /* Getting user information */
				this._name = json_data.getString(String.valueOf(R.string.DB_NAME));
                this._last_name = json_data.getString(String.valueOf(R.string.DB_LAST_NAME));
                this._address = json_data.getString(String.valueOf(R.string.DB_ADDRESS));
                this._phone = json_data.getString(String.valueOf(R.string.DB_PHONE));
                this._userID = json_data.getInt(String.valueOf(R.string.DB_USER_ID));
                this._region = json_data.getString(String.valueOf(R.string.DB_REGION));
                this._province = json_data.getString(String.valueOf(R.string.DB_PROVINCE));
                this._municipality = json_data.getString(String.valueOf(R.string.DB_MUNICIPALITY));
				
				/* The login was successful */
				amILogged = true;
			}catch(JSONException e){
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		}
		return amILogged;
    }
	
	public boolean updateMe(){
		boolean wasTheDBUpdated;
		ArrayList<NameValuePair> userInfo = new ArrayList<NameValuePair>();
        userInfo.add(new BasicNameValuePair(String.valueOf(R.string.REQUEST_TYPE), String.valueOf(R.string.USER_UPDATE_REQUEST));
        userInfo.add(new BasicNameValuePair(String.valueOf(R.string.DB_USERNAME), this._username));
        userInfo.add(new BasicNameValuePair(String.valueOf(R.string.DB_PASSWORD), this._password));
	}
	
    /**
     * Method that contacts a php page to sign up the user.
     * If the user is correctly signed up it will return TRUE, after it has saved the information in the database. If they are invalid the user hasn't been signed up and it returns FALSE
	 * return:
	 *  0 --> The user has been signed successful
	 * -1 --> There wasn't all basic information 
	 * -2 --> The username is already used
	 * -3 --> The email is already used
	 */
    public int signupMe(){
		int ret = -1;
		/* User information to send */
		ArrayList<NameValuePair> signupInformation = new ArrayList<NameValuePair>();
		
		signupInformation.add(new BasicNameValuePair(String.valueOf(R.string.REQUEST_TYPE), String.valueOf(R.string.SIGNUP_REQUEST)));
		signupInformation.add(new BasicNameValuePair(String.valueOf(R.string.DB_NAME), this._name));
		signupInformation.add(new BasicNameValuePair(String.valueOf(R.string.DB_LAST_NAME), this._last_name));
		signupInformation.add(new BasicNameValuePair(R.constants.DB_EMAIL), this._email));
		signupInformation.add(new BasicNameValuePair(String.valueOf(R.string.DB_USERNAME), this._username));
		signupInformation.add(new BasicNameValuePair(String.valueOf(R.string.DB_PASSWORD), this._password));
		/* Sending name, last name, username, password and email for sign up the user */
		JsonArray userInfo = JSONParser.send_receiveToWebServer(R.constants.URL_LOGIN_SIGNUP, signupInformation);
		if(userInfo != null){
			try{
				JSONObject json_data = userInfo.getJSONObject(0);
                /* Getting return message */
				ret = json_data.getInt(String.valueOf(R.string.SIGNUP_RESPONSE));
			}catch(JSONException e){
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		}
		return ret;
    }

	
    /* Getters */
    public String getUsername() {
        return _username;
    }
    public String get_password() {
        return _password;
    }
    public String get_name() {
        return _name;
    }
    public String get_last_name() {
        return _last_name;
    }
    public String get_email() {
        return _email;
    }
    public int get_userID() {
        return _userID;
    }
    public String get_username() {
        return _username;
    }
    public String get_address() {
        return _address;
    }
    public String get_phone() {
        return _phone;
    }
    public String get_region() {
        return _region;
    }
    public String get_province() {
        return _province;
    }
    public String get_municipality() {
        return _municipality;
    }

}

