package com.lions.torque.caring.adapters;

import android.content.Context;
import android.graphics.Paint;
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

        result = data;
    }



    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {

        return result.get(i).getVend_id();

    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    class  Holder
    {
        TextView title, timings, price, distance;
        RatingBar quanlity;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View root = layoutInflater.inflate(R.layout.item_vendor_list,null);

        Holder holder = new Holder();
        Typeface  typeface = Typeface.createFromAsset(context.getAssets(),"amble.ttf");
        Typeface  typeface1 = Typeface.createFromAsset(context.getAssets(),"gothiclit.ttf");

        int flags =  Paint.SUBPIXEL_TEXT_FLAG
                | Paint.ANTI_ALIAS_FLAG;
        // holder.linearLayout = (LinearLayout)root.findViewById(R.id.overlay);
        holder.title = (TextView)root.findViewById(R.id.vendor_name);
        holder.timings = (TextView)root.findViewById(R.id.vendor_timings);
        holder.quanlity = (RatingBar)root.findViewById(R.id.vendor_rating);
        holder.price = (TextView)root.findViewById(R.id.vendor_rating_currency);
        holder.distance = (TextView)root.findViewById(R.id.vendor_distance);
        holder.title.setTypeface(typeface1);
        holder.timings.setTypeface(typeface);
        holder.price.setTypeface(typeface);
        holder.distance.setTypeface(typeface);
        holder.title.setPaintFlags(flags);
        holder.timings.setPaintFlags(flags);
        holder.price.setPaintFlags(flags);
        holder.distance.setPaintFlags(flags);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(hour>result.get(i).getVend_Timings_Close()||hour<result.get(i).getVend_Timings_Open())
        {
           // holder.linearLayout.setVisibility(View.VISIBLE);

        }
        else
        {
           // holder.linearLayout.setVisibility(View.GONE);
        }
        holder.timings.setText(result.get(i).getVend_Timings_Open()+":00 - "+result.get(i).getVend_Timings_Close()+":00");
        holder.title.setText(result.get(i).getVend_Name());
        holder.quanlity.setMax(5);
        holder.quanlity.setRating(Float.parseFloat(result.get(i).getVend_quanlity()));
        float dist = result.get(i).getVend_Distance();
        holder.distance.setText(" "+new DecimalFormat("##.##").format(dist)+" km");
        holder.price.setText(result.get(i).getVend_price_low()+" - "+result.get(i).getVend_price_high());



        return root;
    }

    public void setData(ArrayList<Vendor_List_Bean> data)
    {
        result = data;
        notifyDataSetChanged();

    }

}
