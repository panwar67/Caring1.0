package com.lions.torque.caring.servicecar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Line;
import com.lions.torque.caring.R;
import com.lions.torque.caring.adapters.Adapter_Garage_Car;
import com.lions.torque.caring.adapters.Checkout_Service_Adapter;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.Location_Session;
import com.lions.torque.caring.sessions_manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Structs.Book_Struct;
import Structs.Car_Struct;
import Structs.CustomTimePickerDialog;
import Structs.ExpandableHeightGridView;
import Structs.Garage_Car_Bean;
import Structs.Vendor_List_Bean;

public class Review_Vendor extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    LinearLayout back;
    Calendar calendar;
    Car_Session car_session;
    Location_Session location_session;
    TimePicker timePicker;
    TextView contact, contact_head, car_name, car_model, car_brand, review_header, car_header, booking, time_header, sub_total, taxes, total, sub_head, tax_head, total_head, booking_head;
    ArrayList<HashMap<String,String>> serve_list = new ArrayList<HashMap<String, String>>();
    Bundle bundle = new Bundle();
    String DOWN_URL = "http://www.car-ing.com/app/Book_Vendor.php";
    String time_open, time_close, booking_amount, vend_name, vend_code;
    ExpandableHeightGridView expandableHeightGridView;
    EditText comments;
    Button checkout;
    String hour, min;
    Vendor_List_Bean vendor_list_bean = new Vendor_List_Bean();
    DBHelper dbHelper;
    CustomTimePickerDialog customTimePickerDialog;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review__vendor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"OpenSans.ttf");
        car_session = new Car_Session(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        calendar = Calendar.getInstance();
        Intent intent = getIntent();
        contact = (TextView)findViewById(R.id.contactnumber);
        contact_head = (TextView)findViewById(R.id.contact_header);
        sessionManager = new SessionManager(getApplicationContext());
        dbHelper = new DBHelper(getApplicationContext());
        bundle = intent.getBundleExtra("data");
        time_open = bundle.getString("time_open");
        time_close = bundle.getString("time_close");
        booking_amount = bundle.getString("booking_amount");
        vend_name = bundle.getString("book_vend_name");
        vend_code = bundle.getString("book_vend_id");
        vendor_list_bean = dbHelper.Get_Vendor_Profile(vend_code,location_session.getUserDetails().get("lat"),location_session.getUserDetails().get("long"));
        serve_list = (ArrayList<HashMap<String,String>>) bundle.getSerializable("serve_list");
        expandableHeightGridView = (ExpandableHeightGridView)findViewById(R.id.vendor_service_review);
        expandableHeightGridView.setExpanded(true);
        expandableHeightGridView.setNumColumns(1);
        expandableHeightGridView.setAdapter(new Checkout_Service_Adapter(getApplicationContext(),serve_list));
        checkout = (Button)findViewById(R.id.checkout_payment);
        comments = (EditText)findViewById(R.id.checkout_comments);
        back = (LinearLayout)findViewById(R.id.back);
        review_header =  (TextView)findViewById(R.id.review_heading);
        car_header = (TextView)findViewById(R.id.selected_car_review);
        booking = (TextView)findViewById(R.id.booking_header);
        sub_total = (TextView)findViewById(R.id.booking_sub_total);
        taxes = (TextView)findViewById(R.id.booking_tax);
        total = (TextView)findViewById(R.id.booking_total);
        sub_head = (TextView)findViewById(R.id.subtotal);
        tax_head = (TextView)findViewById(R.id.taxes);
        total_head = (TextView)findViewById(R.id.total);
        booking_head = (TextView)findViewById(R.id.booking_header);
        sub_head.setTypeface(typeface);
        contact_head.setTypeface(typeface);
        contact.setTypeface(typeface);
        contact.setText(sessionManager.getUserDetails().get("mobile"));
        tax_head.setTypeface(typeface);
        total_head.setTypeface(typeface);
        sub_total.setTypeface(typeface);
        taxes.setTypeface(typeface);
        total.setTypeface(typeface);
        time_header = (TextView)findViewById(R.id.schedule_header);
        review_header.setTypeface(typeface);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManager.getUserDetails().get("mobile")==null)
                {
                    Toast.makeText(getApplicationContext(),"Please add mobile no. for booking ",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Review_Vendor.this,Add_Mobile_Number.class));

                }
                else
                {
                    try {


                        Generate_Booking(""+comments.getText().toString(),vendor_list_bean.getVend_Address(),car_session.getUserDetails().get("CAR_CODE"),
                                car_session.getUserDetails().get("CAR_MODEL"),"123","pay123",total.getText().toString(),
                                hour+" "+min,"PENDING",vend_code,vend_name,Get_Booking_Details(serve_list).toString(),vendor_list_bean.getVend_Lat(),vendor_list_bean.getVend_long(),taxes.getText().toString(),sessionManager.getUserDetails().get("uid"),vendor_list_bean.getVend_Contact());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        car_header.setTypeface(typeface);
        booking.setTypeface(typeface);
        time_header.setTypeface(typeface);
        car_name = (TextView)findViewById(R.id.ven_car_name);
        car_model = (TextView)findViewById(R.id.ven_car_model);
        car_brand = (TextView)findViewById(R.id.ven_car_brand);
        timePicker = (TimePicker)findViewById(R.id.checkout_time);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {


                hour = ""+i+"";
                min = ""+i1+"";
                Log.d("selected_time",""+i+" , "+i1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d("selected_time_changed",""+timePicker.getHour()+" "+timePicker.getMinute());
                    hour = String.valueOf(timePicker.getHour());
                    min = String.valueOf(timePicker.getMinute());
                }

            }
        });

        car_name.setTypeface(typeface);
        car_brand.setTypeface(typeface);
        car_model.setTypeface(typeface);
        car_name.setText(car_session.getUserDetails().get("CAR_NAME"));
        car_model.setText(car_session.getUserDetails().get("CAR_MODEL"));
        car_brand.setText(car_session.getUserDetails().get("CAR_BRAND"));
        Display_Booking_Details();

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

    }

    public JSONArray Get_Booking_Details(ArrayList<HashMap<String,String>> data) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<data.size();i++)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SERVE_NAME",data.get(i).get("SERVE_NAME"));
            jsonObject.put("SERVE_ID",data.get(i).get("SERVE_ID"));
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public void Display_Booking_Details()
    {
        sub_total.setText(""+booking_amount);
        float tax = (Integer.parseInt(booking_amount)*5)/100;
        taxes.setText(""+tax);
        float tot = Integer.parseInt(booking_amount) + tax;
        total.setText(""+tot);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sessionManager = new SessionManager(getApplicationContext());
        contact.setText(sessionManager.getUserDetails().get("mobile"));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {

        Log.d("selected_time",""+i+" , "+i1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("selected_time_changed",""+timePicker.getHour()+" "+timePicker.getMinute());
        }

    }

    public boolean Generate_Booking(final String book_des, final String book_address,
                                    final String book_car_code, final String book_car_name, final String book_date, final String book_payid,
                                    final String book_price, final String book_start_time,
                                    final String book_status, final String book_vend, final String book_vend_name,
                                    final String book_details, final String book_lat, final String book_long, final String book_taxes, final String book_user, final String book_contact)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s!=null)
                        {
                            Log.d("response",""+s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                jsonObject.get("book_id");
                                Bundle bundle = new Bundle();
                                bundle.putString("order_id", String.valueOf(jsonObject.get("book_id")));
                                bundle.putString("order_destination",vendor_list_bean.getVend_Address());
                                bundle.putString("order_location",location_session.getUserDetails().get("address"));
                                bundle.putString("order_price","0");
                                bundle.putString("order_distance", String.valueOf(vendor_list_bean.getVend_Distance()));
                                bundle.putString("vendor_name",vendor_list_bean.getVend_Name());
                                bundle.putString("vendor_mob",vendor_list_bean.getVend_Contact());

                                startActivity(new Intent(Review_Vendor.this,Success_Booking.class).putExtra("data",bundle));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("error",""+volleyError.getMessage()+" "+volleyError.toString()+" ");
                        Toast.makeText(Review_Vendor.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put(Book_Struct.Book_des,book_des);
                Keyvalue.put(Book_Struct.Book_address,book_address);
                Keyvalue.put(Book_Struct.Book_car_code,book_car_code);
                Keyvalue.put(Book_Struct.Book_car_name,book_car_name);
                Keyvalue.put(Book_Struct.Book_payid,book_payid);
                Keyvalue.put(Book_Struct.Book_advance,book_price);
                Keyvalue.put(Book_Struct.Book_discount,"0");
                Keyvalue.put(Book_Struct.Book_lat,book_lat);
                Keyvalue.put(Book_Struct.Book_long,book_long);
                Keyvalue.put(Book_Struct.Book_start_time,book_start_time);
                Keyvalue.put(Book_Struct.Book_status,book_status);
                Keyvalue.put(Book_Struct.Book_vend_id,book_vend);
                Keyvalue.put(Book_Struct.Book_vend_name,book_vend_name);
                Keyvalue.put(Book_Struct.Book_taxes,book_taxes);
                Keyvalue.put("BOOK_DETAILS",book_details);
                Keyvalue.put("BOOK_USER",book_user);
                Keyvalue.put("book_user_name",sessionManager.getUserDetails().get("name"));
                Keyvalue.put("book_user_mob",sessionManager.getUserDetails().get("mobile"));
                Keyvalue.put("book_vend_mob",book_contact);
                Keyvalue.put("book_user_lat",location_session.getUserDetails().get("lat"));
                Keyvalue.put("book_user_long",location_session.getUserDetails().get("long"));
                Keyvalue.put("book_start_date","4/4");
                Random r = new Random();
                int i1 = r.nextInt(9997 - 1001);
                Keyvalue.put("book_otp",i1+"");

                Log.d("final_cut_review",""+Keyvalue);
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

