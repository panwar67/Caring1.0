package com.lions.torque.caring.servicecar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lions.torque.caring.R;
import com.lions.torque.caring.dbutils.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

import Structs.Vendor_List_Bean;

public class Vendor_Profile extends AppCompatActivity {

    TextView  title, timing, description_head, description, car, segment, service, service_head;
    ImageView back;
    RatingBar ratingBar;
    String vendor_id;
    DBHelper dbHelper;
    Vendor_List_Bean data = new Vendor_List_Bean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(getApplicationContext());
        Intent intent= getIntent();
        vendor_id = intent.getStringExtra("vendor_id");

        ArrayList<HashMap<String,String>> services = dbHelper.Get_Vendor_Services(vendor_id);
        back = (ImageView)findViewById(R.id.profile_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        data = dbHelper.Get_Vendor_Profile(vendor_id);
        title = (TextView)findViewById(R.id.profile_title);
        timing = (TextView)findViewById(R.id.profile_timings);
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

        for(int i=0;i<services.size();i++)
        {
            service.setText(services.get(i).get("SERVE_NAME")+", ");
        }
        service.setText(""+services.toString()+"");
        service.setTypeface(typeface);
        service_head.setTypeface(typeface);
        title.setText(data.getVend_Name());
        timing.setText(data.getVend_Timings_Open()+":00 - "+data.getVend_Timings_Close()+":00");
        ratingBar.setRating(Float.parseFloat(data.getVend_quanlity()));






    }

}
