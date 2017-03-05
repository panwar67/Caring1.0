package com.lions.torque.caring.sessions_manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Panwar on 18/02/17.
 */
public class Location_Session {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;


    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Location";

    // All Shared Preferences Keys

    // User name (make variable public to access from outside)\
    public static final String KEY_ADD = "address";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LONG = "long";

    // Constructor
    public Location_Session(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public boolean create_Location_Session( String address, String lat, String longitude){
        // Storing login value as TRUE

        editor.clear();
        editor.commit();


        // Storing name in pref
        editor.putString(KEY_ADD,address);

        editor.putString(KEY_LAT, lat);
        editor.putString(KEY_LONG,longitude);

        // commit changes
        editor.commit();
        return  true;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_ADD,pref.getString(KEY_ADD,null));

        user.put(KEY_LAT,pref.getString(KEY_LAT,null));
        user.put(KEY_LONG,pref.getString(KEY_LONG,null));

        // return user
        return user;

    }

    /**
     * Clear session details
     * */
    public void Clear_Location(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }





}
