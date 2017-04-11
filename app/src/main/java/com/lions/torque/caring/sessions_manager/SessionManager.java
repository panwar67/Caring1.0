package com.lions.torque.caring.sessions_manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.lions.torque.caring.servicecar.LoginActivity;

import java.util.HashMap;

/**
 * Created by Panwar on 03/02/17.
 */
public class SessionManager {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;


    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Caring_app";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Email address (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public  static final String KEY_UID = "uid";
    public static final String KEY_DP = "dp";
    public static final String KEY_MOB = "mobile";
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public boolean createLoginSession(String email, String name, String uid, String dp, String mob){
        // Storing login value as TRUE

        editor.clear();
        editor.commit();

        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_EMAIL, email);

        // Storing email in pref
        editor.putString(KEY_NAME, name);

        editor.putString(KEY_UID,uid);

        editor.putString(KEY_DP, dp);
        editor.putString(KEY_MOB,mob);

        // commit changes
        editor.commit();
        return  true;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){




        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user email id
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_UID, pref.getString(KEY_UID,null));

        user.put(KEY_DP,pref.getString(KEY_DP,null));
        user.put(KEY_MOB,pref.getString(KEY_MOB,null));


        // return user
        return user;

    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity


//        ActivityCompat.finishAffinity(null);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }


}
