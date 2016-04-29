package com.example.Easy_Food.app;

/**
 * Created by Alberto on 13/04/2016.
 */
public class Restaurant {
    private int _restaurantID;
    private String _restaurantName;
    private String _address;
    private String _streetNumber;
    private double _longitude;
    private double _latitude;
    private double _rating;
    private String _municipality;
    private String _province;
    private String _provinceID;
    private String _region;
    private String _regionID;
    private String _type;

    /* Constructors */

    public Restaurant(String restaurantName, String municipality, String address, String streetNumber, String province, String provinceID, double rating) {
        this._restaurantName = restaurantName;
        this._municipality = municipality;
        this._address = address;
        this._streetNumber = streetNumber;
        this._province = province;
        this._provinceID = provinceID;
        this._rating = rating;
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

    public String get_streetNumber() {
        return _streetNumber;
    }

    public void set_streetNumber(String streetNumber) {
        this._streetNumber = streetNumber;
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

    public String get_municipality() {
        return _municipality;
    }

    public void set_municipality(String municipality) {
        this._municipality = municipality;
    }

    public String get_province() {
        return _province;
    }

    public void set_province(String province) {
        this._province = province;
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

    public String get_region() {
        return _region;
    }

    public void set_region(String region) {
        this._region = region;
    }

    public String get_regionID() {
        return _regionID;
    }

    public void set_regionID(String regionID) {
        this._regionID = regionID;
    }
}
