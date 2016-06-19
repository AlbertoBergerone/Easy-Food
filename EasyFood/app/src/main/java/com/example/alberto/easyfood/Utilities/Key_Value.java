package com.example.alberto.easyfood.Utilities;

/**
 * Created by Alberto on 19/06/2016.
 * Simple class that contains two String properties. One for the key and the other for its value
 */
public class Key_Value {
    private String key;
    private String value;

    public Key_Value(){}

    public Key_Value(String key, String value){
        this.key = key;
        this.value = value;
    }

    /* Getters and setters */
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
