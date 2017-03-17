package com.lions.torque.caring.servicecar;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.lions.torque.caring.adapters.Adapter_Vendor_List;
import com.lions.torque.caring.filters.Filter_Vendor_List;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.Location_Session;
import com.lions.torque.caring.sessions_manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Structs.Car_Struct;
import Structs.Vendor_List_Bean;
import Structs.Vendor_List_Struct;
import lj_3d.gearloadinglayout.gearViews.TwoGearsLayout;

public class Display_Vendor_List extends AppCompatActivity {

    ListView listView;
    TextView filter;
    TwoGearsLayout twoGearsLayout;
    SessionManager sessionManager;
    Location_Session location_session;
    ArrayList<Vendor_List_Bean> vendor_list = new ArrayList<Vendor_List_Bean>();
    Adapter_Vendor_List adapter_vendor_list;
    Car_Session car_session;
    ImageView back;
    String DOWN_URL = "http://www.car-ing.com/app/Get_Vendors_Service_Car.php";
    String service = "";
    String car = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__vendor__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());



        back  = (ImageView)findViewById(R.id.back_vendor_list);
        filter = (TextView)findViewById(R.id.filter_vendor_list);

        Intent intent =getIntent();
        listView = (ListView)findViewById(R.id.vendor_list);
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(location_session.getUserDetails().get("lat")));
        Log.d("lat_session",""+Double.parseDouble(location_session.getUserDetails().get("lat")));
        Log.d("long_session",""+Double.parseDouble(location_session.getUserDetails().get("long")));

        location.setLongitude(Double.parseDouble(location_session.getUserDetails().get("long")));
        //service = intent.getStringExtra("service");
        car = car_session.getUserDetails().get(Car_Struct.Car_Code);
        Bundle bundle = intent.getBundleExtra("data");
        service =  bundle.getString("service");
        vendor_list = (ArrayList<Vendor_List_Bean>) bundle.getSerializable("vendor_list");

        //listView.setAdapter(new Adapter_Vendor_List(getApplicationContext(),vendor_list));
        listView.setAdapter(new Adapter_Vendor_List(getApplicationContext(),vendor_list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = String.valueOf(listView.getAdapter().getItem(i));
                Intent intent1 = new Intent(Display_Vendor_List.this,Vendor_Profile.class);
                intent1.putExtra("vendor_id",id);
                startActivity(intent1);

            }
        });


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(Display_Vendor_List.this,Filter_Vendor_List.class);
                intent1.putExtra("service",service);
                intent1.putExtra("car",car);
                startActivity(intent1);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public ArrayList<Vendor_List_Bean> Get_Vendor_Service_Car(final String service, final String car, final Location my_location)
    {
        final ArrayList<Vendor_List_Bean> vend_data = new ArrayList<Vendor_List_Bean>();
        //twoGearsLayout = (TwoGearsLayout)findViewById(R.id.two_gear);

        twoGearsLayout.enableCutLayout(false);
        twoGearsLayout.blurBackground(true);
        twoGearsLayout.rotateByValue((float) 4.0);
        twoGearsLayout.enableCutLayout(false);
        twoGearsLayout.start();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s)
                    {
                        Log.d("response",""+s);
                        if (s!=null)
                        {

                            try {
                                JSONObject profile = new JSONObject(s);
                                JSONArray data = profile.getJSONArray("Service");
                                for(int i=0;i<data.length();i++)
                                {

                                    JSONObject details = data.getJSONObject(i);
                                    Vendor_List_Bean vendor_list_bean = new Vendor_List_Bean();
                                    Location location = new Location("");
                                    vendor_list_bean.setVend_id(details.getString(Vendor_List_Struct.Vend_id));
                                    vendor_list_bean.setVend_Lat(details.getString(Vendor_List_Struct.Vend_Lat));
                                    vendor_list_bean.setVend_long(details.getString(Vendor_List_Struct.Vend_long));
                                    location.setLatitude(Double.parseDouble(details.getString(Vendor_List_Struct.Vend_Lat)));
                                    location.setLongitude(Double.parseDouble(details.getString(Vendor_List_Struct.Vend_long)));
                                    vendor_list_bean.setVend_Distance(location.distanceTo(my_location));
                                    Log.d("vendor_distance",""+location.distanceTo(my_location));
                                    vendor_list_bean.setVend_Name(details.getString(Vendor_List_Struct.Vend_Name));
                                    vendor_list_bean.setVend_price_high(details.getString(Vendor_List_Struct.Vend_price_high));
                                    vendor_list_bean.setVend_price_low(details.getString(Vendor_List_Struct.Vend_price_low));
                                    vendor_list_bean.setVend_quanlity(details.getString(Vendor_List_Struct.Vend_quanlity));
                                    vendor_list_bean.setVend_Assure(details.getString(Vendor_List_Struct.Vend_assure));
                                    vendor_list_bean.setVend_Segment(details.getString(Vendor_List_Struct.Vend_segment));
                                    vendor_list_bean.setVend_Timings_Open(Integer.parseInt(details.getString(Vendor_List_Struct.Vend_Timings_Open)));
                                    vendor_list_bean.setVend_Timings_Close(Integer.parseInt(details.getString(Vendor_List_Struct.Vend_Timings_Close)));
                                    Log.d("vendor_id",details.getString(Vendor_List_Struct.Vend_id));
                                    vend_data.add(vendor_list_bean);

                                }
                                Log.d("vendors_fetched", s);
                                // loading.dismiss();
//                                masterdata.put("buy_request",buyreq);




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            twoGearsLayout.stop();
                            twoGearsLayout.hide();
                            twoGearsLayout.setVisibility(View.GONE);
                            //twoGearsLayout.removeAllViews();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.d("voley error",volleyError.toString());
                        //Showing toast
                        twoGearsLayout.stop();
                        twoGearsLayout.removeAllViews();
                        Toast.makeText(Display_Vendor_List.this, "Error Occured", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("service",service);
                Keyvalue.put("car",car);
                Log.d("parameteres",""+service+" "+car);



                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);






        return vend_data;

        //   return  true;
    }



}
