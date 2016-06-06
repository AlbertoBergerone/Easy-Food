package com.example.alberto.easyfood.UserModule;

import android.util.Log;

import com.example.alberto.easyfood.DatabaseModule.CommunicationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * Created by Alberto on 13/04/2016.
 */
public class User implements Serializable {
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
	private String _residence;
    /*** Location which identifies actual user position ***/
    private final String TAG = "User";
    /* URL of the server */
    private static final String URL_LOGIN_SIGNUP = "http://185.51.138.52:5005/serverForApplication/login_signup.php";
	/* Database attributes */
    private static final String LOGIN_REQUEST = "login_request";
    private static final String SIGNUP_REQUEST = "sigup_request";
    private static final String USER_DELETE_REQUEST = "user_delete_request";
    private static final String USER_UPDATE_REQUEST = "user_update_request";
    private static final String SERVER_RESPONSE = "response";
    private static final String REQUEST_TYPE = "request_type";
    private static final String USER = "user";
    private static final String DELETED = "deleted";
    private static final String UPDATED = "updated";
    private static final String ADDED = "added";
    private static final String NOT_ADDED = "not_added";
    private static final String DB_USERNAME = "username";
    private static final String DB_PASSWORD = "password";
    private static final String DB_NAME = "nome";
    private static final String DB_LAST_NAME = "cognome";
    private static final String DB_USER_EMAIL = "emailUtente";
    private static final String DB_ADDRESS = "indirizzo";
    private static final String DB_USER_PHONE = "telUtente";
    private static final String DB_USER_ID = "codUtente";
    private static final String DB_REGION = "nomeRegione";
    private static final String DB_PROVINCE = "nomeProvincia";
    private static final String DB_RESIDENCE = "nomeComune";


	/* Constructors */
    /**
     * Constructor
     * @param username (username)
     * @param password (user password)
     */
    public User(String username, String password){
        this._username = username;
        this._password = password;
        this._userID = -1;
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
        this(username, password);
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
		/* Sending username and password to get other information if the user exists */
		JSONObject userInfo = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(LOGIN_REQUEST));
        if(userInfo != null){
			try{
                /* Getting user information */
                fromJSON(userInfo.getJSONObject(USER));
				/* The login was successful */
				amILogged = true;
			}catch(JSONException e){
				Log.e(TAG, "Error parsing data " + e.toString());
			}
		}else{
            Log.e(TAG, "public boolean loginMe() - Error receiving data. The server returned null");
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
		/* Sending user information and getting the response */
        JSONObject response = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(SIGNUP_REQUEST));
		if(response != null){
			try{
				JSONObject json_data = response.getJSONObject(USER);
                /* Getting return message */
                if(response.getString(SERVER_RESPONSE).equals(ADDED)){
                    /* The user has been signed */
                    int id;
                    if((id = json_data.getInt(DB_USER_ID)) >= 0){
                        /* If the user ID is a valid number, it replace the current id */
                        this._userID = id;
                        /* The user has been signed correctly */
                        AmISigned = true;
                    }else{
                        Log.e(TAG, "public boolean signupMe() - negative user id");
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
            Log.e(TAG, "public boolean signupMe() - Error receiving data. The server returned null");
        }
        return AmISigned;
    }


    /**
     * Method that contacts a php page to delete the user.
     * @return
     * False --> the user has not been deleted
     * True  --> the user has been deleted
     */
    public boolean deleteMe(){
        boolean amIDeleted = false;
        JSONObject response = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(USER_DELETE_REQUEST));
        if(response != null){
            try{
                if(response.getString(SERVER_RESPONSE).equals(DELETED)){
				    /* The user was successfully deleted */
                    amIDeleted = true;
                }
            }catch(JSONException e){
                Log.e(TAG, "Error parsing data " + e.toString());
            }
        }else{
            Log.e(TAG, "public boolean deleteMe() - Error receiving data. The server returned null");
        }
        return amIDeleted;
    }

	/**
     * Method that contacts a php page to update the user information.
     * @return
     * False --> the user has not been updated
     * True  --> the user has been updated
     */
	public boolean updateMe(){
        boolean amIUpdated = false;
        JSONObject response = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(USER_UPDATE_REQUEST));
        if(response != null){
            try{
                JSONObject json_data = response.getJSONObject(USER);
                /* Getting return message */
                if(response.getString(SERVER_RESPONSE).equals(UPDATED)){
                    /* The user has been updated */
                    /* Getting user information */
                    fromJSON(json_data);
                    amIUpdated = true;
                }else{
                    /* The user has not been signed */
                    /* Checking what is the problem */
                    if(json_data.getString(DB_USER_EMAIL).isEmpty()){
                        /* The email is invalid */
                        this._email = null;
                    }
                }
            }catch(JSONException e){
                Log.e(TAG, "Error parsing data " + e.toString());
            }
        }else{
            Log.e(TAG, "public boolean updateMe() - Error receiving data. The server returned null");
        }
        return amIUpdated;
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
            /* adding user information to a JSONObject */
            jsonObject.put(DB_USER_ID, get_userID());
            jsonObject.put(DB_NAME, get_name());
            jsonObject.put(DB_LAST_NAME, get_last_name());
            jsonObject.put(DB_USERNAME, get_username());
            jsonObject.put(DB_PASSWORD, get_password());
            jsonObject.put(DB_ADDRESS, get_address());
            jsonObject.put(DB_USER_EMAIL, get_email());
            jsonObject.put(DB_USER_PHONE, get_phone());
            jsonObject.put(DB_RESIDENCE, get_residence());
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
            _residence = user.getString(DB_RESIDENCE);
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
    public String get_residence() {
        return _residence;
    }
    /* Setters */
    public void set_username(String username) {
        this._username = username;
    }
    public void set_password(String password) {
        this._password = password;
    }
    public void set_name(String name) {
        this._name = name;
    }
    public void set_last_name(String last_name) {
        this._last_name = last_name;
    }
    public void set_email(String email) {
        this._email = email;
    }
    public void set_address(String address) {
        this._address = address;
    }
    public void set_phone(String phone) {
        this._phone = phone;
    }
    public void set_region(String region) {
        this._region = region;
    }
    public void set_province(String province) {
        this._province = province;
    }
    public void set_residence(String residence) {
        this._residence = residence;
    }
}

