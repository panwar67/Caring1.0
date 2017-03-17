package com.lions.torque.caring.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lions.torque.caring.R;

import java.util.ArrayList;
import java.util.HashMap;

import Structs.Search_Bean;
import Structs.Search_Struct;

/**
 * Created by Panwar on 05/03/17.
 */
public class Search_Adapter extends BaseAdapter implements Filterable{

    Context context;
    ArrayList<Search_Bean> orig = new ArrayList<Search_Bean>();

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
        HashMap<String,String> map = new HashMap<String, String>();
        map.put(Search_Struct.tag,result.get(i).getTAG());
        map.put(Search_Struct.type,result.get(i).getType());
        map.put(Search_Struct.col,result.get(i).getColoumn());
        return map;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Search_Bean> results = new ArrayList<>();
                if (orig == null)
                    orig = result;
                if (charSequence != null) {
                    if (orig != null && orig.size() > 0) {
                        for (int i=0;i<orig.size();i++) {
                            if (orig.get(i).getTAG().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                results.add(orig.get(i));

                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                result = (ArrayList<Search_Bean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder
    {
        TextView tag, type;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root = inflater.inflate(R.layout.search_item,null);
        Holder holder = new Holder();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"SourceSansProLight.otf");
        holder.tag = (TextView)root.findViewById(R.id.search_tag);
        holder.type = (TextView)root.findViewById(R.id.search_type);
        holder.type.setText(result.get(i).getType().toUpperCase());
        holder.tag.setText(result.get(i).getTAG());
        holder.tag.setTypeface(typeface);
        holder.type.setTypeface(typeface);
        return root;
    }
}
