package com.lions.torque.caring.servicecar;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lions.torque.caring.R;

import Structs.Garage_Car_Bean;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Garage_Car_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Garage_Car_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Garage_Car_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView car_name, car_model, car_brand, car_year, right;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Garage_Car_Bean garage_car_bean;
    public Garage_Car_Fragment() {
        // Required empty public constructor
    }

    public void Set_Bean(Garage_Car_Bean bean)
    {
        this.garage_car_bean = bean;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Garage_Car_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Garage_Car_Fragment newInstance(Garage_Car_Bean bean) {
        Garage_Car_Fragment fragment = new Garage_Car_Fragment();
        fragment.Set_Bean(bean);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_garage__car_, container, false);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"amble.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getContext().getAssets(),"gothiclit.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getContext().getAssets(),"quicksand.otf");

        int flags =  Paint.SUBPIXEL_TEXT_FLAG
                | Paint.ANTI_ALIAS_FLAG;

        car_name = (TextView)root.findViewById(R.id.car_name_garage);
        car_brand = (TextView)root.findViewById(R.id.car_brand_garage);
        car_model = (TextView)root.findViewById(R.id.car_model_garage);
        car_year = (TextView)root.findViewById(R.id.car_year_garage);
        right = (TextView)root.findViewById(R.id.swipe_right);
        right.setTypeface(typeface2);
        car_name.setTypeface(typeface);
        car_model.setTypeface(typeface1);
        car_brand.setTypeface(typeface1);
        car_year.setTypeface(typeface);
        right.setPaintFlags(flags);
        car_name.setPaintFlags(flags);
        car_model.setPaintFlags(flags);
        car_brand.setPaintFlags(flags);
        car_year.setPaintFlags(flags);
        car_name.setText(garage_car_bean.getCar_Name());
        car_brand.setText(garage_car_bean.getCar_Brand());
        car_model.setText(garage_car_bean.getCar_Model());
        car_year.setText(garage_car_bean.getCar_Year());

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
