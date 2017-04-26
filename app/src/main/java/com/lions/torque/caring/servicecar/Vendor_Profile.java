package com.lions.torque.caring.servicecar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lions.torque.caring.R;
import com.lions.torque.caring.adapters.Vendor_Serivice_Adapter;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.Location_Session;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import Structs.ExpandableHeightGridView;
import Structs.Vendor_List_Bean;

public class Vendor_Profile extends AppCompatActivity {

    TextView  title,   description, car, segment, service,
            service_head, select_head, selected_service;
    TextView car_name, car_model, car_brand, car_year;
    ImageView back;
    Button checkout;
    Vendor_Serivice_Adapter vendor_serivice_adapter;
    Location_Session location_session;
    String vendor_id;
    String service_id;
    String car_id;
    String service_name;
    Car_Session car_session;
    DBHelper dbHelper;
    LinearLayout linearLayout, change_car;
    Vendor_List_Bean data = new Vendor_List_Bean();
    ArrayList<String> services_list = new ArrayList<String>();
    ArrayList<HashMap<String,String>> services = new ArrayList<HashMap<String, String>>();
    ExpandableHeightGridView expandableHeightGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());
        Intent intent= getIntent();
        vendor_id = intent.getStringExtra("vendor_id");
        service_id = intent.getStringExtra("service_id");
        service_name = intent.getStringExtra("service_name");
        car_id = car_session.getUserDetails().get("CAR_CODE");
        services = dbHelper.Get_Vendor_Services(vendor_id,car_id);
        Log.d("service_size",""+services.size());
        back = (ImageView)findViewById(R.id.profile_back);
        change_car = (LinearLayout)findViewById(R.id.change_car);
        change_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vendor_Profile.this,Garage_Page.class));

            }
        });
        change_car.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        data = dbHelper.Get_Vendor_Profile(vendor_id,location_session.getUserDetails().get("lat"),location_session.getUserDetails().get("long"));

        linearLayout = (LinearLayout)findViewById(R.id.back);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onBackPressed();
                finish();
                return false;
            }
        });
        expandableHeightGridView = (ExpandableHeightGridView)findViewById(R.id.select_multiple_services);
        vendor_serivice_adapter = new Vendor_Serivice_Adapter(getApplicationContext(),services);
        expandableHeightGridView.setAdapter(vendor_serivice_adapter);
        expandableHeightGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        expandableHeightGridView.setExpanded(true);
        expandableHeightGridView.setNumColumns(1);
        expandableHeightGridView.setEmptyView(findViewById(R.id.emptyElement));
        select_head = (TextView)findViewById(R.id.selected_service_vendor);
        title = (TextView)findViewById(R.id.profile_title);
        description = (TextView)findViewById(R.id.profile_description);
        car = (TextView)findViewById(R.id.profile_car);
        //selected_service = (TextView)findViewById(R.id.service_text);
        //selected_service.setText(service_name);
        segment = (TextView)findViewById(R.id.profile_segment);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"OpenSans.ttf");
        title.setTypeface(typeface);
//        description_head.setTypeface(typeface);
        description.setTypeface(typeface);
        car.setTypeface(typeface);
        segment.setTypeface(typeface);
        segment.setText(data.getVend_Segment_Name());
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<services.size();i++)
        {
            stringBuilder.append(services.get(i).get("SERVE_NAME"));
        }
        select_head.setTypeface(typeface);
//        service_head.setTypeface(typeface);
        title.setText(data.getVend_Name().toUpperCase());
        car_name = (TextView)findViewById(R.id.ven_car_name);
        car_brand = (TextView)findViewById(R.id.car_brand);
        car_model = (TextView)findViewById(R.id.ven_car_model);
        car_name.setTypeface(typeface);
        car_model.setTypeface(typeface);
        car_brand.setTypeface(typeface);
        checkout = (Button)findViewById(R.id.profile_checkout);
        checkout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               if(isAnyItemChecked())
               {
                   Bundle bundle = new Bundle();
                   bundle.putString("time_open", String.valueOf(data.getVend_Timings_Open()));
                   bundle.putString("time_close", String.valueOf(data.getVend_Timings_Close()));
                   bundle.putString("booking_amount","0");
                   bundle.putString("book_vend_name",data.getVend_Name());
                   bundle.putString("book_vend_id",data.getVend_id());
                   bundle.putSerializable("serve_list",vendor_serivice_adapter.Get_Checked_Item());
                   Intent intent1 = new Intent(Vendor_Profile.this,Review_Vendor.class);
                   intent1.putExtra("data",bundle);
                   startActivity(intent1);
                   //finish();
               }
                else
               {

               }
                return false;
            }
        });
    }

    public void Update_Car(Car_Session car_session)
    {
        car_name.setText(car_session.getUserDetails().get("CAR_NAME"));
        car_brand.setText(car_session.getUserDetails().get("CAR_BRAND"));
        car_model.setText(car_session.getUserDetails().get("CAR_MODEL"));
    }

    public boolean isAnyItemChecked()
    {
        if(vendor_serivice_adapter.Get_Checked_Item().size()==0)
        {
            Toast.makeText(getApplicationContext(),"Please Select Service",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            Log.d("checked_size","list_method"+vendor_serivice_adapter.Get_Checked_Item().toString()+" ");
            return true;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        car_session = new Car_Session(getApplicationContext());
        Update_Car(car_session);
        car_id = car_session.getUserDetails().get("CAR_CODE");
        services = dbHelper.Get_Vendor_Services(vendor_id,car_id);
        Log.d("service_size",""+services.size());
        vendor_serivice_adapter = new Vendor_Serivice_Adapter(getApplicationContext(),services);
        expandableHeightGridView.setAdapter(vendor_serivice_adapter);
    }



}
