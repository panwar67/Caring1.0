package com.lions.torque.caring.servicecar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lions.torque.caring.R;
import com.lions.torque.caring.adapters.Service_Track_Adapter;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.Location_Session;
import com.lions.torque.caring.sessions_manager.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile_Page extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    ImageView dp, back;
    SessionManager sessionManager;
    TextView name, email, ph_no, add_mobile, delete_mobile, title, logout;
    private FirebaseAuth mAuth;
    Car_Session car_session;
    Location_Session location_session;
    private GoogleApiClient mGoogleApiClient;


    private FirebaseAuth.AuthStateListener mAuthListener;
    String DOWN_URL1 = "http://www.car-ing.com/app/Delete_Mobile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        logout =(TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                signOut();
                LoginManager.getInstance().logOut();
                sessionManager.logoutUser();
                car_session.Clear_Location();
                car_session.logoutUser();
                location_session.Clear_Location();
                mAuth.addAuthStateListener(mAuthListener);

            }
        });
        car_session = new Car_Session(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"gothiclit.ttf");
        int flags =  Paint.SUBPIXEL_TEXT_FLAG
                | Paint.ANTI_ALIAS_FLAG;
        mAuth = FirebaseAuth.getInstance();
        add_mobile = (TextView)findViewById(R.id.add_mobile);
        delete_mobile = (TextView)findViewById(R.id.delete_mobile);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null)
                {
                    startActivity(new Intent(Profile_Page.this,Loading_Data.class));
                    finish();
                }
            }
        };
        name = (TextView)findViewById(R.id.profile_name);
        email = (TextView)findViewById(R.id.profile_email);
        dp = (ImageView)findViewById(R.id.profile_pic);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                signOut();
                LoginManager.getInstance().logOut();
                sessionManager.logoutUser();
                car_session.Clear_Location();
                car_session.logoutUser();
                location_session.Clear_Location();
                mAuth.addAuthStateListener(mAuthListener);


            }
        });
        ph_no = (TextView)findViewById(R.id.profile_no);

        name.setTypeface(typeface);
        email.setTypeface(typeface);
        name.setPaintFlags(flags);
        email.setPaintFlags(flags);
        ph_no.setPaintFlags(flags);
        ph_no.setTypeface(typeface);
        add_mobile.setTypeface(typeface);
        delete_mobile.setTypeface(typeface);
        add_mobile.setPaintFlags(flags);
        delete_mobile.setPaintFlags(flags);
        title = (TextView)findViewById(R.id.title);
        title.setTypeface(typeface);
        title.setPaintFlags(flags);
        logout.setTypeface(typeface);
        logout.setPaintFlags(flags);
        back = (ImageView)findViewById(R.id.profile_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Picasso.with(this)
                .load(sessionManager.getUserDetails().get("dp"))
                .resize(400,400)                        // optional
                .into(dp);

        name.setText(sessionManager.getUserDetails().get("name"));
        email.setText(sessionManager.getUserDetails().get("email"));
        delete_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete_Mobile(sessionManager.getUserDetails().get("mobile"),sessionManager.getUserDetails().get("name"),sessionManager.getUserDetails().get("email"),sessionManager.getUserDetails().get("dp"),sessionManager.getUserDetails().get("uid"));
            }
        });

        add_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_Page.this,Add_Mobile_Number.class));

            }
        });
        Check_Mobile(sessionManager);


    }
    private void signOut() {
        // Firebase sign out


        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //  updateUI(null);
                    }
                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Profile_Page.this,Home_Screen.class));
        finish();
    }

    public void Check_Mobile(SessionManager sessionManager1)
    {

        if(sessionManager1.getUserDetails().get("mobile")==null)
        {
                ph_no.setText("Please Add Mobile No.");
                add_mobile.setVisibility(View.VISIBLE);
            delete_mobile.setVisibility(View.GONE);


        }
        else
        {
            ph_no.setText(sessionManager1.getUserDetails().get("mobile"));
            delete_mobile.setVisibility(View.VISIBLE);
            add_mobile.setVisibility(View.GONE);

             }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionManager sessionManager2 = new SessionManager(getApplicationContext());
        Check_Mobile(sessionManager2);
    }

    public void Delete_Mobile(final String mobile, final String name, final String email, final String dp, final String uid)
    {
        final SessionManager sessionManager1 = new SessionManager(getApplicationContext());
        final ProgressDialog progressDialog = new ProgressDialog(Profile_Page.this);
        progressDialog.setMessage("loading");
        progressDialog.setIndeterminate(true);

        final ArrayList<HashMap<String,String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.cancel();

                        if (s!=null)
                        {

                            if(s.equals("deleted"))
                            {
                                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
                                sessionManager1.createLoginSession(email,name,uid,dp,"");
                                add_mobile.setVisibility(View.VISIBLE);
                                delete_mobile.setVisibility(View.GONE);
                                Check_Mobile(sessionManager1);
                            }

                        }
                        else
                        {
                            delete_mobile.setVisibility(View.VISIBLE);
                            add_mobile.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Error Occured Try Again Later",Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        delete_mobile.setVisibility(View.VISIBLE);
                        add_mobile.setVisibility(View.GONE);
                        Toast.makeText(Profile_Page.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("number",mobile);

                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
