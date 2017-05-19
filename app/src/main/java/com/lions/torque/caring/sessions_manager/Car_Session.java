package com.lions.torque.caring.sessions_manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Panwar on 02/03/17.
 */
public class Car_Session
{
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;


    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Car_Session";

    // All Shared Preferences Keys

    // User name (make variable public to access from outside)\
    public static String KEY_Car_Name = "CAR_NAME";
    public static String KEY_Car_Model = "CAR_MODEL";
    public static String KEY_Car_Year = "CAR_YEAR";
    public static String KEY_Car_Brand = "CAR_BRAND";
    public static String KEY_Car_FUEL = "CAR_FUEL";
    public static String KEY_Car_Segment = "CAR_SEGMENT";
    public static String KEY_Car_Code = "CAR_CODE";
    public static String KEY_Car_User = "CAR_USER";
    // Constructor
    public Car_Session(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public boolean create_Location_Session( String car_brand, String car_code, String car_fuel, String car_model, String car_name, String car_segment, String car_year){
        // Storing login value as TRUE

        editor.clear();
        editor.commit();
        editor.putString(KEY_Car_Brand,car_brand);
        editor.putString(KEY_Car_Code,car_code);
        editor.putString(KEY_Car_FUEL,car_fuel);
        editor.putString(KEY_Car_Model,car_model);
        editor.putString(KEY_Car_Name,car_name);
        editor.putString(KEY_Car_Segment,car_segment);
        editor.putString(KEY_Car_Year,car_year);

        // Storing name in pref
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
        user.put(KEY_Car_Brand,pref.getString(KEY_Car_Brand,null));
        user.put(KEY_Car_Code,pref.getString(KEY_Car_Code,null));
        user.put(KEY_Car_FUEL,pref.getString(KEY_Car_FUEL,null));
        user.put(KEY_Car_Model,pref.getString(KEY_Car_Model,null));
        user.put(KEY_Car_Name,pref.getString(KEY_Car_Name,null));
        user.put(KEY_Car_Segment,pref.getString(KEY_Car_Segment,null));
        user.put(KEY_Car_Year,pref.getString(KEY_Car_Year,null));
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
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity


//        ActivityCompat.finishAffinity(null);
    }



}
