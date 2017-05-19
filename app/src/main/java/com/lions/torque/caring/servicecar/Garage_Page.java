package com.lions.torque.caring.servicecar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.google.android.gms.vision.text.Line;
import com.lions.torque.caring.R;
import com.lions.torque.caring.adapters.Adapter_Garage_Car;
import com.lions.torque.caring.adapters.Garage_Slider_Adapter;
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
import Structs.ZoomOutPageTransformer;

public class Garage_Page extends AppCompatActivity implements Garage_Car_Fragment.OnFragmentInteractionListener {

    DBHelper dbHelper;
    Garage_Slider_Adapter garage_slider_adapter ;
    ListView listView;
    String DOWN_URL = "http://www.car-ing.com/app/Get_Vendor_Car.php";
    String DOWN_URL2 = "http://www.car-ing.com/app/DeleteCar.php";
    SessionManager sessionManager;
    Car_Session car_session;
    ImageView back ;
    Button select;
    LinearLayout garage_back;
    TextView textView, title;
    ArrayList<Garage_Car_Bean> data = new ArrayList<Garage_Car_Bean>();
    ViewPager viewPager;
    com.github.clans.fab.FloatingActionButton add_float, delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage__page);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager)findViewById(R.id.pager);
        dbHelper = DBHelper.getInstance(getApplicationContext());
        select = (Button)findViewById(R.id.select_car);
        add_float = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabadd);
        delete = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabdel);
        add_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Garage_Page.this,Add_Car.class));
                finish();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(garage_slider_adapter.Get_Car_Id(viewPager.getCurrentItem())!=null)
                {
                    garage_slider_adapter.Get_Selected_Bean(viewPager.getCurrentItem());
                    Garage_Car_Bean temp_garage_bean = new Garage_Car_Bean();
                    temp_garage_bean = (Garage_Car_Bean) garage_slider_adapter.Get_Selected_Bean(viewPager.getCurrentItem());
                    Delete_Car(temp_garage_bean.getCar_Id());
                    onBackPressed();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You don't have any car",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //listView = (ListView)findViewById(R.id.garage_car_list);
        sessionManager = new SessionManager(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());
        garage_back = (LinearLayout) findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);

        garage_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //textView  = (TextView) findViewById(R.id.garage_label);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"gothiclit.ttf");
        //textView.setTypeface(typeface);
        title.setTypeface(typeface);
        int flags =  Paint.SUBPIXEL_TEXT_FLAG
                | Paint.ANTI_ALIAS_FLAG;
        title.setPaintFlags(flags);
        //textView.setPaintFlags(flags);



        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               garage_slider_adapter.get_Car_Code(viewPager.getCurrentItem());
                Log.d("selected_car",garage_slider_adapter.get_Car_Code(viewPager.getCurrentItem()));

            }
        });
        select.setTypeface(typeface);
        select.setPaintFlags(flags);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                garage_slider_adapter.get_Car_Code(viewPager.getCurrentItem());
                garage_slider_adapter.Get_Selected_Bean(viewPager.getCurrentItem());
                Garage_Car_Bean temp_garage_bean = new Garage_Car_Bean();
                temp_garage_bean = (Garage_Car_Bean) garage_slider_adapter.Get_Selected_Bean(viewPager.getCurrentItem());
                car_session.create_Location_Session(temp_garage_bean.getCar_Brand()+"",temp_garage_bean.getCar_Code()+"",temp_garage_bean.getCar_FUEL()+"",temp_garage_bean.getCar_Model()+"",temp_garage_bean.getCar_Name()+"",temp_garage_bean.getCar_Segment()+"",temp_garage_bean.getCar_Year()+"");
                onBackPressed();

            }
        });

         data =  Get_Car(sessionManager.getUserDetails().get(SessionManager.KEY_UID));

      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });  */


        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

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

    @Override
    protected void onResume() {
        super.onResume();
        Get_Car(sessionManager.getUserDetails().get(SessionManager.KEY_UID));
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
                                        garage_car_bean.setCar_Id(car_details.getString(Car_Struct.Car_Id));
                                        garage_car_bean.setCar_Code(car_details.getString(Car_Struct.Car_Code));
                                        garage_car_bean.setCar_Model(car_details.getString(Car_Struct.Car_Model));
                                        garage_car_bean.setCar_Year(car_details.getString(Car_Struct.Car_Year));
                                        garage_car_bean.setCar_Segment(car_details.getString(Car_Struct.Car_Segment));
                                        garage_car_bean.setCar_FUEL(car_details.getString(Car_Struct.Car_FUEL));
                                        garage_car_bean.setCar_Brand(car_details.getString(Car_Struct.Car_Brand));
                                        garage_car_bean.setCar_Name(car_details.getString(Car_Struct.Car_Name));
                                        temp.add(garage_car_bean);

                                    }
                                    garage_slider_adapter = new Garage_Slider_Adapter(getSupportFragmentManager(),temp);
                                    viewPager.setAdapter(garage_slider_adapter);

                                   // listView.setAdapter(new Adapter_Garage_Car(getApplicationContext(),temp));
                                    //listView.setSelection(0);



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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void Delete_Car(final String car_id)
    {

        final ProgressDialog progressDialog = new ProgressDialog(Garage_Page.this);
        progressDialog.setMessage("Deleting Car....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.cancel();
                        if (s!=null)
                        {
                            Log.d("response_deleted",s);
                            Toast.makeText(getApplicationContext(),"Car deleted",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {


                            Toast.makeText(getApplicationContext(),"Car not deleted",Toast.LENGTH_SHORT).show();
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
                Keyvalue.put("car_id",car_id);
                Log.d("reject_final_cut",Keyvalue.toString()+"");
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
