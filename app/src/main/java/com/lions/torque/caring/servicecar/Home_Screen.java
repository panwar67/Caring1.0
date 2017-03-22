package com.lions.torque.caring.servicecar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.SearchView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.lions.torque.caring.R;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.Location_Session;
import com.lions.torque.caring.sessions_manager.SessionManager;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Structs.Campaign_Struct;
import Structs.Car_Struct;
import Structs.ExpandableHeightGridView;
import Structs.Vendor_List_Bean;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Screen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlaceSelectionListener {


    TextView home_screen_address, service_label;
    ImageView mechanical, electrical;
   // Location_Session location_session;
    PlaceAutocomplete placeAutocomplete;
    Intent intent;
    SliderLayout sliderLayout;
    SearchView searchView;
    Car_Session car_session;
    Location_Session location_session;
    ExpandableHeightGridView expandableHeightGridView;
    DBHelper dbHelper;
    Typeface typeface;
    TextView car_model;
    TextView name;
    SessionManager sessionManager;
    ArrayList<HashMap<String,String>> Campaign_Data = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(getApplicationContext());
        Campaign_Data = dbHelper.Get_Campaigns();
        car_session = new Car_Session(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"OpenSans.ttf");

        if(car_session.getUserDetails().get(Car_Struct.Car_Code)==null)
        {
            startActivity(new Intent(Home_Screen.this,Garage_Page.class));

        }
        PagerIndicator pagerIndicator = (PagerIndicator)findViewById(R.id.custom_indicator);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        sliderLayout.setCustomIndicator(pagerIndicator);
        sliderLayout.startAutoCycle();

        for(int i=0;i<Campaign_Data.size();i++)
        {

            sliderLayout.addSlider(new DefaultSliderView(getApplicationContext()).image(Campaign_Data.get(i).get(Campaign_Struct.Camp_Url)));

        }
        service_label = (TextView)findViewById(R.id.service_label);
        service_label.setTypeface(typeface);
        mechanical = (ImageView)findViewById(R.id.mechanical_service);
        electrical = (ImageView)findViewById(R.id.electrical_services);
        mechanical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


            }
        });
        electrical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CharSequence[] items = { "Clutch", "Brake", "Gear", "Engine" };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Home_Screen.this)
                        .setTitle("Choose Service")
                        .setSingleChoiceItems( items, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("service","1001");
                                    bundle.putString("service_name","Clutch Repair");

                                    startActivity(new Intent(Home_Screen.this,Display_Vendor_List.class).putExtra("data",bundle));
                                }
                                if (which==1)
                                {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("service","1002");
                                    bundle.putString("service_name","Brake Repair");
                                    startActivity(new Intent(Home_Screen.this,Display_Vendor_List.class).putExtra("data",bundle));

                                }
                                if (which==2)
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("service","1001");
                                    bundle.putString("service_name","Gear Repair");
                                    startActivity(new Intent(Home_Screen.this,Display_Vendor_List.class).putExtra("data",bundle));

                                }
                                if (which==3)
                                {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("service","1001");
                                    bundle.putString("service_name","Engine Repair");
                                    startActivity(new Intent(Home_Screen.this,Display_Vendor_List.class).putExtra("data",bundle));

                                }
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog2 = builder2.create();
                alertdialog2.show();
            }
        });



        searchView = (SearchView)findViewById(R.id.search_view);
        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(Home_Screen.this,Search_Page.class));
                    return false;
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        CircleImageView dp = (CircleImageView) view.findViewById(R.id.drawer_profile_dp);
        name = (TextView)view.findViewById(R.id._profile_name);
        ImageView car = (ImageView)view.findViewById(R.id.drawer_user_car);
         car_model = (TextView)view.findViewById(R.id.drawer_car_name);
        Picasso.with(this)
                .load(sessionManager.getUserDetails().get("dp"))
                .resize(400,400)                        // optional
                .into(dp);
        name.setText(sessionManager.getUserDetails().get("name"));
        car_model.setText(car_session.getUserDetails().get("CAR_MODEL"));
        navigationView.setNavigationItemSelectedListener(this);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setCountry("IN")
                .build();
        try
        {
             intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter)
                            .build(this);
        }
        catch (GooglePlayServicesRepairableException e)
        {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e)
        {
            e.printStackTrace();
        }


        home_screen_address = (TextView)findViewById(R.id.home_screen_address);
        home_screen_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Get_Location_Autocomplete();

                return false;
            }

        });
        location_session = new Location_Session(getApplicationContext());
        home_screen_address.setText(location_session.getUserDetails().get("address"));
        //placeAutocomplete = (PlaceAutocomplete)


    }

    public void Get_Location_Autocomplete()
    {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        name.setText(sessionManager.getUserDetails().get("name"));
        car_model.setText(car_session.getUserDetails().get("CAR_MODEL"));

    }

    public ArrayList<Vendor_List_Bean> Get_Location_Sorted_List(ArrayList<Vendor_List_Bean> data)
    {
        Collections.sort(data, new Comparator<Vendor_List_Bean>()
        {
            @Override
            public int compare(Vendor_List_Bean vendor_list_bean, Vendor_List_Bean t1) {

                Float dist1 = vendor_list_bean.getVend_Distance();
                Float dist2 = t1.getVend_Distance();
                return dist1.compareTo(dist2);
            }
        });

        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {


               /* Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("home_screen_place", "Place: " + place.getName()+" "+place.getAddress());
                LatLng latLng = place.getLatLng();
                location_session.create_Location_Session(place.getAddress().toString(),String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
                home_screen_address.setText(""+place.getAddress());
                */
                Place place = PlacePicker.getPlace(data, this);
                LatLng latLng = place.getLatLng();
                location_session.create_Location_Session(place.getAddress().toString(),String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
                home_screen_address.setText(""+place.getAddress());

                String toastMsg = String.format("Place: %s", place.getName());

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("home_screen", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.home__screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(Home_Screen.this,Profile_Page.class));

        } else if (id == R.id.nav_garage) {

            startActivity(new Intent(Home_Screen.this,Garage_Page.class));

        } else if (id == R.id.nav_bookings) {


        } else if (id == R.id.nav_rate_us) {

        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {

    }
}
