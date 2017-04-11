package com.lions.torque.caring.servicecar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lions.torque.caring.R;
import com.lions.torque.caring.sessions_manager.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class Add_Mobile_Number extends AppCompatActivity {

    String DOWN_URL = "http://www.car-ing.com/app/send_otp.php";
    String DOWN_URL1 = "http://www.car-ing.com/app/verify_otp.php";
    CardView cardView;
    EditText mobile, otp;
    Button requestotp, enterotp;
    SessionManager sessionManager;
    LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__mobile__number);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        cardView = (CardView)findViewById(R.id.otp_view);
        setSupportActionBar(toolbar);
        back = (LinearLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sessionManager = new SessionManager(getApplicationContext());
        mobile = (EditText)findViewById(R.id.edit_number);
        otp = (EditText)findViewById(R.id.edit_otp);
        requestotp = (Button)findViewById(R.id.request_otp);
        enterotp = (Button)findViewById(R.id.enter_otp);

        requestotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Generate_OTP(sessionManager.getUserDetails().get("uid"),mobile.getText().toString(),"carotp");
            }
        });

        enterotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp.getText().toString()==null)
                {
                    Toast.makeText(getApplicationContext(),"Please enter OTP",Toast.LENGTH_SHORT).show();
                }
                else
                    Verify_Mobile(sessionManager.getUserDetails().get("uid"),mobile.getText().toString(),otp.getText().toString(),sessionManager.getUserDetails().get("email"),"",sessionManager.getUserDetails().get("dp"),mobile.getText().toString(),sessionManager.getUserDetails().get("name"));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean Generate_OTP(final String user_id, final String number, final String sender_id)
    {
        final ProgressDialog progressDialog = new ProgressDialog(Add_Mobile_Number.this);
        progressDialog.setMessage("Sending Confirmation sms");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.cancel();
                        if(s!=null)
                        {
                            if(s.equals("Done"))
                            {
                                cardView.setVisibility(View.VISIBLE);
                                Toast.makeText(Add_Mobile_Number.this,"You will receive otp in few seconds",Toast.LENGTH_SHORT).show();

                            }
                            if(s.equals("Exists"))
                            {
                                Toast.makeText(Add_Mobile_Number.this,"Number Exists",Toast.LENGTH_SHORT).show();
                            }
                          }

                        else
                        {
                            Toast.makeText(Add_Mobile_Number.this,"Error Occured, Try again later",Toast.LENGTH_SHORT).show();
                        }


                        Log.d("res",""+s.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        Log.d("error",""+volleyError.getMessage()+" "+volleyError.toString()+" ");
                        Toast.makeText(Add_Mobile_Number.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("number","91"+number);
                Keyvalue.put("sender",sender_id);
                Keyvalue.put("user_id",user_id);
                Log.d("final_cut",""+Keyvalue);
                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);

        return  true;
    }

    public boolean Verify_Mobile(final String user_id, final String number,  final String otp, final String email, final String uid, final String dp, final String mob, final String name)
    {
        final ProgressDialog progressDialog = new ProgressDialog(Add_Mobile_Number.this);
        progressDialog.setMessage("Sending Confirmation sms");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.cancel();
                        if(s!=null)
                        {
                            s= s.replace(" ","");

                            if(s.contains("done"))
                            {
                                sessionManager.createLoginSession(email,name,user_id,dp,mob);
                                Toast.makeText(getApplicationContext(),"Number Verified and added",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Error Occured, Try again later",Toast.LENGTH_SHORT).show();
                            }
                        }

                        Log.d("verify",""+s.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        Log.d("error",""+volleyError.getMessage()+" "+volleyError.toString()+" ");
                        Toast.makeText(Add_Mobile_Number.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("number","91"+number);
                Keyvalue.put("otp",otp);
                Keyvalue.put("user_id",user_id);
                Log.d("final_cut",""+Keyvalue);
                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);

        return  true;
    }


}
