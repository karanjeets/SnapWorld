package edu.usc.snapworld;

import android.graphics.Bitmap;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by Monica on 10/2/2016.
 */

public class Constants {

    static HashMap<String,String> categoryMap = new HashMap<>();

    static String IMAGE = "image";
    static String USERNAME = "username";
    static String LATITUDE = "latitude";
    static String LONGITUDE = "longitude";
    static String CATEGORY = "category";
    static String DESCRIPTION = "description";
    static String TIMESTAMP = "timestamp";
    static JSONArray jsonListArray ;

    enum RequestType {
        GET,
        PUT_DETAILS
    }
}
