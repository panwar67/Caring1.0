package com.lions.torque.caring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lions.torque.caring.R;

import java.util.ArrayList;
import java.util.HashMap;

import Structs.Search_Bean;

/**
 * Created by Panwar on 05/03/17.
 */
public class Search_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Search_Bean> result = new ArrayList<Search_Bean>();
    LayoutInflater inflater = null;
    public Search_Adapter(Context cont, ArrayList<Search_Bean> data)
    {
        result = data;
        context = cont;
        inflater = (LayoutInflater)context.
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
        View root = inflater.inflate(R.layout.car_list_item,null);

        return null;
    }
}
