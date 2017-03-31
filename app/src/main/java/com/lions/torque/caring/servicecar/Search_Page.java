package com.lions.torque.caring.servicecar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.lions.torque.caring.R;
import com.lions.torque.caring.adapters.Search_Adapter;
import com.lions.torque.caring.dbutils.DBHelper;
import com.lions.torque.caring.sessions_manager.Car_Session;
import com.lions.torque.caring.sessions_manager.Location_Session;

import java.util.ArrayList;
import java.util.HashMap;

import Structs.Car_Struct;
import Structs.Search_Bean;
import Structs.Vendor_List_Bean;

public class Search_Page extends AppCompatActivity {

    ListView search_suggestions;
    SearchView searchView;
    ArrayList<Search_Bean> data = new ArrayList<Search_Bean>();
    ImageView back;
    Location_Session location_session;
    Car_Session car_session;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__page);
        search_suggestions = (ListView)findViewById(R.id.search_suggestions);
        searchView = (SearchView)findViewById(R.id.search_view_page);
        back = (ImageView)findViewById(R.id.search_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        searchView.setSubmitButtonEnabled(false);
        dbHelper = new DBHelper(getApplicationContext());
        car_session = new Car_Session(getApplicationContext());
        location_session = new Location_Session(getApplicationContext());
        data = dbHelper.Get_Search_Data();
        showInputMethod(searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(!s.isEmpty())
                {
                    ArrayList<Search_Bean> map = new ArrayList<Search_Bean>();
                    for (int i=0;i<data.size();i++)
                    {
                        if(data.get(i).getTAG().toLowerCase().contains(s.toLowerCase()))
                        {
                            map.add(data.get(i));
                        }
                    }
                    search_suggestions.setAdapter(new Search_Adapter(getApplicationContext(),map));
                }else
                {
                    ArrayList<Search_Bean> map = new ArrayList<Search_Bean>();
                    search_suggestions.setAdapter(new Search_Adapter(getApplicationContext(),map));
                }
                return true;

            }
        });
        searchView.setSubmitButtonEnabled(false);

        search_suggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                hideSoftKeyboard(Search_Page.this);
                Log.d("clicked",""+search_suggestions.getAdapter().getItem(i));
                HashMap<String,String> map = (HashMap<String, String>) search_suggestions.getAdapter().getItem(i);
                if(map.get("TYPE").equals("SERVICE"))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("service",map.get("COLOUMN"));
                    bundle.putString("service_name",map.get("TAG"));
                    startActivity(new Intent(Search_Page.this,Display_Vendor_List.class).putExtra("data",bundle));
                }
                if (map.get("TYPE").equals("SHOP"))
                {
                    startActivity(new Intent(Search_Page.this,Vendor_Profile_Search.class).putExtra("id",map.get("COLOUMN")));
                }
            }
        });

    }

    private void showInputMethod(SearchView searchView) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        if (imm != null) {
            imm.showSoftInput(searchView, 0);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
