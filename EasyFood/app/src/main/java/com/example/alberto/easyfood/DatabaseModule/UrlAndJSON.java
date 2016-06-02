package com.example.alberto.easyfood.DatabaseModule;

import org.json.JSONObject;

/**
 * Created by Alberto on 02/06/2016.
 * Class that is used only to contain an url and a JSONObject
 */
public class UrlAndJSON {
    String url;
    JSONObject json;

    /**
     * Constructor
     * @param url (String) url
     * @param data (JSONObject) data
     */
    public UrlAndJSON(String url, JSONObject data){
        setUrl(url);
        setJson(data);
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getJson() {
        return json;
    }
    public void setJson(JSONObject json) {
        this.json = json;
    }
}
