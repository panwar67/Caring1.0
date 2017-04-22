package com.lions.torque.caring.servicecar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
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
import com.lions.torque.caring.adapters.Adapter_Garage_Car;
import com.lions.torque.caring.adapters.Service_Track_Adapter;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.sessions_manager.Location_Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Structs.Book_Struct;
import Structs.Book_Track;
import Structs.Book_Track_Bean;
import Structs.Car_Struct;
import Structs.ExpandableHeightGridView;
import Structs.Garage_Car_Bean;

public class Book_Tracking extends AppCompatActivity {

    TextView sub_total,navhead, subtotal_head, taxes, taxes_head, advance, advance_head, discount, discout_head, total, total_head, address, vend_name, invoice_head;
    LinearLayout directions,back;
    ExpandableHeightGridView expandableHeightGridView;
    DBHelper dbHelper;
    Location_Session location_session;
    Book_Track_Bean book_track_bean;
    String DOWN_URL = "http://www.car-ing.com/app/Get_Book_Tracking.php";
    String DOWN_URL1 = "http://www.car-ing.com/app/Get_Book_Services.php";
    String book_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        expandableHeightGridView = (ExpandableHeightGridView)findViewById(R.id.track_service_list);
        expandableHeightGridView.setExpanded(true);
        back = (LinearLayout)findViewById(R.id.back);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onBackPressed();
                return false;
            }
        });
        navhead = (TextView)findViewById(R.id.navhead);

        expandableHeightGridView.setNumColumns(1);
        sub_total = (TextView)findViewById(R.id.book_track_subtotal);
        taxes = (TextView)findViewById(R.id.book_track_taxes);
        address = (TextView)findViewById(R.id.booK_track_address);
        taxes_head = (TextView)findViewById(R.id.book_track_taxes_head);
        subtotal_head = (TextView)findViewById(R.id.book_track_subtotal_head);
        advance = (TextView)findViewById(R.id.book_track_advance);
        advance_head = (TextView)findViewById(R.id.book_track_advance_head);
        discount = (TextView)findViewById(R.id.book_track_discount);
        discout_head = (TextView)findViewById(R.id.book_track_discount_head);
        total = (TextView)findViewById(R.id.book_tracK_total);
        total_head = (TextView)findViewById(R.id.book_track_total_head);
        vend_name = (TextView)findViewById(R.id.book_track_vend_name);
        invoice_head = (TextView)findViewById(R.id.book_track_invoice_head);
        directions = (LinearLayout)findViewById(R.id.track_directions);
        book_track_bean = new Book_Track_Bean();

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%s,%s(%s)&daddr=%s,%s (%s)", location_session.getUserDetails().get("lat"), location_session.getUserDetails().get("long"), "Your Location", book_track_bean.getLat(), book_track_bean.getLongitude(), book_track_bean.getVend_name());
                Log.d("url",""+uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);

            }
        });
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"SourceSansProLight.otf");
        sub_total.setTypeface(typeface);
        navhead.setTypeface(typeface);
        subtotal_head.setTypeface(typeface);
        taxes.setTypeface(typeface);
        taxes_head.setTypeface(typeface);
        sub_total.setTypeface(typeface);
        subtotal_head.setTypeface(typeface);
        advance.setTypeface(typeface);
        advance_head.setTypeface(typeface);
        discout_head.setTypeface(typeface);
        discount.setTypeface(typeface);
        total.setTypeface(typeface);
        total_head.setTypeface(typeface);
        address.setTypeface(typeface);
        vend_name.setTypeface(typeface);
        invoice_head.setTypeface(typeface);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getBundleExtra("data");
        book_id = bundle.getString("book_id");
        Get_Booking_Details(book_id);



    }

    public Book_Track_Bean Get_Booking_Details (final String booking_id)
    {
        final Book_Track_Bean data = new Book_Track_Bean();
        final ArrayList<Garage_Car_Bean> temp = new ArrayList<Garage_Car_Bean>();
        final ProgressDialog progressDialog = new ProgressDialog(Book_Tracking.this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        final ArrayList<HashMap<String,String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s!=null)
                        {
                            Log.d("car_fetched_user",""+s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONArray jsonarray = jsonObject.getJSONArray("Book_Tracking");
                                JSONObject jsonObject1 = jsonarray.getJSONObject(0);
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
                                data.setId(booking_id);
                                data.setOtp(jsonObject1.getString("BOOK_OTP"));

                                book_track_bean =data;
                                SetUp_Tracking_Page(data);



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
                        Toast.makeText(Book_Tracking.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("book_id",book_id);

                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);
        Log.d("bean_track",""+data.getVend_id());
        return data;
    }

    public void SetUp_Services(final String Book_id)
    {
        final ArrayList<HashMap<String,String>> master = new ArrayList<HashMap<String, String>>();
        final ProgressDialog progressDialog = new ProgressDialog(Book_Tracking.this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        final ArrayList<HashMap<String,String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s!=null)
                        {
                            Log.d("book_details",""+s);
                            try {

                                JSONObject jsonObject = new JSONObject(s);
                                JSONArray jsonArray = jsonObject.getJSONArray("Book_Details");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put("BOOK_SERVE_NAME",jsonObject1.getString("BOOK_SERVICE_NAME"));
                                    map.put("BOOK_SERVE_ID",jsonObject1.getString("BOOK_SERVICE_CODE"));
                                    map.put("BOOK_TRACK",jsonObject1.getString("BOOK_TRACK"));
                                    master.add(map);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            expandableHeightGridView.setAdapter(new Service_Track_Adapter(getApplicationContext(),master));
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
                        Toast.makeText(Book_Tracking.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("book_id",Book_id);

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


    public void SetUp_Tracking_Page(Book_Track_Bean book_track_bean)
    {

        //Get_Booking_Details(id);
        SetUp_Services(book_id);
        Log.d("bean_track_2",""+book_track_bean.getVend_id());
        sub_total.setText(book_track_bean.getPrice());
        taxes.setText(book_track_bean.getTaxes());
        advance.setText(book_track_bean.getAdvance());
        discount.setText(book_track_bean.getDiscount());
        try
        {
            float sum = Float.parseFloat(book_track_bean.getPrice())+Float.parseFloat(book_track_bean.getTaxes())-Float.parseFloat(book_track_bean.getAdvance())-Float.parseFloat(book_track_bean.getDiscount());
            total.setText(""+sum);

        }
        catch (NumberFormatException e)
        {
            float sum = Float.parseFloat(book_track_bean.getPrice())+Float.parseFloat(book_track_bean.getTaxes())-Float.parseFloat(book_track_bean.getAdvance())-0;
            total.setText(""+sum);


        }
        address.setText(book_track_bean.getAddress());
        vend_name.setText(book_track_bean.getVend_name());



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
