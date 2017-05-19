package com.lions.torque.caring.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.lions.torque.caring.servicecar.Garage_Car_Fragment;

import java.util.ArrayList;

import Structs.Garage_Car_Bean;

/**
 * Created by Panwar on 04/05/17.
 */
public class Garage_Slider_Adapter extends FragmentStatePagerAdapter {

    ArrayList<Garage_Car_Bean> result = new ArrayList<Garage_Car_Bean>();


    public Garage_Slider_Adapter(FragmentManager fm, ArrayList<Garage_Car_Bean> data) {
        super(fm);

        result = data;
    }

    @Override
    public Fragment getItem(int position) {
        return new Garage_Car_Fragment().newInstance(result.get(position));
    }

    @Override
    public int getCount() {
        return result.size();
    }

    public String get_Car_Code(int position)
    {

        return result.get(position).getCar_Code();

    }

    public Garage_Car_Bean Get_Selected_Bean(int position)
    {


        return   result.get(position);


    }

    public String Get_Car_Id(int position)
    {

        String id = result.get(position).getCar_Id();
              //  result.remove(position);
        return id;
    }
}
