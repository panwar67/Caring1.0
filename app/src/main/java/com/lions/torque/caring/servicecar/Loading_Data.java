package com.lions.torque.caring.servicecar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jetradarmobile.rxlocationsettings.RxLocationSettings;
import com.lions.torque.caring.R;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.gps_track.Constants;
import com.lions.torque.caring.gps_track.FetchAddressIntentService;
import com.lions.torque.caring.sessions_manager.Location_Session;
import com.lions.torque.caring.sessions_manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Structs.Campaign_Struct;
import Structs.Car_Struct;
import Structs.Search_Bean;
import Structs.Search_Struct;
import Structs.Ven_List_Struct;
import Structs.Vendor_List_Struct;
import rx.functions.Action1;


public class Loading_Data extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    String DOWN_URL = "http://www.car-ing.com/app/Get_Vendor.php";
    String DOWN_URL1 = "http://www.car-ing.com/app/Get_Campaign.php";
    String DOWN_URL2 = "http://www.car-ing.com/app/Get_All_Cars.php";
    String DOWN_URL3 = "http://www.car-ing.com/app/Get_Services.php";
    protected LocationRequest mLocationRequest;
    private FirebaseAuth mAuth;
    DBHelper dbHelper;
    private FirebaseAuth.AuthStateListener mAuthListener;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected LocationSettingsRequest mLocationSettingsRequest;
    Location_Session location_session;

    //GPS_Tracker gps_tracker;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    String mAddressOutput;
    private TextView mLatitudeText, Splash_Status;
    private TextView mLongitudeText;
    LocationRequest locationRequest;
    EasyLocationRequest easyLocationRequest;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading__data);
        Splash_Status = (TextView) findViewById(R.id.loading_status);
        sessionManager = new SessionManager(getApplicationContext());
        dbHelper = DBHelper.getInstance(getApplicationContext());
        //gps_tracker = new GPS_Tracker(getApplicationContext());
        mResultReceiver = new AddressResultReceiver(new Handler());
        //gps_tracker = new GPS_Tracker(Loading_Data.this);
        mAuth = FirebaseAuth.getInstance();
        location_session = new Location_Session(getApplicationContext());
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d("inside_user",""+user+"");
                    if(sessionManager.createLoginSession(user.getEmail(),user.getDisplayName(),user.getUid(),user.getPhotoUrl().toString())) {
                        //  requestSingleLocationFix(easyLocationRequest);
                        Log.d("firebase_state", "onAuthStateChanged:signed_in:" + user.getUid());
                        startActivity(new Intent(Loading_Data.this, Home_Screen.class));
                        finish();
                    }

                } else {

                    // User is signed out
                    Log.d("firebase_state", "onAuthStateChanged:signed_out");
                    startActivity(new Intent(Loading_Data.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };

        //  Log.d("gps_tracker", "" + gps_tracker.getLocation());

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        // mAuth.addAuthStateListener(mAuthListener);

    }


    protected synchronized void buildGoogleApiClient() {
        Log.i("google_api", "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void ensureLocationSettings() {
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER))
                .build();
        RxLocationSettings.with(this).ensure(locationSettingsRequest)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean enabled) {
                        //checkPermission();
                        Toast.makeText(Loading_Data.this, enabled ? "Enabled" : "Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        //locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //easyLocationRequest = new EasyLocationRequestBuilder().setLocationRequest(locationRequest).build();
        // requestSingleLocationFix(easyLocationRequest);


        //checkLocationSettings();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    public void Get_Location() {
        int counter = 0;

        Log.d("reached", "Get_Location");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        if (mLastLocation == null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(Loading_Data.this);
            progressDialog.setMessage("Getting Location");
            progressDialog.show();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("location", "" + location);
                    mLastLocation = location;

                    progressDialog.dismiss();
                    startIntentService();
                }
            });

        }


    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(600000);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(600000);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */


    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("request_code_settings", "" + requestCode);
        Log.d("result_code_settings", "" + resultCode);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        Log.d("location_setting", "User agreed to make required location settings changes.");
                        onConnected(null);
                        break;
                    }   //startLocationUpdates();

                    case Activity.RESULT_CANCELED:
                        Log.i("location_setting", "User chose not to make required location settings changes.");
                        Toast.makeText(getApplicationContext(), "Cannot continue without location", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 104: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Get_Location();
                    //requestSingleLocationFix(easyLocationRequest);
                    Log.d("permission_granted", "on request");
                    // mGoogleApiClient.connect();
                    onConnected(null);
                    //ensureLocationSettings();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(getApplicationContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(Loading_Data.this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.

        startService(intent);
        Splash_Status.setText("Getting Your Location");
    }

    @SuppressLint("NewApi")
    public static final void recreateActivityCompat(final Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            a.recreate();
        } else {
            final Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.finish();
            a.overridePendingTransition(0, 0);
            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("google_client", "connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d("Permission_check", "Not permitted");
            ActivityCompat.requestPermissions(Loading_Data.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 104
            );
            return;
        }

        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
        Log.d("Permission_check", "granted");
        Log.d("location", "Getting location");


    }

    @Override
    public void onConnectionSuspended(int i) {

        // mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS: {
                Log.i("Settings_result", "All location settings are satisfied.");
                Get_Location();


                break;
            }
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i("Settings_result", "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(Loading_Data.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i("Settings_result", "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i("Settings_result", "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }

    }


    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            String lat = resultData.getString("latitude");
            String longitude = resultData.getString("longitude");
            if (resultCode == Constants.SUCCESS_RESULT|| resultCode==Constants.FAILURE_RESULT) {
               // showToast("success");
                mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                Log.d("address", "" + mAddressOutput+" "+lat+ ""+longitude);
                location_session.create_Location_Session(mAddressOutput, lat, longitude);
                Intent intent = new Intent(Loading_Data.this, FetchAddressIntentService.class);
                stopService(intent);
                dbHelper.Init_Search_Data();
                Load_Vendor_Data();

                if(Load_Vendor_Data())
                {
                    Load_Services();

                }
                Load_Campaigns();
                Load_Cars();
                mAuth.addAuthStateListener(mAuthListener);
            }

        }
    }

    protected void displayAddressOutput() {



        }


        //  mLocationAddressTextView.setText(mAddressOutput);


    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }



    public boolean Load_Vendor_Data()
    {
        final ArrayList<HashMap<String, String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        final ArrayList<HashMap<String,String>> Search_Data = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s != null) {
                            try {

                                JSONObject profile = new JSONObject(s);
                                JSONArray data = profile.getJSONArray(Ven_List_Struct.Table_Name);
                                if(data.length()>0)
                                {
                                    dbHelper.Init_Vendor();

                                }

                                // dbHelper.InitImg();
                                for (int i = 0; i < data.length(); i++) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    HashMap<String,String> search = new HashMap<String, String>();
                                    JSONObject details = data.getJSONObject(i);
                                    map.put(Ven_List_Struct.Ven_Assure, details.getString(Ven_List_Struct.Ven_Assure));
                                    map.put(Ven_List_Struct.Ven_Des, details.getString(Ven_List_Struct.Ven_Des));
                                    map.put(Ven_List_Struct.Ven_Id, details.getString(Ven_List_Struct.Ven_Id));
                                    map.put(Ven_List_Struct.Ven_Sn,details.getString(Ven_List_Struct.Ven_Sn));
                                    map.put(Ven_List_Struct.Ven_No_Img, details.getString(Ven_List_Struct.Ven_No_Img));
                                    map.put(Ven_List_Struct.Ven_Name, details.getString(Ven_List_Struct.Ven_Name));
                                    search.put(Search_Struct.tag,details.getString(Ven_List_Struct.Ven_Name));
                                    search.put(Search_Struct.col,details.getString(Ven_List_Struct.Ven_Id));
                                    search.put(Search_Struct.type,"SHOP");
                                    map.put(Ven_List_Struct.Ven_Quality, details.getString(Ven_List_Struct.Ven_Quality));
                                    map.put(Ven_List_Struct.Ven_Lat, details.getString(Ven_List_Struct.Ven_Lat));
                                    map.put(Ven_List_Struct.Ven_Long, details.getString(Ven_List_Struct.Ven_Long));
                                    map.put(Ven_List_Struct.Ven_Address,details.getString(Ven_List_Struct.Ven_Address));
                                    map.put(Ven_List_Struct.Ven_Url, details.getString(Ven_List_Struct.Ven_Url));
                                    map.put(Ven_List_Struct.Ven_Serve,details.getString(Ven_List_Struct.Ven_Serve));
                                    map.put(Ven_List_Struct.Ven_Serve_Name,details.getString(Ven_List_Struct.Ven_Serve_Name));
                                    map.put(Ven_List_Struct.Ven_Segment,details.getString(Ven_List_Struct.Ven_Segment));
                                    map.put(Ven_List_Struct.Ven_Segment_Name,details.getString(Ven_List_Struct.Ven_Segment_Name));
                                    map.put(Ven_List_Struct.Ven_price_low,details.getString(Ven_List_Struct.Ven_price_low));
                                    map.put(Ven_List_Struct.Ven_price_high,details.getString(Ven_List_Struct.Ven_price_high));
                                    map.put(Ven_List_Struct.Ven_Timings_Open,details.getString(Ven_List_Struct.Ven_Timings_Open));
                                    map.put(Ven_List_Struct.Ven_Timings_Close,details.getString(Ven_List_Struct.Ven_Timings_Close));
                                    Ven_Data.add(map);
                                    Search_Data.add(search);
                                }
                                Log.d("Vendor_List", s);
                                dbHelper.Insert_Vendor_List(Ven_Data);
                                dbHelper.Insert_Search_Data(Search_Data);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // mAuth.addAuthStateListener(mAuthListener);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Loading_Data.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String, String> Keyvalue = new HashMap<String, String>();


                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);
        return true;
    }

    public boolean Load_Campaigns()
    {
        final ArrayList<HashMap<String, String>> Camp_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s != null) {
                            try {
                                JSONObject profile = new JSONObject(s);
                                JSONArray data = profile.getJSONArray(Campaign_Struct.Table_Name);

                                 dbHelper.Init_Camp();
                                for (int i = 0; i < data.length(); i++) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    JSONObject details = data.getJSONObject(i);
                                    map.put(Campaign_Struct.Camp_Id, details.getString(Campaign_Struct.Camp_Id));
                                    map.put(Campaign_Struct.Camp_Name, details.getString(Campaign_Struct.Camp_Name));
                                    map.put(Campaign_Struct.Camp_Service_Id, details.getString(Campaign_Struct.Camp_Service_Id));
                                    map.put(Campaign_Struct.Camp_Url, details.getString(Campaign_Struct.Camp_Url));
                                    String camp_id = details.getString(Campaign_Struct.Camp_Id);
                                    String camp_name = details.getString(Campaign_Struct.Camp_Name);
                                    String camp_url = details.getString(Campaign_Struct.Camp_Url);
                                    String camp_serve_id = details.getString(Campaign_Struct.Camp_Service_Id);
                                    dbHelper.Insert_Campaign_List(camp_id,camp_name,camp_serve_id,camp_url);
                                }
                                Log.d("Camp_List", s);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //mAuth.addAuthStateListener(mAuthListener);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Loading_Data.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String, String> Keyvalue = new HashMap<String, String>();


                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        return true;
    }

    public boolean Load_Cars()
    {
        final ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    Log.d("response_cars",response.toString());
                        dbHelper.Init_Car();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("User_Car");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject car_detail = jsonArray.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(Car_Struct.Car_Model, car_detail.getString(Car_Struct.Car_Model));
                            map.put(Car_Struct.Car_Brand, car_detail.getString(Car_Struct.Car_Brand));
                            map.put(Car_Struct.Car_FUEL, car_detail.getString(Car_Struct.Car_FUEL));
                            map.put(Car_Struct.Car_Year, car_detail.getString(Car_Struct.Car_Year));
                            map.put(Car_Struct.Car_Segment, car_detail.getString(Car_Struct.Car_Segment));
                            map.put(Car_Struct.Car_Code, car_detail.getString(Car_Struct.Car_Code));
                            Log.d("load_cars", car_detail.getString(Car_Struct.Car_Model));
                            data.add(map);

                        }

                        dbHelper.Insert_All_Car(data);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error", "Getting cars");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<String, String>();
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);


        return true;
    }

    public boolean Load_Services()
    {
        final ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    Log.d("response_services",response.toString());
                    //dbHelper.Init_Car();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Services");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject car_detail = jsonArray.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(Search_Struct.tag,car_detail.getString("SERVE_NAME"));
                            map.put(Search_Struct.col,car_detail.getString("SERVE_CODE"));
                            map.put(Search_Struct.type,"SERVICE");
                            data.add(map);

                        }
                            dbHelper.Insert_Search_Data(data);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error", "Getting cars");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<String, String>();
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);


        return  true;
    }

}
