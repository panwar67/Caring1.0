package com.lions.torque.caring.servicecar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.lions.torque.caring.R;

public class Search_Page extends AppCompatActivity {

    ListView search_suggestions;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__page);
        search_suggestions = (ListView)findViewById(R.id.search_suggestions);
        searchView = (SearchView)findViewById(R.id.search_view_page);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setSubmitButtonEnabled(false);



    }
}
