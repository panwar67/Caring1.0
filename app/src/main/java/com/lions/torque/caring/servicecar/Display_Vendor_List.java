package com.lions.torque.caring.servicecar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Line;
import com.lions.torque.caring.R;
import com.lions.torque.caring.adapters.Adapter_Vendor_List;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.filters.Filter_Vendor_List;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.Location_Session;
import com.lions.torque.caring.sessions_manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import Structs.Car_Struct;
import Structs.Ven_List_Struct;
import Structs.Vendor_List_Bean;
import Structs.Vendor_List_Struct;
import lj_3d.gearloadinglayout.gearViews.TwoGearsLayout;

public class Display_Vendor_List extends AppCompatActivity {

    ListView listView;
    Adapter_Vendor_List adapter_vendor_list;
    TwoGearsLayout twoGearsLayout;
    LinearLayout linearLayout, change_car, change_order, change_location;
    SessionManager sessionManager;
    TextView change_carr, change_orderr, change_loc;
    Location_Session location_session;
    ArrayList<Vendor_List_Bean> vendor_list = new ArrayList<Vendor_List_Bean>();
    Car_Session car_session;
    TextView address;
    ImageView back;
    String DOWN_URL = "http://www.car-ing.com/app/Get_Vendors_Service_Car.php";
    String service = "";
    String car = "";
    String order = "";
    String service_name;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__vendor__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());
        dbHelper = new DBHelper(getApplicationContext());
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"gothiclit.ttf");
        int flags =  Paint.SUBPIXEL_TEXT_FLAG
                | Paint.ANTI_ALIAS_FLAG;
        back  = (ImageView)findViewById(R.id.back_vendor_list);
        linearLayout = (LinearLayout)findViewById(R.id.back);
        change_car = (LinearLayout)findViewById(R.id.display_vendor_change_car);
        change_order = (LinearLayout)findViewById(R.id.display_vendor_change_order);
        change_location = (LinearLayout)findViewById(R.id.change_location);
        address = (TextView)findViewById(R.id.screen_address);
        change_carr = (TextView)findViewById(R.id.change_car);
        change_orderr =  (TextView)findViewById(R.id.change_order);
        change_carr.setTypeface(typeface);
        change_orderr.setTypeface(typeface);
        address.setText(""+location_session.getUserDetails().get("address")+"");
        address.setTypeface(typeface);
        address.setPaintFlags(flags);
        change_carr.setPaintFlags(flags);
        change_orderr.setPaintFlags(flags);
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
        service_name = bundle.getString("service_name");
        //order = bundle.getString("order");

        vendor_list = dbHelper.Get_Vendor_By_All(service,car,location);
        Get_Location_Sorted_List(vendor_list);

        adapter_vendor_list  = new Adapter_Vendor_List(getApplicationContext(),vendor_list);
        listView.setAdapter(adapter_vendor_list);


        change_location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Get_Location_Autocomplete();

                return false;
            }
        });


        change_car.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                startActivity(new Intent(Display_Vendor_List.this,Garage_Page.class));


                return false;
            }
        });

        change_order.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                CharSequence[] items = { "Distance", "Price", "Ratings" };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Display_Vendor_List.this)
                        .setTitle("Choose Order")
                        .setSingleChoiceItems( items, 0, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                        Get_Location_Sorted_List(vendor_list);
                                        adapter_vendor_list.setData(vendor_list);
                                        adapter_vendor_list.notifyDataSetChanged();

                                }
                                if (which==1)
                                {
                                        Get_Price_Sorted_List(vendor_list);

                                    adapter_vendor_list.setData(vendor_list);
                                    adapter_vendor_list.notifyDataSetChanged();
                                }
                                if (which==2)
                                {
                                    Get_Rating_Sorted_List(vendor_list);
                                    Collections.reverse(vendor_list);
                                    adapter_vendor_list.setData(vendor_list);
                                    adapter_vendor_list.notifyDataSetChanged();
                                }
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog2 = builder2.create();
                alertdialog2.show();
                return false;
            }
        });
        //listView.setAdapter(new Adapter_Vendor_List(getApplicationContext(),vendor_list));



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = String.valueOf(listView.getAdapter().getItem(i));
                Intent intent1 = new Intent(Display_Vendor_List.this,Vendor_Profile.class);
                intent1.putExtra("vendor_id",id);
                intent1.putExtra("service",service);
                intent1.putExtra("service_name",service_name);
                startActivity(intent1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               onBackPressed();
                return false;
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        location_session = new Location_Session(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());
        car = car_session.getUserDetails().get(Car_Struct.Car_Code);
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(location_session.getUserDetails().get("lat")));
        Log.d("lat_session",""+Double.parseDouble(location_session.getUserDetails().get("lat")));
        Log.d("long_session",""+Double.parseDouble(location_session.getUserDetails().get("long")));
        location.setLongitude(Double.parseDouble(location_session.getUserDetails().get("long")));
        vendor_list.clear();
        vendor_list = dbHelper.Get_Vendor_By_All(service,car,location);
        Get_Location_Sorted_List(vendor_list);
        adapter_vendor_list.setData(vendor_list);
        adapter_vendor_list.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {


               /* Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("home_screen_place", "Place: " + place.getName()+" "+place.getAddress());
                LatLng latLng = place.getLatLng();
                location_session.create_Location_Session(place.getAddress().toString(),String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
                home_screen_address.setText(""+place.getAddress());
                */
                Place place = PlacePicker.getPlace(data, this);
                LatLng latLng = place.getLatLng();
                location_session.create_Location_Session(place.getAddress().toString(),String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
                address.setText(""+place.getAddress());

                String toastMsg = String.format("Place: %s", place.getName());

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("home_screen", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
            }
        }


        if(requestCode==67)
        {

            onResume();

        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void Get_Location_Autocomplete()
    {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Vendor_List_Bean> Get_Location_Sorted_List(ArrayList<Vendor_List_Bean> data)
    {
        Collections.sort(data, new Comparator<Vendor_List_Bean>()
        {
            @Override
            public int compare(Vendor_List_Bean vendor_list_bean, Vendor_List_Bean t1) {

                Float dist1 = vendor_list_bean.getVend_Distance();
                Float dist2 = t1.getVend_Distance();
                return dist1.compareTo(dist2);
            }
        });

        return data;
    }


    public ArrayList<Vendor_List_Bean> Get_Rating_Sorted_List(ArrayList<Vendor_List_Bean> data)
    {
        Collections.sort(data, new Comparator<Vendor_List_Bean>()
        {
            @Override
            public int compare(Vendor_List_Bean vendor_list_bean, Vendor_List_Bean t1) {

                Float dist1 = Float.valueOf(vendor_list_bean.getVend_quanlity());
                Float dist2 = Float.valueOf(t1.getVend_quanlity());
                return dist1.compareTo(dist2);
            }
        });

        return data;
    }

    public ArrayList<Vendor_List_Bean> Get_Price_Sorted_List(ArrayList<Vendor_List_Bean> data)
    {
        Collections.sort(data, new Comparator<Vendor_List_Bean>()
        {
            @Override
            public int compare(Vendor_List_Bean vendor_list_bean, Vendor_List_Bean t1) {

                Float dist1 = Float.valueOf(vendor_list_bean.getVend_price_low());
                Float dist2 = Float.valueOf(t1.getVend_price_low());
                return dist1.compareTo(dist2);
            }
        });

        return data;
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
                                    vendor_list_bean.setVend_Address(details.getString(Ven_List_Struct.Ven_Address));
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
