package com.lions.torque.caring.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lions.torque.caring.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Structs.Ven_List_Struct;
import Structs.Vendor_List_Bean;
import Structs.Vendor_List_Struct;

/**
 * Created by Panwar on 25/02/17.
 */
public class Adapter_Vendor_List extends BaseAdapter {

    Context context;
    ArrayList<Vendor_List_Bean> result = new ArrayList<>();
    LayoutInflater layoutInflater = null;


    public Adapter_Vendor_List(Context cnt, ArrayList<Vendor_List_Bean> data)
    {

        context = cnt;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       // result = data;




        Collections.sort(data, new Comparator<Vendor_List_Bean>() {
            @Override
            public int compare(Vendor_List_Bean vendor_list_bean, Vendor_List_Bean t1) {

                Float dist1 = vendor_list_bean.getVend_Distance();
                Float dist2 = t1.getVend_Distance();
                return dist1.compareTo(dist2);
            }


        });

        result = data;

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


    class  Holder
    {
        TextView title, timings, price, distance;
        RatingBar quanlity;
        LinearLayout linearLayout;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View root = layoutInflater.inflate(R.layout.item_vendor_list,null);

        Holder holder = new Holder();
        Typeface  typeface = Typeface.createFromAsset(context.getAssets(),"OpenSans.ttf");

        holder.linearLayout = (LinearLayout)root.findViewById(R.id.overlay);
        holder.title = (TextView)root.findViewById(R.id.vendor_name);
        holder.timings = (TextView)root.findViewById(R.id.vendor_timings);
        holder.quanlity = (RatingBar)root.findViewById(R.id.vendor_rating);
        holder.price = (TextView)root.findViewById(R.id.vendor_rating_currency);
        holder.distance = (TextView)root.findViewById(R.id.vendor_distance);
        holder.title.setTypeface(typeface);
        holder.timings.setTypeface(typeface);
        holder.price.setTypeface(typeface);
        holder.distance.setTypeface(typeface);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(hour>result.get(i).getVend_Timings_Close()||hour<result.get(i).getVend_Timings_Open())
        {
            holder.linearLayout.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.linearLayout.setVisibility(View.GONE);
        }
        holder.timings.setText(result.get(i).getVend_Timings_Open()+":00 - "+result.get(i).getVend_Timings_Close()+":00");
        holder.title.setText(result.get(i).getVend_Name());
        holder.quanlity.setMax(5);
        holder.quanlity.setRating(Float.parseFloat(result.get(i).getVend_quanlity()));
        float dist = result.get(i).getVend_Distance()/1000;
        holder.distance.setText(" "+new DecimalFormat("##.##").format(dist)+" km");
        holder.price.setText(result.get(i).getVend_price_low()+" - "+result.get(i).getVend_price_high());



        return root;
    }

}