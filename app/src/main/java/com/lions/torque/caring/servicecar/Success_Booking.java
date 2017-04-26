package com.lions.torque.caring.servicecar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.lions.torque.caring.R;
import com.lions.torque.caring.sessions_manager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Structs.Book_Struct;

public class Success_Booking extends AppCompatActivity {

    TextView order_id, date, location, destination, price, distance;
    String Order_id, Date, Location, Destination, Distance, Price, Name, Vendor_Name, Vendor_number, vend_id, datetime;
    Button track;
    SessionManager sessionManager;
    String DOWN_URL = "http://www.car-ing.com/sendsms.php";

    String DOWN_URL1 = "http://www.car-ing.com/sendmail.php";
    String DOWN_URL5  = "http://www.car-ing.com/Send_Push_Vendor.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success__booking);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getBundleExtra("data");
        sessionManager = new SessionManager(getApplicationContext());
        order_id = (TextView)findViewById(R.id.order_id);
        date = (TextView)findViewById(R.id.order_date);
        destination = (TextView)findViewById(R.id.vendor_address);
        location = (TextView)findViewById(R.id.order_myaddress);
        price = (TextView)findViewById(R.id.order_price);
        distance = (TextView)findViewById(R.id.order_distance);
        Order_id = bundle.getString("order_id");
        Location = bundle.getString("order_location");
        Destination = bundle.getString("order_destination");
        Price = bundle.getString("order_price");
        Distance = bundle.getString("order_distance");
        Vendor_Name = bundle.getString("vendor_name");
        Vendor_number = bundle.getString("vendor_mob");
        vend_id = bundle.getString("ven_id");
        datetime = bundle.getString("date_time");

        Calendar c = Calendar.getInstance();
        track = (Button)findViewById(R.id.button);
        track.setText("Sending SMS regarding above Booking ");
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("book_id",Order_id);
                startActivity(new Intent(Success_Booking.this,Book_Tracking.class).putExtra("data",bundle1));
                finish();
            }
        });
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
         if(datetime!=null)
         {
          //   Date = df.format(datetime);
             date.setText(datetime);

         }
        else
         {
             Date = df.format(c.getTime());
            date.setText(""+Date);
         }
        order_id.setText(Order_id);
        location.setText(Location);
        destination.setText(Destination);
        price.setText(Price);
        distance.setText(Distance);
        Name = sessionManager.getUserDetails().get("name");
        //date.setText(Date);
        String message = "Hi "+Name+" we are delighted to receive your request for service, please wait while "+Vendor_Name+" confirms your request.";
        sendRegistrationToServer(vend_id);
        Generate_Booking(Order_id,Vendor_Name,sessionManager.getUserDetails().get("mobile"),message,sessionManager.getUserDetails().get("name"),"caring");

    }

    public boolean Generate_Booking(final String order_id, final String vendor_name, final String number, final String message, final String username, final String sender_id)
    {
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Sending Confirmation sms");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("sms_response",""+s.toString());
                        if(s!=null)
                        {
                            if(s.equals("Done"))
                            {
                                track.setText("SMS Sent");
                                Generate_Booking_Email(order_id,vendor_name,username,sessionManager.getUserDetails().get("email"));


                            }
                            else
                            {
                                track.setText("SMS Sent");
                                Generate_Booking_Email(order_id,vendor_name,username,sessionManager.getUserDetails().get("email"));

                            }
                        }
                        else
                        {
                            track.setText("Sending Mail");
                            Toast.makeText(getApplicationContext(),"Sms may not be delivered",Toast.LENGTH_SHORT).show();
                            Generate_Booking_Email(order_id,vendor_name,username,sessionManager.getUserDetails().get("email"));

                        }

                        //progressDialog.cancel();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //progressDialog.cancel();
                        track.setText("SMS Sent");
                        Log.d("error",""+volleyError.getMessage()+" "+volleyError.toString()+" ");
                        Toast.makeText(Success_Booking.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("book_id",order_id);
                Keyvalue.put("vendor_name",vendor_name);
                Keyvalue.put("number",number);
                Keyvalue.put("sender",sender_id);
                Keyvalue.put("message",message);
                Keyvalue.put("username",username);
                Keyvalue.put("vendor_mob",Vendor_number);


                Log.d("sms_cut",""+Keyvalue);
                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);
        track.setText("SMS Sent");

        return  true;
    }

    public boolean Generate_Booking_Email(final String order_id, final String vendor_name, final String username, final String user_email)
    {
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Sending Confirmation sms");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
       // progressDialog.show();
        track.setText("Sending Mail..");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s!=null)
                        {
                            Log.d("respose",s);
                            track.setText("CLICK TO TRACK");

                        }

                        track.setText("CLICK TO TRACK");


                        //progressDialog.cancel();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                      //  progressDialog.cancel();

                        track.setText("CLICK TO TRACK");

                        Log.d("error",""+volleyError.getMessage()+" "+volleyError.toString()+" ");
                        Toast.makeText(Success_Booking.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("book_id",order_id);
                Keyvalue.put("vendor_name",vendor_name);
                Keyvalue.put("username",username);
                Keyvalue.put("email",user_email);
                Log.d("email_cut",""+Keyvalue);
                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);

        track.setText("CLICK TO TRACK");

        return  true;
    }

    private void sendRegistrationToServer(final String ven_id) {
        // sending gcm token to server
        // Log.e(TAG, "sendRegistrationToServer: " + reg_id);
        final ProgressDialog progressDialog = new ProgressDialog(Success_Booking.this);
        progressDialog.setMessage("Notifying....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.cancel();
                        Log.d("response_registration",s);
                        if (s!=null)
                        {
                            Log.d("response_registration",s);
                        }
                        else
                        {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        // Toast.makeText(MyFirebaseInstanceIDService.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("VEN_ID",ven_id);
                Log.d("push_final_cut",Keyvalue.toString()+"");
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);





    }

}
