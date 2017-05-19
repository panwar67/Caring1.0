package com.lions.torque.caring.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lions.torque.caring.R;

import java.util.ArrayList;

import Structs.Garage_Car_Bean;

/**
 * Created by Panwar on 01/03/17.
 */
public class Adapter_Garage_Car extends BaseAdapter {


    Context context;
    ArrayList<Garage_Car_Bean> result = new ArrayList<Garage_Car_Bean>();
    private static LayoutInflater inflater=null;


    public  Adapter_Garage_Car(Context cont, ArrayList<Garage_Car_Bean> data)
    {
        context = cont;
        result = data;
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


    public class Holder
    {
        TextView car_name, car_model, car_brand, car_year;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View root = inflater.inflate(R.layout.car_list_item,null);

        Holder holder = new Holder();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"amble.ttf");
        Typeface typeface1 = Typeface.createFromAsset(context.getAssets(),"gothiclit.ttf");

        int flags =  Paint.SUBPIXEL_TEXT_FLAG
                | Paint.ANTI_ALIAS_FLAG;
        holder.car_name = (TextView)root.findViewById(R.id.car_name_garage);
        holder.car_brand = (TextView)root.findViewById(R.id.car_brand_garage);
        holder.car_model = (TextView)root.findViewById(R.id.car_model_garage);
        holder.car_year = (TextView)root.findViewById(R.id.car_year_garage);
        holder.car_name.setTypeface(typeface);
        holder.car_model.setTypeface(typeface1);
        holder.car_brand.setTypeface(typeface1);
        holder.car_year.setTypeface(typeface);
        holder.car_name.setPaintFlags(flags);
        holder.car_model.setPaintFlags(flags);
        holder.car_brand.setPaintFlags(flags);
        holder.car_year.setPaintFlags(flags);
        holder.car_name.setText(result.get(i).getCar_Name());
        holder.car_model.setText(result.get(i).getCar_Model());
        holder.car_year.setText(result.get(i).getCar_Year());
        holder.car_brand.setText(result.get(i).getCar_Brand());

        return root;
    }
}
