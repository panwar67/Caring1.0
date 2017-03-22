package com.lions.torque.caring.servicecar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.lions.torque.caring.adapters.Adapter_Garage_Car;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Structs.Car_Struct;
import Structs.Garage_Car_Bean;

public class Garage_Page extends AppCompatActivity {

    DBHelper dbHelper;
    ListView listView;
    String DOWN_URL = "http://www.car-ing.com/app/Get_Vendor_Car.php";
    SessionManager sessionManager;
    Car_Session car_session;
    ImageView back ;
    TextView textView;
    ArrayList<Garage_Car_Bean> data = new ArrayList<Garage_Car_Bean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage__page);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = DBHelper.getInstance(getApplicationContext());
        listView = (ListView)findViewById(R.id.garage_car_list);
        sessionManager = new SessionManager(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());
        back = (ImageView)findViewById(R.id.garage_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        textView  = (TextView) findViewById(R.id.garage_label);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"OpenSans.ttf");
        textView.setTypeface(typeface);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Garage_Page.this,Add_Car.class));
                finish();
            }
        });

         data =  Get_Car(sessionManager.getUserDetails().get(SessionManager.KEY_UID));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                car_session.create_Location_Session(data.get(i).getCar_Brand(),data.get(i).getCar_Code(),data.get(i).getCar_FUEL(),data.get(i).getCar_Model(),data.get(i).getCar_Name(),data.get(i).getCar_Segment(),data.get(i).getCar_Year());
                Log.d("car_session",car_session.getUserDetails().get(Car_Struct.Car_Model));
                onBackPressed();
            }
        });

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(getCallingActivity()==null)
                {

                    view.setSelected(true);
                    car_session.create_Location_Session(data.get(i).getCar_Brand(),data.get(i).getCar_Code(),data.get(i).getCar_FUEL(),data.get(i).getCar_Model(),data.get(i).getCar_Name(),data.get(i).getCar_Segment(),data.get(i).getCar_Year());
                    Log.d("car_session",car_session.getUserDetails().get(Car_Struct.Car_Model));
                    onBackPressed();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    public  ArrayList<Garage_Car_Bean> Get_Car (final String car_user)
    {

        final ArrayList<Garage_Car_Bean> temp = new ArrayList<Garage_Car_Bean>();
        final ProgressDialog progressDialog = new ProgressDialog(Garage_Page.this);
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
                                JSONArray jsonArray = jsonObject.getJSONArray(Car_Struct.Table_Name);

                                if(jsonArray.length()>0)
                                {
                                    for(int i =0; i<jsonArray.length();i++)
                                    {   Garage_Car_Bean garage_car_bean = new Garage_Car_Bean();
                                        JSONObject car_details = jsonArray.getJSONObject(i);
                                        garage_car_bean.setCar_Code(car_details.getString(Car_Struct.Car_Code));
                                        garage_car_bean.setCar_Model(car_details.getString(Car_Struct.Car_Model));
                                        garage_car_bean.setCar_Year(car_details.getString(Car_Struct.Car_Year));
                                        garage_car_bean.setCar_Segment(car_details.getString(Car_Struct.Car_Segment));
                                        garage_car_bean.setCar_FUEL(car_details.getString(Car_Struct.Car_FUEL));
                                        garage_car_bean.setCar_Brand(car_details.getString(Car_Struct.Car_Brand));
                                        garage_car_bean.setCar_Name(car_details.getString(Car_Struct.Car_Name));
                                        temp.add(garage_car_bean);

                                    }
                                    listView.setAdapter(new Adapter_Garage_Car(getApplicationContext(),temp));
                                    listView.setSelection(0);



                                }else
                                {
                                    startActivity(new Intent(Garage_Page.this,Add_Car.class));
                                    finish();


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.cancel();

                        }
                        else
                        {

                            progressDialog.cancel();
                            startActivity(new Intent(Garage_Page.this,Add_Car.class));
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        Toast.makeText(Garage_Page.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put(Car_Struct.Car_User,car_user);

                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);
        return temp;
    }


}
