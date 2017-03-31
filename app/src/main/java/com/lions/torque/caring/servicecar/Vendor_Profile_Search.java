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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
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

public class Vendor_Profile_Search extends AppCompatActivity {

    ExpandableHeightGridView expandableHeightGridView;
    String vend_id;
    TextView car_name, car_model, car_brand, car_year;
    Button button;
    RatingBar ratingBar;
    Location_Session location_session;
    Car_Session car_session;
    Spinner spinner;
    LinearLayout linearLayout, change_car;

    Vendor_List_Bean vendor_list_bean = new Vendor_List_Bean();
    ArrayList<HashMap<String,String>> Services = new ArrayList<HashMap<String, String>>();
    DBHelper dbHelper;
    ImageView back;
    String car_id;
    ArrayList<HashMap<String,String>> services = new ArrayList<HashMap<String, String>>();
    Vendor_Serivice_Adapter vendor_serivice_adapter;
    TextView title, timing, description_head, description, car, segment, service, service_head, vend_price, vend_distance;

    ArrayList<String> service_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__profile__search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        vend_id = intent.getStringExtra("id");
        dbHelper = new DBHelper(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());
        back = (ImageView)findViewById(R.id.back_vendor_search);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onBackPressed();
                finish();
                return false;
            }
        });
        button = (Button)findViewById(R.id.profile_checkout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAnyItemChecked();
            }
        });
        change_car = (LinearLayout)findViewById(R.id.change_car);
        linearLayout = (LinearLayout)findViewById(R.id.back);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onBackPressed();
                finish();
                return false;
            }
        });

        change_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vendor_Profile_Search.this,Garage_Page.class));

            }
        });
       Services = dbHelper.Get_Vendor_Services(vend_id,car_session.getUserDetails().get("CAR_CODE"));

        vendor_list_bean = dbHelper.Get_Vendor_Profile(vend_id,location_session.getUserDetails().get("lat"),location_session.getUserDetails().get("long"));
        title = (TextView)findViewById(R.id.profile_title);
        timing = (TextView)findViewById(R.id.profile_timings);
        vend_price = (TextView)findViewById(R.id.vendor_price);
        vend_distance = (TextView)findViewById(R.id.vendor_distance);
        vend_distance.setText(""+new DecimalFormat("##.##").format(vendor_list_bean.getVend_Distance())+" km");
        //vend_distance.setTypeface();
        ratingBar = (RatingBar)findViewById(R.id.profile_rating);
        description_head = (TextView)findViewById(R.id.description_head);
        description = (TextView)findViewById(R.id.profile_description);
        car = (TextView)findViewById(R.id.profile_car);
        segment = (TextView)findViewById(R.id.profile_segment);
        service = (TextView)findViewById(R.id.profile_services);
        service_head = (TextView)findViewById(R.id.profile_services);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"OpenSans.ttf");
        car_name = (TextView)findViewById(R.id.ven_car_name);
        car_brand = (TextView)findViewById(R.id.car_brand);
        car_model = (TextView)findViewById(R.id.ven_car_model);
        car_name.setTypeface(typeface);

        car_model.setTypeface(typeface);
        car_brand.setTypeface(typeface);
        title.setTypeface(typeface);
        timing.setTypeface(typeface);
        description_head.setTypeface(typeface);
        description.setTypeface(typeface);
        car.setTypeface(typeface);
        segment.setTypeface(typeface);
        vend_distance.setTypeface(typeface);
        vend_price.setTypeface(typeface);
        segment.setText(vendor_list_bean.getVend_Segment_Name());

        service.setTypeface(typeface);
        service_head.setTypeface(typeface);
        title.setText(vendor_list_bean.getVend_Name());
        timing.setText(vendor_list_bean.getVend_Timings_Open()+":00 - "+vendor_list_bean.getVend_Timings_Close()+":00");
        ratingBar.setRating(Float.parseFloat(vendor_list_bean.getVend_quanlity()));
        expandableHeightGridView = (ExpandableHeightGridView)findViewById(R.id.select_multiple_services);
        expandableHeightGridView.setExpanded(true);
        expandableHeightGridView.setNumColumns(1);
        vendor_serivice_adapter = new Vendor_Serivice_Adapter(getApplicationContext(),Services);
        expandableHeightGridView.setAdapter(vendor_serivice_adapter);
        expandableHeightGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        expandableHeightGridView.setEmptyView(findViewById(R.id.emptyElement));



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
        }
        else
        {
            Log.d("checked_size","list_method"+vendor_serivice_adapter.Get_Checked_Item().toString()+" ");
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        car_session = new Car_Session(getApplicationContext());
        Update_Car(car_session);
        car_id = car_session.getUserDetails().get("CAR_CODE");
        Services = dbHelper.Get_Vendor_Services(vend_id,car_id);
        Log.d("service_size",""+Services.size());
        expandableHeightGridView.setAdapter(new Vendor_Serivice_Adapter(getApplicationContext(),Services));

    }


}
