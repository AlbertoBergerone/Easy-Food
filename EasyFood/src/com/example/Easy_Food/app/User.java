package com.example.Easy_Food.app;

/**
 * Created by Alberto on 13/04/2016.
 */
public class User {
    private String _username;
    private String _password;
    private String _name;
    private String _last_name;
    private String _email;

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

    /* Getters & setters */
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
    public void set_username(String username) {
        this._username = username;
    }
    public void set_password(String password) {
        this._password = password;
    }
    public void set_name(String name) {
        this._name = name;
    }
    public void set_surname(String surname) {
        this._last_name = surname;
    }
    public void set_email(String email) {
        this._email = email;
    }

}

