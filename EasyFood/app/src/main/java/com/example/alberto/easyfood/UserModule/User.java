package com.example.alberto.easyfood.UserModule;

import android.util.Log;

import com.example.alberto.easyfood.DatabaseModule.CommunicationManager;
import com.example.alberto.easyfood.DatabaseModule.UrlAndJSON;
import com.example.alberto.easyfood.R;

import org.json.JSONException;
import org.json.JSONObject;


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
    /*** Location which identifies actual user position ***/
    private final String TAG = "User";
    /* Constants for communicate with the server */
    private final String URL_LOGIN_SIGNUP = "http://185.51.138.52:5005/serverForApplication/login_signup.php";
    private final String LOGIN_REQUEST = "login_request";
    private final String SIGNUP_REQUEST = "sigup_request";
    private final String USER_UPDATE_REQUEST = "user_update_request";
    private final String SERVER_RESPONSE = "response";
    private final String REQUEST_TYPE = "request_type";
    private final String USER = "user";
    private final String ADDED = "added";
    private final String NOT_ADDED = "not_added";
    private final String DB_USERNAME = "username";
    private final String DB_PASSWORD = "password";
    private final String DB_NAME = "nome";
    private final String DB_LAST_NAME = "cognome";
    private final String DB_USER_EMAIL = "emailUtente";
    private final String DB_ADDRESS = "indirizzo";
    private final String DB_USER_PHONE = "telUtente";
    private final String DB_USER_ID = "codUente";
    private final String DB_REGION = "nomeRegione";
    private final String DB_PROVINCE = "nomeProvincia";
    private final String DB_MUNICIPALITY = "nomeComune";


	/* Constructors */
    /**
     * Constructor
     * @param username (username)
     * @param password (user password)
     */
    public User(String username, String password){
        this._username = username;
        this._password = password;
    }

    /**
     * Constructor
     * @param username (String) username
     * @param password (String) password
     * @param name (String) name
     * @param last_name (String) last name
     * @param email (String) email
     */
    public User(String username, String password, String name, String last_name, String email){
        this._username = username;
        this._password = password;
        this._name = name;
        this._last_name = last_name;
        this._email = email;
    }



    /**
     * Method that contacts a php page to authenticate the user.
     * @return
     * False --> The user doesn't exist
     * True  --> The user exists and now he is logged
     */
    public boolean loginMe(){
		boolean amILogged = false;
		/* User information to send */
		/*
		ArrayList<NameValuePair> loginInformation = new ArrayList<NameValuePair>();
		loginInformation.add(new BasicNameValuePair(valueOf(R.string.REQUEST_TYPE), valueOf(R.string.LOGIN_REQUEST)));
		loginInformation.add(new BasicNameValuePair(valueOf(R.string.DB_USERNAME), this._username));
		loginInformation.add(new BasicNameValuePair(valueOf(R.string.DB_PASSWORD), this._password));
        */
		/* Sending username a nd password to get other information if the user exist */
		JSONObject userInfo = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(LOGIN_REQUEST));
        if(userInfo != null){
			try{
                /* Getting user information */
                fromJSON(userInfo.getJSONObject(USER));
                /*
				this._name = json_data.getString(valueOf(R.string.DB_NAME));
                this._last_name = json_data.getString(valueOf(R.string.DB_LAST_NAME));
                this._address = json_data.getString(valueOf(R.string.DB_ADDRESS));
                this._phone = json_data.getString(valueOf(R.string.DB_USER_PHONE));
                this._userID = json_data.getInt(valueOf(R.string.DB_USER_ID));
                this._region = json_data.getString(valueOf(R.string.DB_REGION));
                this._province = json_data.getString(valueOf(R.string.DB_PROVINCE));
                this._municipality = json_data.getString(valueOf(R.string.DB_MUNICIPALITY));
                */
				
				/* The login was successful */
				amILogged = true;
			}catch(JSONException e){
				Log.e(TAG, "Error parsing data " + e.toString());
			}
		}
		return amILogged;
    }
	
    /**
     * Method that contacts a php page to sign up the user.
     * @return
     *  True  --> The user has been signed successful
     *  False --> The user ha not been signed
     */
    public boolean signupMe(){
        boolean AmISigned = false;
		/* User information to send */
		/*
		ArrayList<NameValuePair> signupInformation = new ArrayList<NameValuePair>();
		signupInformation.add(new BasicNameValuePair(valueOf(R.string.REQUEST_TYPE), valueOf(R.string.SIGNUP_REQUEST)));
		signupInformation.add(new BasicNameValuePair(valueOf(R.string.DB_NAME), this._name));
		signupInformation.add(new BasicNameValuePair(valueOf(R.string.DB_LAST_NAME), this._last_name));
		signupInformation.add(new BasicNameValuePair(valueOf(R.string.DB_USER_EMAIL), this._email));
		signupInformation.add(new BasicNameValuePair(valueOf(R.string.DB_USERNAME), this._username));
		signupInformation.add(new BasicNameValuePair(valueOf(R.string.DB_PASSWORD), this._password));
		*/
		/* Sending user information and getting the response */
        JSONObject response = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(SIGNUP_REQUEST));
		if(response != null){
			try{
				JSONObject json_data = response.getJSONObject(USER);
                /* Getting return message */
                if(json_data.getString(SERVER_RESPONSE).equals(ADDED)){
                    /* The user has been signed */
                    int id;
                    if((id = json_data.getInt(DB_USER_ID)) > 0){
                        /* If the user ID is a valid number, it replace the current id (that might be equals to 0) */
                        this._userID = id;
                        /* The user has been signed correctly */
                        AmISigned = true;
                    }
                }else{
                    /* The user has not been signed */
                    /* Checking what is the problem */
                    if(json_data.getString(DB_USER_EMAIL).isEmpty()){
                        /* The email is invalid */
                        this._email = null;
                    }
                    if(json_data.getString(DB_USERNAME).isEmpty()){
                        /* The username is already used */
                        this._username = null;
                    }
                }
			}catch(JSONException e){
				Log.e(TAG, "Error parsing data " + e.toString());
			}
		}else{
            Log.e(TAG, "Error receiving data. The server returned null");
        }
        return AmISigned;
    }
	
	
	/**
     * Method that contacts a php page to update the user information.
     */
	public void updateMe(){
        /*
		ArrayList<NameValuePair> userInfo = new ArrayList<NameValuePair>();
        userInfo.add(new BasicNameValuePair(valueOf(R.string.REQUEST_TYPE), valueOf(R.string.USER_UPDATE_REQUEST)));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_USER_ID), valueOf(tmpUser.get_userID())));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_USERNAME), tmpUser.get_username()));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_PASSWORD), tmpUser.get_password()));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_ADDRESS), tmpUser.get_address()));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_LAST_NAME), tmpUser.get_last_name()));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_NAME), tmpUser.get_name()));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_USER_EMAIL), tmpUser.get_email()));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_MUNICIPALITY), tmpUser.get_municipality()));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_PROVINCE), tmpUser.get_province()));
        userInfo.add(new BasicNameValuePair(valueOf(R.string.DB_REGION), tmpUser.get_region()));
*/
        JSONObject userInfo = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(USER_UPDATE_REQUEST));
        if(userInfo != null){
			try{
                /* Setting user information */
                fromJSON(userInfo.getJSONObject(USER));
			}catch(JSONException e){
				Log.e(TAG, "Error parsing data " + e.toString());
			}
		}
	}

    /**
     * Method that converts to a JSONObject the request type (so the server will understand what to do) and all user information
     * @param request_type
     * @return JSONObject containing user information and the request type
     */
    private JSONObject toJSON(String request_type){
        JSONObject jsonObject = new JSONObject();
        if(request_type != null){
            try {
                JSONObject obj;
                if((obj = toJSON()) != null){
                    /* If user information are converted correctly */
                    /* Composing JSON Object with the request type so the server will understand what to do */
                    jsonObject.put(REQUEST_TYPE, request_type);
                    jsonObject.put(USER, obj.toString());
                }

            } catch (JSONException e) {
                Log.e(TAG, "Error composing JSON " + e.toString());
            }
        }
        return jsonObject;
    }

    /**
     * Method that converts all user information to a JSONObject
     * @return JSONObject containing user information. Null if there was something wrong
     */
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DB_USER_ID, get_userID());
            jsonObject.put(DB_NAME, get_name());
            jsonObject.put(DB_LAST_NAME, get_last_name());
            jsonObject.put(DB_USERNAME, get_username());
            jsonObject.put(DB_PASSWORD, get_password());
            jsonObject.put(DB_ADDRESS, get_address());
            jsonObject.put(DB_USER_EMAIL, get_email());
            jsonObject.put(DB_USER_PHONE, get_phone());
            jsonObject.put(DB_MUNICIPALITY, get_municipality());
            jsonObject.put(DB_PROVINCE, get_province());
            jsonObject.put(DB_REGION, get_region());
            /* return the JSONObject created */
            return jsonObject;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
            return null;
        }
    }

    /**
     * Method that catches user information from a JSONObject and replace the current one's
     * @param user (JSONObject)
     */
    private void fromJSON(JSONObject user){
        try {
            _name = user.getString(DB_NAME);
            _last_name = user.getString(DB_LAST_NAME);
            _username = user.getString(DB_USERNAME);
            _password = user.getString(DB_PASSWORD);
            _address = user.getString(DB_ADDRESS);
            _email = user.getString(DB_USER_EMAIL);
            _phone = user.getString(DB_USER_PHONE);
            _municipality = user.getString(DB_MUNICIPALITY);
            _province = user.getString(DB_PROVINCE);
            _region = user.getString(DB_REGION);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }
    }

    /**
     * Method that check if the email is valid
     * @param email
     * @return TRUE if the email is valid. FALSE if it isn't.
     */
    public static boolean isValidEmail(String email) {
        return (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


    /* Getters */
    public String get_username() {
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
    /* Setters */
    public void set_username(String _username) {
        this._username = _username;
    }
    public void set_password(String _password) {
        this._password = _password;
    }
    public void set_name(String _name) {
        this._name = _name;
    }
    public void set_last_name(String _last_name) {
        this._last_name = _last_name;
    }
    public void set_email(String _email) {
        this._email = _email;
    }
    public void set_address(String _address) {
        this._address = _address;
    }
    public void set_phone(String _phone) {
        this._phone = _phone;
    }
    public void set_region(String _region) {
        this._region = _region;
    }
    public void set_province(String _province) {
        this._province = _province;
    }
    public void set_municipality(String _municipality) {
        this._municipality = _municipality;
    }
}

