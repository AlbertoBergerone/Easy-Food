package com.example.alberto.easyfood.GeolocationModule;

/**
 * Created by Alberto on 18/06/2016.
 */
public class Residence {
    public String cadasralID;
    public String residenceName;
    public String provinceID;
    public String cap;

    public Residence(){}

    public Residence(String cadasralID, String residenceName, String provinceID) {
        this.cadasralID = cadasralID;
        this.residenceName = residenceName;
        this.provinceID = provinceID;
    }

    public Residence(String cadasralID, String residenceName, String provinceID, String cap) {
        this(cadasralID, residenceName, provinceID);
        this.cap = cap;
    }

    public String get_full_residence(){
        return residenceName + "(" + provinceID + ")";
    }
}
