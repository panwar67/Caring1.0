package com.lions.torque.caring.servicecar;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
    TextView title,  description, car, segment, service_head;

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
                if(isAnyItemChecked())
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("time_open", String.valueOf(vendor_list_bean.getVend_Timings_Open()));
                    bundle.putString("time_close", String.valueOf(vendor_list_bean.getVend_Timings_Close()));
                    bundle.putString("booking_amount","0");
                    bundle.putString("book_vend_name",vendor_list_bean.getVend_Name());
                    bundle.putString("book_vend_id",vendor_list_bean.getVend_id());
                    bundle.putSerializable("serve_list",vendor_serivice_adapter.Get_Checked_Item());
                    Intent intent1 = new Intent(Vendor_Profile_Search.this,Review_Vendor.class);
                    intent1.putExtra("data",bundle);
                    startActivity(intent1);
                }
                else
                {

                }
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
        description = (TextView)findViewById(R.id.profile_description);
        car = (TextView)findViewById(R.id.profile_car);
        segment = (TextView)findViewById(R.id.profile_segment);
        service_head = (TextView)findViewById(R.id.selected_service_vendor);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"gothiclit.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getApplicationContext().getAssets(),"amble.ttf");
        button.setTypeface(typeface);
        int flags =  Paint.SUBPIXEL_TEXT_FLAG
                | Paint.ANTI_ALIAS_FLAG;
        button.setPaintFlags(flags);
        car_name = (TextView)findViewById(R.id.ven_car_name);
        car_brand = (TextView)findViewById(R.id.car_brand);
        car_model = (TextView)findViewById(R.id.ven_car_model);
        car_name.setTypeface(typeface);

        car_model.setTypeface(typeface);
        car_brand.setTypeface(typeface);
        title.setTypeface(typeface);
       // timing.setTypeface(typeface);
        description.setTypeface(typeface1);
        car.setTypeface(typeface);
        segment.setTypeface(typeface1);
        segment.setText(vendor_list_bean.getVend_Segment_Name());

        service_head.setTypeface(typeface);
        segment.setPaintFlags(flags);
        title.setPaintFlags(flags);
        description.setPaintFlags(flags);
        car.setPaintFlags(flags);
        segment.setPaintFlags(flags);
        service_head.setPaintFlags(flags);
        car_name.setPaintFlags(flags);
        car_brand.setPaintFlags(flags);
        car_model.setPaintFlags(flags);
        title.setText(Html.fromHtml("<u>"+vendor_list_bean.getVend_Name()+"</u>"));
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
        Services = dbHelper.Get_Vendor_Services(vend_id,car_id);
        Log.d("service_size",""+Services.size());
        vendor_serivice_adapter = new Vendor_Serivice_Adapter(getApplicationContext(),Services);

        expandableHeightGridView.setAdapter(vendor_serivice_adapter);

    }


}
