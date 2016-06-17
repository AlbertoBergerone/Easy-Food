package com.example.alberto.easyfood.RestaurantModule;

import java.io.Serializable;

/**
 * Created by Alberto on 13/04/2016.
 */
public class Restaurant implements Serializable {
    private int _restaurantID;
    private String _restaurantName;
    private String _address;
    private double _longitude;
    private double _latitude;
    private double _rating;
    private String _residence;
    private String _provinceID;
    private String _type;
    private String _phoneNumber;
    private String _email;

    /* Constructors */
    public Restaurant(){}

    public Restaurant(String restaurantName, String residence, String address, String provinceID, double rating) {
        this._restaurantName = restaurantName;
        this._residence = residence;
        this._address = address;
        this._provinceID = provinceID;
        this._rating = rating;
    }

    public String get_full_address(){
        if(_address != null && _residence != null && _provinceID != null)
            return _address + ", " + _residence + " (" + _provinceID + ")";
        else return null;
    }

    /* Getters and setters */
    public int get_restaurantID() {
        return _restaurantID;
    }

    public void set_restaurantID(int restaurantID) {
        this._restaurantID = restaurantID;
    }

    public String get_restaurantName() {
        return _restaurantName;
    }

    public void set_restaurantName(String restaurantName) {
        this._restaurantName = restaurantName;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String address) {
        this._address = address;
    }

    public double get_longitude() {
        return _longitude;
    }

    public void set_longitude(double longitude) {
        this._longitude = longitude;
    }

    public double get_latitude() {
        return _latitude;
    }

    public void set_latitude(double latitude) {
        this._latitude = latitude;
    }

    public double get_rating() {
        return _rating;
    }

    public void set_rating(double rating) {
        this._rating = rating;
    }

    public String get_residence() {
        return _residence;
    }

    public void set_residence(String residence) {
        this._residence = residence;
    }

    public String get_provinceID() {
        return _provinceID;
    }

    public void set_provinceID(String provinceID) {
        this._provinceID = provinceID;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String type) {
        this._type = type;
    }
}
