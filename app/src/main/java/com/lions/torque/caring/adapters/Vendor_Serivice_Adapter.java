package com.lions.torque.caring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.lions.torque.caring.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Panwar on 15/03/17.
 */
public class Vendor_Serivice_Adapter extends BaseAdapter implements SpinnerAdapter {

    Context context;
    ArrayList<HashMap<String, String>> result = new ArrayList<>();
    LayoutInflater layoutInflater = null;
    public Vendor_Serivice_Adapter(Context cont, ArrayList<HashMap<String,String>> data)
    {
        context = cont;
        result = data;
        layoutInflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View root = layoutInflater.inflate(R.layout.item_service_vendor_profile,null);

        final TextView service = (TextView)root.findViewById(R.id.service_type);
        service.setText(result.get(i).get("SERVE_NAME"));
        return root ;
    }
}
