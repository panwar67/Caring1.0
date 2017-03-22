package com.lions.torque.caring.filters;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbSeekbar;
import com.lions.torque.caring.R;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.servicecar.Display_Vendor_List;
import com.lions.torque.caring.servicecar.Garage_Page;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.Location_Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import Structs.Car_Struct;
import Structs.Vendor_List_Bean;

public class Filter_Vendor_List extends AppCompatActivity {

    TextView min_price, max_price, ven_car_name, ven_car_model, ven_car_brand;
    ArrayList<Vendor_List_Bean> filter_data = new ArrayList<Vendor_List_Bean>();
    BubbleThumbSeekbar bubbleThumbSeekbar;
    Button filter;
    ImageView back;
    LinearLayout change_color, linearLayout;
    RadioGroup sorting;
    DBHelper dbHelper;
    String service, car;
    String service_name;
    Location_Session location_session;
    Car_Session car_session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter__vendor__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"SourceSansProLight.otf");
        setSupportActionBar(toolbar);
        location_session = new Location_Session(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());
        bubbleThumbSeekbar = (BubbleThumbSeekbar)findViewById(R.id.price_rangebar);
        min_price = (TextView)findViewById(R.id.lower_range);
        max_price = (TextView)findViewById(R.id.higher_range);
        sorting = (RadioGroup)findViewById(R.id.sorting_group);
        ven_car_brand = (TextView)findViewById(R.id.car_brand);
        ven_car_model = (TextView)findViewById(R.id.ven_car_model);
        ven_car_name = (TextView)findViewById(R.id.ven_car_name);
        linearLayout = (LinearLayout)findViewById(R.id.back);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onBackPressed();
                finish();
                return false;
            }
        });
        ven_car_brand.setTypeface(typeface);
        ven_car_name.setTypeface(typeface);
        ven_car_model.setTypeface(typeface);
        back =(ImageView)findViewById(R.id.back_filter);
        ven_car_model.setText(""+car_session.getUserDetails().get(Car_Struct.Car_Model));
        ven_car_name.setText(""+car_session.getUserDetails().get(Car_Struct.Car_Name));
        ven_car_brand.setText(""+car_session.getUserDetails().get(Car_Struct.Car_Brand));

        dbHelper = new DBHelper(getApplicationContext());
        Intent intent = getIntent();
        service = intent.getStringExtra("service");
        car = intent.getStringExtra("car");
        service_name = intent.getStringExtra("service_name");
        bubbleThumbSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                min_price.setText(""+value.intValue());
            }
        });
        change_color = (LinearLayout)findViewById(R.id.change_car);

        change_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Filter_Vendor_List.this, Garage_Page.class));
                finish();
            }
        });


        filter = (Button)findViewById(R.id.filter_vendor);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = sorting.getCheckedRadioButtonId();
                if(id==R.id.rating_sort_filter)
                {
                    Log.d("reached","sort");
                    Location location = new Location("");
                    location.setLatitude(Double.parseDouble(location_session.getUserDetails().get("lat")));
                    location.setLongitude(Double.parseDouble(location_session.getUserDetails().get("long")));
                    filter_data = dbHelper.Get_Filtered_Vendor_List_Quality(min_price.getText().toString(),car,service,location);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("vendor_list",filter_data);
                    bundle.putString("service",service);
                    bundle.putString("car",car);
                    bundle.putString("service_name",service_name);
                    Intent intent1 = new Intent(Filter_Vendor_List.this, Display_Vendor_List.class);
                    intent1.putExtra("data",bundle);
                    startActivity(intent1);
                    finish();
                }
                if(id==R.id.sort_by_price)
                {
                    Log.d("reached","price");
                    Location location = new Location("");
                    location.setLatitude(Double.parseDouble(location_session.getUserDetails().get("lat")));
                    location.setLongitude(Double.parseDouble(location_session.getUserDetails().get("long")));
                    filter_data =dbHelper.Get_Filtered_Vendor_List_Price(min_price.getText().toString(),car,service,location);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("vendor_list",filter_data);
                    bundle.putString("service",service);
                    bundle.putString("service_name",service_name);
                    Intent intent1 = new Intent(Filter_Vendor_List.this, Display_Vendor_List.class);
                    intent1.putExtra("data",bundle);
                    intent1.putExtra("service",service);
                    intent1.putExtra("car",car);

                    bundle.putString("service_name",service_name);
                    startActivity(intent1);
                    finish();
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(location_session.getUserDetails().get("lat")));
        location.setLongitude(Double.parseDouble(location_session.getUserDetails().get("long")));
        ArrayList<Vendor_List_Bean> data = new ArrayList<Vendor_List_Bean>();
        data = dbHelper.Get_Vendor_Car_Service(car_session.getUserDetails().get(Car_Struct.Car_Code),service,location);
        Bundle bundle = new Bundle();
        bundle.putSerializable("vendor_list",data);
        bundle.putString("service",service);

        startActivity(new Intent(Filter_Vendor_List.this,Display_Vendor_List.class).putExtra("data",bundle));
        finish();

    }
}
