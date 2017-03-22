package com.lions.torque.caring.servicecar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

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

    TextView  title, timing, description_head, description, car, segment, service,
            service_head, select_head, selected_service, vend_price, vend_distance;
    TextView car_name, car_model, car_brand, car_year;
    ImageView back;
    Button button;
    RatingBar ratingBar;
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
        expandableHeightGridView.setAdapter(new Vendor_Serivice_Adapter(getApplicationContext(),services));
        select_head = (TextView)findViewById(R.id.selected_service_vendor);
        vend_price = (TextView)findViewById(R.id.vendor_price);
        vend_price.setText(data.getVend_price_low()+" - "+data.getVend_price_high());
        vend_distance = (TextView)findViewById(R.id.vendor_distance);
        vend_distance.setText(""+new DecimalFormat("##.##").format(data.getVend_Distance())+" km");
        title = (TextView)findViewById(R.id.profile_title);
        timing = (TextView)findViewById(R.id.profile_timings);
        ratingBar = (RatingBar)findViewById(R.id.profile_rating);
        description_head = (TextView)findViewById(R.id.description_head);
        description = (TextView)findViewById(R.id.profile_description);
        car = (TextView)findViewById(R.id.profile_car);
        button = (Button)findViewById(R.id.profile_checkout);
        //selected_service = (TextView)findViewById(R.id.service_text);
        //selected_service.setText(service_name);
        segment = (TextView)findViewById(R.id.profile_segment);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"OpenSans.ttf");
        title.setTypeface(typeface);
        timing.setTypeface(typeface);
        description_head.setTypeface(typeface);
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
        title.setText(data.getVend_Name());
        timing.setText(data.getVend_Timings_Open()+":00 - "+data.getVend_Timings_Close()+":00");
        ratingBar.setRating(Float.parseFloat(data.getVend_quanlity()));
        car_name = (TextView)findViewById(R.id.ven_car_name);
        car_brand = (TextView)findViewById(R.id.car_brand);
        car_model = (TextView)findViewById(R.id.ven_car_model);
        car_name.setTypeface(typeface);

        car_model.setTypeface(typeface);
        car_brand.setTypeface(typeface);

    }

    public void Update_Car(Car_Session car_session)
    {
        car_name.setText(car_session.getUserDetails().get("CAR_NAME"));
        car_brand.setText(car_session.getUserDetails().get("CAR_BRAND"));
        car_model.setText(car_session.getUserDetails().get("CAR_MODEL"));
    }



    @Override
    protected void onResume() {
        super.onResume();
        car_session = new Car_Session(getApplicationContext());
        Update_Car(car_session);
        car_id = car_session.getUserDetails().get("CAR_CODE");
        services = dbHelper.Get_Vendor_Services(vendor_id,car_id);
        expandableHeightGridView.setAdapter(new Vendor_Serivice_Adapter(getApplicationContext(),services));

    }
}
