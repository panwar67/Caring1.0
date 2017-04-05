package com.lions.torque.caring.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lions.torque.caring.R;
import com.lions.torque.caring.servicecar.MyBookings;

import java.util.ArrayList;

import Structs.Book_Track;
import Structs.Book_Track_Bean;
import Structs.Vendor_List_Bean;

/**
 * Created by Panwar on 05/04/17.
 */
public class MyBookings_Adapter extends BaseAdapter {

    LayoutInflater layoutInflater = null;
    ArrayList<Book_Track_Bean> result = new ArrayList<Book_Track_Bean>();
    Context context;


    public MyBookings_Adapter( ArrayList<Book_Track_Bean> result, Context context) {
        this.result = result;
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {

        return result.get(i).getId();
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(result.get(i).getId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

     View root = layoutInflater.inflate(R.layout.book_item,null);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"OpenSans.ttf");

        TextView id = (TextView)root.findViewById(R.id.order_id);
        TextView status =(TextView)root.findViewById(R.id.order_status);
        TextView vend_name = (TextView)root.findViewById(R.id.book_vendor_name);
        TextView booking_amount = (TextView)root.findViewById(R.id.booking_amount);

        id.setText("BOOKING ID - "+result.get(i).getId());
        status.setText("STATUS - "+result.get(i).getStatus());
        vend_name.setText(result.get(i).getVend_name()+"");
        booking_amount.setText("Booking amount "+result.get(i).getAdvance());

        id.setTypeface(typeface);
        status.setTypeface(typeface);
        vend_name.setTypeface(typeface);
        booking_amount.setTypeface(typeface);

        return root;
    }
}
