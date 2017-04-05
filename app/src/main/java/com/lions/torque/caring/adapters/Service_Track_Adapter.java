package com.lions.torque.caring.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.lions.torque.caring.R;

import java.util.ArrayList;
import java.util.HashMap;

import Structs.Search_Bean;
import Structs.Search_Struct;

/**
 * Created by Panwar on 02/04/17.
 */
public class Service_Track_Adapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> result = new ArrayList<>();
    LayoutInflater inflater = null;

    public Service_Track_Adapter(Context cont, ArrayList<HashMap<String, String>> data)
    {
        result = data;
        context = cont;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return result.size();
    }

    @Override
    public Object getItem(int i)
    {

        return result.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    public class Holder
    {
        TextView tag, type;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root = inflater.inflate(R.layout.track_service_item,null);
        Holder holder = new Holder();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"SourceSansProLight.otf");
        holder.tag = (TextView)root.findViewById(R.id.service_name_track);
        holder.type = (TextView)root.findViewById(R.id.service_status_track);
        holder.type.setText(result.get(i).get("BOOK_TRACK"));
        holder.tag.setText(result.get(i).get("BOOK_SERVE_NAME"));
        holder.tag.setTypeface(typeface);
        holder.type.setTypeface(typeface);
        return root;
    }
}

