package com.lions.torque.caring.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.lions.torque.caring.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Panwar on 27/03/17.
 */
public class Checkout_Service_Adapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> Checked_item = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> result = new ArrayList<>();
    LayoutInflater layoutInflater = null;
    public Checkout_Service_Adapter(Context cont, ArrayList<HashMap<String,String>> data)
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

        View root = layoutInflater.inflate(R.layout.service_item,null);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"OpenSans.ttf");

        final TextView service = (TextView) root.findViewById(R.id.review_serve_name);
        TextView code = (TextView)root.findViewById(R.id.review_serve_code);
        //service.setChecked(false);
        service.setTypeface(typeface);
        service.setText(result.get(i).get("SERVE_NAME"));
        code.setText(result.get(i).get("SERVE_ID"));
        return root ;
    }





}
