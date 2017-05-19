package com.lions.torque.caring.servicecar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.lions.torque.caring.adapters.MyBookings_Adapter;
import com.lions.torque.caring.sessions_manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Structs.Book_Track;
import Structs.Book_Track_Bean;
import Structs.Garage_Car_Bean;

public class MyBookings extends AppCompatActivity {

     String DOWN_URL = "http://www.car-ing.com/app/Get_User_Bookings.php";
    ListView listView;
    SessionManager sessionManager;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.booking_list);
        listView.setEmptyView(findViewById(R.id.emptyview_booking));
        sessionManager = new SessionManager(getApplicationContext());
        back = (LinearLayout)findViewById(R.id.back);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onBackPressed();
                return false;
            }
        });

        Get_Booking_Details(sessionManager.getUserDetails().get("uid"));
        Log.d("for user",sessionManager.getUserDetails().get("uid"));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("book_id", (String) listView.getAdapter().getItem(i));
                Intent intent = new Intent(MyBookings.this,Book_Tracking.class);
                intent.putExtra("data",bundle);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        Get_Booking_Details(sessionManager.getUserDetails().get("uid"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void Get_Booking_Details (final String user_id)
    {
       final ArrayList<Book_Track_Bean> data_book = new ArrayList<Book_Track_Bean>();
        final ProgressDialog progressDialog = new ProgressDialog(MyBookings.this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        final ArrayList<HashMap<String,String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s!=null)
                        {
                            Log.d("book_user",""+s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONArray jsonarray = jsonObject.getJSONArray("Book_Tracking");
                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    Book_Track_Bean data = new Book_Track_Bean();

                                    JSONObject jsonObject1 = jsonarray.getJSONObject(i);
                                    Log.d("address",jsonObject1.getString(Book_Track.address));
                                    data.setAddress(jsonObject1.getString(Book_Track.address));
                                    data.setAdvance(jsonObject1.getString(Book_Track.advance));
                                    data.setCar_code(jsonObject1.getString(Book_Track.car_code));
                                    data.setCar_name(jsonObject1.getString(Book_Track.car_name));
                                    data.setDate(jsonObject1.getString(Book_Track.Date));
                                    data.setLat(jsonObject1.getString(Book_Track.lat));
                                    data.setLongitude(jsonObject1.getString(Book_Track.longitude));
                                    data.setVend_id(jsonObject1.getString(Book_Track.vend_id));
                                    data.setVend_name(jsonObject1.getString(Book_Track.vend_name));
                                    data.setPrice(jsonObject1.getString(Book_Track.price));
                                    data.setTaxes(jsonObject1.getString(Book_Track.taxes));
                                    data.setStart_time(jsonObject1.getString(Book_Track.start_time));
                                    data.setStatus(jsonObject1.getString(Book_Track.status));
                                    data.setDiscount(jsonObject1.getString(Book_Track.discount));
                                    data.setId(jsonObject1.getString(Book_Track.id));
                                    data.setOtp(jsonObject1.getString("BOOK_OTP"));
                                    data.setBook_contact(jsonObject1.getString("BOOK_VENDOR_MOB"));
                                    data.setStart_date(jsonObject1.getString("BOOK_START_DATE"));


                                    data_book.add(data);

                                }
                                listView.setAdapter(new MyBookings_Adapter(data_book,getApplicationContext()));



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.cancel();

                        }
                        else
                        {

                            progressDialog.cancel();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        Toast.makeText(MyBookings.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("BOOK_USER",user_id);

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

}
