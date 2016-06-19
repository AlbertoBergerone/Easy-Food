package com.example.alberto.easyfood.RestaurantModule;

import android.content.Context;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.Utilities.DB_Attributes;
import com.example.alberto.easyfood.Utilities.Key_Value;

import java.io.Serializable;
import java.util.ArrayList;

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
    private String[] _type;
    private String _phoneNumber;
    private String _email;
    private String _description;

    /* Constructors */
    public Restaurant(){}

    /**
     * Getting an ArrayList containing key, value of the main properties of this restaurant
     * @return (ArrayList<Key_Value>)
     */
    public ArrayList<Key_Value> toArrayList(Context context){
        ArrayList<Key_Value> key_valueRestaurantArrayList = new ArrayList<>();
        if(_restaurantName != null && !_restaurantName.isEmpty())
            key_valueRestaurantArrayList.add(new Key_Value(context.getResources().getString(R.string.Name), get_restaurantName()));

        if(_address != null && !_address.isEmpty())
            key_valueRestaurantArrayList.add(new Key_Value(context.getResources().getString(R.string.Address), get_address()));

        String residence;
        if((residence = get_full_residence()) != null)
            key_valueRestaurantArrayList.add(new Key_Value(context.getResources().getString(R.string.Location), residence));

        String types;
        if(!(types = get_typeToString()).isEmpty()) {
            key_valueRestaurantArrayList.add(new Key_Value(context.getResources().getString(R.string.Type), types));
        }

        key_valueRestaurantArrayList.add(new Key_Value(context.getResources().getString(R.string.Rating), get_ratingToString()));

        if(_email != null && !_email.isEmpty())
            key_valueRestaurantArrayList.add(new Key_Value(context.getResources().getString(R.string.Email), get_email()));

        if(_phoneNumber != null && !_phoneNumber.isEmpty())
            key_valueRestaurantArrayList.add(new Key_Value(context.getResources().getString(R.string.PhoneNumber), get_phoneNumber()));

        return key_valueRestaurantArrayList;
    }

    public String get_full_address(){
        if(_address != null && _residence != null && _provinceID != null)
            return _address + ", " + _residence + " (" + _provinceID + ")";
        else return null;
    }

    public String get_full_residence(){
        if(_residence != null && _provinceID != null)
            return _residence + " (" + _provinceID + ")";
        else return null;
    }

    public String get_ratingToString(){
        return String.valueOf(_rating) + "/8";
    }

    /**
     * Getting all the restaurant types
     * @return (String)
     */
    public String get_typeToString(){
        if(_type != null){
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < _type.length; i++){
                ret.append(_type[i]);
                if(i != (_type.length -1)) ret.append(", ");
            }
            return ret.toString();
        }else return "";
    }

    /**
     * Add a new type to this restaurant. If it already exists, it isn't added.
     * @param new_type
     */
    public void addType(String new_type) {
        int i = 0;
        boolean found = false;
        if(_type != null) {
            while (i < _type.length && !found) {
                if (_type[i] == new_type)
                    found = true;
                else i++;
            }
            if (!found)
                _type[_type.length] = new_type;
        } else {
            _type[0] = new_type;
        }
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

    public String[] get_type() {
        return _type;
    }

    public void set_type(String[] type) {
        this._type = type;
    }

    public String get_phoneNumber() {
        return _phoneNumber;
    }

    public void set_phoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }


    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }
}
