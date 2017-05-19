package com.lions.torque.caring.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    ArrayList<HashMap<String,String>> Checked_item = new ArrayList<HashMap<String, String>>();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View root = layoutInflater.inflate(R.layout.item_service_vendor_profile,null);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"amble.ttf");

        final CheckedTextView service = (CheckedTextView) root.findViewById(R.id.filter_item_textchecked);
        service.setChecked(false);
        service.setTypeface(typeface);
        service.setText(result.get(i).get("SERVE_NAME").toUpperCase());

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                service.setChecked(!service.isChecked());
                if(service.isChecked()) {

                    service.setBackgroundColor(Color.parseColor("#d7ccc8"));
                    Set_Checked(result.get(i));
                }
                else
                {
                    service.setBackgroundColor(Color.WHITE);
                    Decheck_items(result.get(i));
                }
            }
        });


        return root ;
    }

    public void Set_Checked(HashMap<String,String> map)
    {
        Checked_item.add(map);
        //return Checked_item;
    }

    public void Decheck_items(HashMap<String,String> map)
    {
        Checked_item.remove(map);
    }

    public ArrayList<HashMap<String,String>> Get_Checked_Item()
    {
        return Checked_item;
    }

}
