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
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class Vendor_Profile_Search extends AppCompatActivity {

    ExpandableHeightGridView expandableHeightGridView;
    String vend_id;
    RatingBar ratingBar;
    Location_Session location_session;
    Car_Session car_session;
    Spinner spinner;
    Vendor_List_Bean vendor_list_bean = new Vendor_List_Bean();
    ArrayList<HashMap<String,String>> Services = new ArrayList<HashMap<String, String>>();
    DBHelper dbHelper;
    ImageView back;
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
     //   Services = dbHelper.Get_Vendor_Services(vend_id);
        for (int i=0;i<Services.size();i++)
        {
            service_list.add(Services.get(i).get("SERVE_NAME"));
        }
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
        expandableHeightGridView.setAdapter(new Vendor_Serivice_Adapter(getApplicationContext(),Services));
        expandableHeightGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //CheckedTextView checkedTextView = (CheckedTextView)view.findViewById(R.id.service_type);
                //checkedTextView.setChecked(true);
                HashMap<String,String> map  = new HashMap<String, String>();


            }
        });


    }

}
