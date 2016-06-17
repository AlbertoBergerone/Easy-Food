package com.example.alberto.easyfood.UserModule;

import android.util.Log;

import com.example.alberto.easyfood.ServerCommunicationModule.CommunicationManager;

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
	private String _province;
	private String _residence;

	/* Constructors */

    /**
     * Constructor
     */
    public User(){}

    /**
     * Constructor
     * @param username (String) username
     * @param password (String) user password
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
        this(username, password);
        this._name = name;
        this._last_name = last_name;
        this._email = email;
    }

    /**
     * Constructor for copy a user
     * @param user (User) user
     */
    public User(User user){
        this.set_userID(user.get_userID());
        this.set_username(user.get_username());
        this.set_name(user.get_name());
        this.set_last_name(user.get_last_name());
        this.set_password(user.get_password());
        this.set_address(user.get_address());
        this.set_residence(user.get_residence());
        this.set_province(user.get_province());
        this.set_phone(user.get_phone());
        this.set_email(user.get_email());
    }

    public String get_full_address(){
        if(_address != null && _residence != null && _province != null)
            return _address + ", " + _residence + " (" + _province + ")";
        else
            return null;
    }

    /* Getters */
    public int get_userID() {
        return _userID;
    }
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
    public String get_address() {
        return _address;
    }
    public String get_phone() {
        return _phone;
    }
    public String get_province() {
        return _province;
    }
    public String get_residence() {
        return _residence;
    }
    /* Setters */
    public void set_userID(int user_ID) {
        this._userID = user_ID;
    }
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
    public void set_province(String province) {
        this._province = province;
    }
    public void set_residence(String residence) {
        this._residence = residence;
    }
}

