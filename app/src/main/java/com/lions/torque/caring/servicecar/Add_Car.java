package com.lions.torque.caring.servicecar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.sessions_manager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Structs.Car_Struct;

public class Add_Car extends AppCompatActivity {


    Button add_car;
    String DOWN_URL = "http://www.car-ing.com/app/Create_User_Car.php";
    Spinner car_brand, car_car_model, car_year, car_fuel;
    DBHelper dbHelper;
    EditText editText;
    ImageView back;
    SessionManager sessionManager;

    ArrayList<String> car_brand_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = DBHelper.getInstance(getApplicationContext());
        car_brand = (Spinner)findViewById(R.id.brand_spinner);
        car_car_model = (Spinner)findViewById(R.id.name_spinner);
        car_year = (Spinner)findViewById(R.id.year_spinner);
        car_fuel  = (Spinner)findViewById(R.id.fuel_spinner);
        add_car = (Button)findViewById(R.id.add_car);
        editText = (EditText)findViewById(R.id.name_car);
        back = (ImageView)findViewById(R.id.car_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sessionManager = new SessionManager(getApplicationContext());
        car_brand_list = dbHelper.Get_Car_Brand();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, car_brand_list); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_drop_down);
        car_brand.setAdapter(spinnerArrayAdapter);

        car_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),  R.layout.spinner_item, dbHelper.Get_Car_Model(car_brand.getSelectedItem().toString()) ); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_drop_down);
                car_car_model.setAdapter(spinnerArrayAdapter);

                car_car_model.setSelection(0);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        car_car_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),  R.layout.spinner_item, dbHelper.Get_Car_Year(car_car_model.getSelectedItem().toString())); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_drop_down);
                car_year.setAdapter(spinnerArrayAdapter);
                car_year.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        car_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),  R.layout.spinner_item, dbHelper.Get_Car_Fuel(car_car_model.getSelectedItem().toString(),car_year.getSelectedItem().toString())); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_drop_down);
                car_fuel.setAdapter(spinnerArrayAdapter);
                car_fuel.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        car_fuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("car_returned",""+dbHelper.Get_Car_Code(car_car_model.getSelectedItem().toString(),car_brand.getSelectedItem().toString(),car_year.getSelectedItem().toString(),car_fuel.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Save_Car(editText.getText().toString(),car_car_model.getSelectedItem().toString(),car_brand.getSelectedItem().toString(),car_fuel.getSelectedItem().toString(),

                        dbHelper.Get_Car_Segment(car_car_model.getSelectedItem().toString(),
                        car_brand.getSelectedItem().toString(),car_year.getSelectedItem().toString(),
                        dbHelper.Get_Car_Code(car_car_model.getSelectedItem().toString(),car_brand.getSelectedItem().toString(),car_year.getSelectedItem().toString(),car_fuel.getSelectedItem().toString()),car_fuel.getSelectedItem().toString())

                        ,dbHelper.Get_Car_Code(car_car_model.getSelectedItem().toString(),car_brand.getSelectedItem().toString(),car_year.getSelectedItem().toString(),car_fuel.getSelectedItem().toString()),car_year.getSelectedItem().toString(),sessionManager.getUserDetails().get("uid"));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Add_Car.this,Garage_Page.class));
        finish();
    }

    public boolean Save_Car(final String car_name, final String car_model, final String car_brand, final String car_fuel, final String car_segment, final String car_code, final String car_year, final String car_user)
    {

        final ProgressDialog progressDialog = new ProgressDialog(Add_Car.this);
        progressDialog.setMessage("please wait");
        progressDialog.show();
        final ArrayList<HashMap<String,String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s!=null)
                        {
                            progressDialog.cancel();
                            Log.d("response_add_car",s);
                            startActivity(new Intent(Add_Car.this,Garage_Page.class));
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {

                        progressDialog.cancel();


                        Toast.makeText(Add_Car.this, "Error In Connectivity", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(Add_Car.this,Garage_Page.class));
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put(Car_Struct.Car_Name,car_name);
                Keyvalue.put(Car_Struct.Car_Model,car_model);
                Keyvalue.put(Car_Struct.Car_Brand,car_brand);
                Keyvalue.put(Car_Struct.Car_FUEL,car_fuel);
                Keyvalue.put(Car_Struct.Car_Segment,car_segment);
                Keyvalue.put(Car_Struct.Car_Code,car_code);
                Keyvalue.put(Car_Struct.Car_Year,car_year);
                Keyvalue.put(Car_Struct.Car_User,car_user);
                Log.d("add_car_cut",""+Keyvalue.toString());

                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);
        return false;
    }


}
