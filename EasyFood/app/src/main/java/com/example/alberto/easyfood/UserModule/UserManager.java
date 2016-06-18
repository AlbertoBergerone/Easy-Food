package com.example.alberto.easyfood.UserModule;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.alberto.easyfood.GeolocationModule.Residence;
import com.example.alberto.easyfood.ServerCommunicationModule.CommunicationManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Alberto on 13/04/2016.
 */
public class UserManager {
    private User _user;
	
    private static final String TAG = "UserManager";
    /* URL of the server */
    private static final String URL_LOGIN_SIGNUP = "http://185.51.138.52:5005/serverForApplication/login_signup.php";
    private static final String URL_RESIDENCES = "http://185.51.138.52:5005/serverForApplication/residence_list.php";
    /* Request values */
    private static final String USER_LOGIN_REQUEST = "login_request";
    private static final String USER_SIGNUP_REQUEST = "sigup_request";
    private static final String USER_DELETE_REQUEST = "user_delete_request";
    private static final String USER_UPDATE_REQUEST = "user_update_request";
    /* Requests and responses */
    private static final String REQUEST_TYPE = "request_type";
    private static final String SERVER_RESPONSE = "response";
    private static final String USER = "user";
    /* Server response values */
    private static final String DELETED = "deleted";
    private static final String ADDED = "added";
    private static final String ALREADY_USED = "already_used";
    /* Database attributes */
    private static final String DB_USERNAME = "username";
    private static final String DB_PASSWORD = "passwordUtente";
    private static final String DB_NAME = "nome";
    private static final String DB_LAST_NAME = "cognome";
    private static final String DB_USER_EMAIL = "emailUtente";
    private static final String DB_USER_ADDRESS = "indirizzoUtente";
    private static final String DB_USER_PHONE = "telUtente";
    private static final String DB_USER_ID = "codUtente";
    private static final String DB_PROVINCE_ID = "codProvincia";
    private static final String DB_CADASTRAL_ID = "codCatastale";
    private static final String DB_RESIDENCE = "nomeComune";
    private static final String DB_CAP = "cap";

    private static final String RESIDENCES = "comuni";

    public UserManager(){
        this._user = null;
    }

	/**
     * Constructor
     * @param user (User)
     */
	public UserManager(User user){
		this._user = user;
	}
	
	/**
     * Constructor
     * @param username (String) username
     * @param password (String) password
     */
	public UserManager(String username, String password){
        this._user = new User();
		this._user.set_username(username);
		this._user.set_password(password);
	}
	
	/**
     * Constructor
     * @param username (String) username
     * @param password (String) password
     * @param name (String) name
     * @param last_name (String) last name
     * @param email (String) email
     */
	public UserManager(String username, String password, String  name, String last_name, String email){
		this(username, password);
		this._user.set_name(name);
		this._user.set_last_name(last_name);
		this._user.set_email(email);
	}
	
    

    /**
     * Method that contacts a php page to authenticate the user.
     * If the user is correctly logged the User property will contain the user information
     * @return
     * False --> The user doesn't exist
     * True  --> The user exists and now he is logged
     */
    public boolean loginUser(){
		boolean amILogged = false;
		/* Sending username and password to get other information if the user exists */
		JSONObject userInfo = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(USER_LOGIN_REQUEST));
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
            Log.e(TAG, "public boolean loginUser() - Error receiving data. The server returned null");
        }
		return amILogged;
    }
	
    /**
     * Method that contacts a php page to sign up the user.
     * If the user is correctly signed the User property will contain the user information
     * @return
     *  True  --> The user has been signed successful
     *  False --> The user ha not been signed
     */
    public boolean signUpUser(){
        boolean AmISigned = false;
		/* Sending user information and getting the response */
        JSONObject response = CommunicationManager.postData(URL_LOGIN_SIGNUP, toJSON(USER_SIGNUP_REQUEST));
		if(response != null){
			try{
				JSONObject json_data = response.getJSONObject(USER);
                /* Getting return message */
                if(response.getString(SERVER_RESPONSE).equals(ADDED)){
                    /* The user has been signed */
                    fromJSON(response.getJSONObject(USER));
                    AmISigned = true;
                }else{
                    /* The user has not been signed */
                    /* Checking what is the problem */
                    if(json_data.getString(DB_USER_EMAIL).equals(ALREADY_USED)){
                        /* The email is invalid */
                        this._user.set_email(null);
                    }
                    if(json_data.getString(DB_USER_EMAIL).equals(ALREADY_USED)){
                        /* The username is already used */
                        this._user.set_username(null);
                    }
                }
			}catch(JSONException e){
				Log.e(TAG, "Error parsing data " + e.toString());
			}
		}else{
            Log.e(TAG, "public boolean signUpUser() - Error receiving data. The server returned null");
        }
        return AmISigned;
    }


    /**
     * Method that contacts a php page to delete the user.
     * @return
     * False --> the user has not been deleted
     * True  --> the user has been deleted
     */
    public boolean deleteUser(){
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
     * The User property will contain the updated user information
     * @return
     * False --> the user has not been updated
     * True  --> the user has been updated
     */
	public boolean updateUser(){
        boolean amIUpdated = false;
        JSONObject json = toJSON(USER_UPDATE_REQUEST);
        if(json != null){
            JSONObject response = CommunicationManager.postData(URL_LOGIN_SIGNUP, json);
            if(response != null){
                try{
                    /* Getting user information updated */
                    fromJSON(response.getJSONObject(USER));
                    amIUpdated = true;
                }catch(JSONException e){
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
            }else{
                Log.e(TAG, "public boolean updateMe() - Error receiving data. The server returned null");
            }
        }
        return amIUpdated;
	}

    public ArrayList<Residence> getResidences(String residence_initials){
        ArrayList<Residence> ret = new ArrayList<>();
        /* Getting a JSONArray from the server containing residences */
        JSONArray jsonArray = getResidencesJSONArray(residence_initials);
        if(jsonArray != null){
            int i;
            /* Getting residence information */
            try {
                for(i = 0; i < jsonArray.length(); i++){
                    Residence tmp = new Residence();

                    if(!jsonArray.getJSONObject(i).isNull(DB_CADASTRAL_ID))
                        tmp.cadasralID = jsonArray.getJSONObject(i).getString(DB_CADASTRAL_ID);
                    if(!jsonArray.getJSONObject(i).isNull(DB_RESIDENCE))
                        tmp.provinceID = jsonArray.getJSONObject(i).getString(DB_RESIDENCE);
                    if(!jsonArray.getJSONObject(i).isNull(DB_PROVINCE_ID))
                        tmp.provinceID = jsonArray.getJSONObject(i).getString(DB_PROVINCE_ID);
                    if(!jsonArray.getJSONObject(i).isNull(DB_CAP))
                        tmp.provinceID = jsonArray.getJSONObject(i).getString(DB_CAP);

                    ret.add(tmp);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return ret;
    }

    public ArrayList<String> getResidenceNames(String residence_initials){
        ArrayList<String> ret = new ArrayList<>();
        /* Getting a JSONArray from the server containing residences */
        JSONArray jsonArray = getResidencesJSONArray(residence_initials);
        if(jsonArray != null){
            int i;
            /* Getting residence information */
            try {
                for(i = 0; i < jsonArray.length(); i++){
                    if(!jsonArray.getJSONObject(i).isNull(DB_RESIDENCE) && !jsonArray.getJSONObject(i).isNull(DB_PROVINCE_ID))
                        ret.add(jsonArray.getJSONObject(i).getString(DB_RESIDENCE) + " (" + jsonArray.getJSONObject(i).getString(DB_PROVINCE_ID) + ")");
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return ret;
    }


    public JSONArray getResidencesJSONArray(String residence_initials){
        JSONArray jsonArray = null;
        JSONObject json = new JSONObject();

        if(!residence_initials.isEmpty()){
            try {
                json.put(DB_RESIDENCE, residence_initials);
                json = CommunicationManager.postData(URL_RESIDENCES, json);
                if(json != null)
                    jsonArray = json.getJSONArray(RESIDENCES);

            } catch (JSONException e) {
                Log.e(TAG, "Malformed Json string: " + json.toString() + e.getMessage());
            }
        }
        return jsonArray;
    }


    /**
     * Method that converts to a JSONObject the request type (so the server will understand what to do) and all user information
     * @param request_type (String)
     * @return JSONObject containing user information and the request type
     */
    private JSONObject toJSON(String request_type){
        JSONObject jsonObject = null;
        if(request_type != null){
            try {
                JSONObject obj;
                if((obj = toJSON()) != null){
                    jsonObject = new JSONObject();
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
            jsonObject.put(DB_USER_ID, _user.get_userID());
            jsonObject.put(DB_NAME, _user.get_name());
            jsonObject.put(DB_LAST_NAME, _user.get_last_name());
            jsonObject.put(DB_USERNAME, _user.get_username());
            jsonObject.put(DB_PASSWORD, _user.get_password());
            jsonObject.put(DB_USER_ADDRESS, _user.get_address());
            jsonObject.put(DB_USER_EMAIL, _user.get_email());
            jsonObject.put(DB_USER_PHONE, _user.get_phone());
            jsonObject.put(DB_RESIDENCE, _user.get_residence());
            jsonObject.put(DB_PROVINCE_ID, _user.get_province());
            /* return the JSONObject created */
            return jsonObject;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }
        return null;
    }

    /**
     * Getter of user property
     * @return User property
     */
    public User get_user(){
        return _user;
    }

    /**
     * Setter of user property
     * @param user user to set
     */
    public void set_user(User user){
        this._user = user;
    }

    /**
     * Method that catches user information from a JSONObject and replace the current one's
     * @param user (JSONObject)
     */
    private void fromJSON(JSONObject user){
        try {
            if(user.getString(DB_USER_ID).equalsIgnoreCase("null") || user.getString(DB_USER_ID) == null || user.getString(DB_USER_ID).isEmpty() || user.getInt(DB_USER_ID) < 0)
                _user.set_userID(-1);
            else
                _user.set_userID(user.getInt(DB_USER_ID));

            if(user.getString(DB_NAME).equalsIgnoreCase("null") || user.getString(DB_NAME) == null || user.getString(DB_NAME).isEmpty())
                _user.set_name(null);
            else
                _user.set_name(user.getString(DB_NAME));

            if(user.getString(DB_LAST_NAME).equalsIgnoreCase("null") || user.getString(DB_LAST_NAME) == null || user.getString(DB_LAST_NAME).isEmpty())
                _user.set_last_name(null);
            else
                _user.set_last_name(user.getString(DB_LAST_NAME));

            if(user.getString(DB_USERNAME) == null || user.getString(DB_USERNAME).isEmpty())
                _user.set_username(null);
            else
                _user.set_username(user.getString(DB_USERNAME));

            if(user.getString(DB_PASSWORD) == null || user.getString(DB_PASSWORD).isEmpty())
                _user.set_password(null);
            else
                _user.set_password(user.getString(DB_PASSWORD));

            if(user.getString(DB_USER_ADDRESS).equalsIgnoreCase("null") || user.getString(DB_USER_ADDRESS) == null || user.getString(DB_USER_ADDRESS).isEmpty())
                _user.set_address(null);
            else
                _user.set_address(user.getString(DB_USER_ADDRESS));

            if(user.getString(DB_USER_EMAIL).equalsIgnoreCase("null") || user.getString(DB_USER_EMAIL) == null || user.getString(DB_USER_EMAIL).isEmpty())
                _user.set_email(null);
            else
                _user.set_email(user.getString(DB_USER_EMAIL));

            if(user.getString(DB_USER_PHONE).equalsIgnoreCase("null") || user.getString(DB_USER_PHONE) == null || user.getString(DB_USER_PHONE).isEmpty())
                _user.set_phone(null);
            else
                _user.set_phone(user.getString(DB_USER_PHONE));

            if(user.getString(DB_RESIDENCE).equalsIgnoreCase("null") || user.getString(DB_RESIDENCE) == null || user.getString(DB_RESIDENCE).isEmpty())
                _user.set_residence(null);
            else
                _user.set_residence(user.getString(DB_RESIDENCE));

            if(user.getString(DB_PROVINCE_ID).equalsIgnoreCase("null") || user.getString(DB_PROVINCE_ID) == null || user.getString(DB_PROVINCE_ID).isEmpty())
                _user.set_province(null);
            else
                _user.set_province(user.getString(DB_PROVINCE_ID));

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }
    }

    /**
     * Method that check if the email is valid
     * @param email (String) email
     * @return TRUE if the email is valid. FALSE if it isn't.
     */
    public static boolean isValidEmail(String email) {
        return (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

}

