package com.lions.torque.caring.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Structs.Campaign_Struct;
import Structs.Car_Struct;
import Structs.Garage_Car_Bean;
import Structs.Search_Bean;
import Structs.Search_Struct;
import Structs.Ven_List_Struct;
import Structs.Vendor_List_Bean;
import Structs.Vendor_List_Struct;

/**
 * Created by Panwar on 03/02/17.
 */
public class DBHelper extends SQLiteOpenHelper {

        SQLiteDatabase read, write;
    private static DBHelper sInstance;


    public DBHelper(Context context)
    {
        super(context, "CaringApp", null, 6);
        read = this.getReadableDatabase();
        write = this.getWritableDatabase();
    }

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    public static String Car_List = "CAR_LIST";




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + Ven_List_Struct.Table_Name + " " +
                "" + "(" + Ven_List_Struct.Ven_Id + " text, " +
                "" + Ven_List_Struct.Ven_Name + " text, " +
                "" + Ven_List_Struct.Ven_Des + " text, " +
                "" + Ven_List_Struct.Ven_Assure + " text, " +
                "" + Ven_List_Struct.Ven_Price + " float, " +
                "" + Ven_List_Struct.Ven_Quality + " integer, " +
                "" + Ven_List_Struct.Ven_Lat + " text, " +
                "" + Ven_List_Struct.Ven_Long + " text, " +
                "" + Ven_List_Struct.Ven_Url + " text, " +
                "" + Ven_List_Struct.Ven_No_Img + " text, "+Ven_List_Struct.Ven_Serve+" text, "+Ven_List_Struct.Ven_Segment+" text, "+Ven_List_Struct.Ven_price_low+" integer, "+Ven_List_Struct.Ven_price_high+" integer, "+Ven_List_Struct.Ven_Timings_Open+" text, "+Ven_List_Struct.Ven_Timings_Close+" text )");

        sqLiteDatabase.execSQL("CREATE TABLE " + Campaign_Struct.Table_Name + " " +
                "" + "(" + Campaign_Struct.Camp_Id + " text, " + "" +
                " " + Campaign_Struct.Camp_Name + " text, " +
                "" + Campaign_Struct.Camp_Service_Id + " text, " +
                "" + Campaign_Struct.Camp_Url + " text)");

        sqLiteDatabase.execSQL("CREATE TABLE " + Car_List + " (" + Car_Struct.Car_Model + " text, " +
                "" + Car_Struct.Car_Brand + " text, " + Car_Struct.Car_Year + " text, " +
                "" + Car_Struct.Car_FUEL + " text, " + Car_Struct.Car_Segment + " text, " +
                "" + Car_Struct.Car_Code + " text, " + Car_Struct.Car_User + " text)");

        sqLiteDatabase.execSQL("CREATE TABLE " + Car_Struct.Table_Name + " " +
                "( " + Car_Struct.Car_Name + " text, " + Car_Struct.Car_Brand + " text, " +
                "" + Car_Struct.Car_Model + " text, " + Car_Struct.Car_Segment + " text, " +
                "" + Car_Struct.Car_FUEL + " text, " + Car_Struct.Car_Year + " text, " +
                "" + Car_Struct.Car_Code + " text, " + Car_Struct.Car_User + " text)");

        sqLiteDatabase.execSQL("CREATE TABLE "+ Search_Struct.Table_Name+" ( "+Search_Struct.tag+" text, " +
                ""+Search_Struct.col+" text, " +
                ""+Search_Struct.type+" text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        onCreate(sqLiteDatabase);

    }


    public  boolean Init_Car()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+Car_List);
        Log.d("sql car","Deleted");


        return  true;
    }

    public boolean Init_Camp()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+Campaign_Struct.Table_Name);
        Log.d("sql camp","Deleted");

        return true;
    }

    public  boolean Init_Search_Data()
    {
        write.execSQL("DELETE FROM "+Search_Struct.Table_Name);
        Log.d("search","deleted");
        return  true;

    }

    public boolean Init_Vendor()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+Ven_List_Struct.Table_Name);
        Log.d("sql vendor","Deleted");
        return true;
    }

    public boolean Insert_Search_Data(ArrayList<HashMap<String,String>> search_data)
    {
        try {
            write.beginTransaction();
            for (int i = 0; i < search_data.size(); i++)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Search_Struct.tag,search_data.get(i).get(Search_Struct.tag));
                contentValues.put(Search_Struct.col,search_data.get(i).get(Search_Struct.col));
                contentValues.put(Search_Struct.type,search_data.get(i).get(Search_Struct.type));
                long row = write.insertWithOnConflict(Search_Struct.Table_Name,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                Log.d("search_list", "" + row);

            }
            write.setTransactionSuccessful();
            write.endTransaction();

        } catch (SQLiteException e)
        {
            Log.d("error", e.toString());

        }

        return  true;
    }

    public boolean Insert_Campaign_List(String Camp_Id, String Camp_Name, String Camp_Service_Id, String Camp_Url) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Campaign_Struct.Camp_Id, Camp_Id);
        contentValues.put(Campaign_Struct.Camp_Name, Camp_Name);
        contentValues.put(Campaign_Struct.Camp_Service_Id, Camp_Service_Id);
        contentValues.put(Campaign_Struct.Camp_Url, Camp_Url);
        long row = write.insertWithOnConflict(Campaign_Struct.Table_Name,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("camp_insert",""+row);
        return true;
    }


    public boolean Insert_Vendor_List(ArrayList<HashMap<String, String>> data) {

        try {
            write.beginTransaction();
            for (int i = 0; i < data.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Ven_List_Struct.Ven_Id, data.get(i).get(Ven_List_Struct.Ven_Id));
                contentValues.put(Ven_List_Struct.Ven_Name, data.get(i).get(Ven_List_Struct.Ven_Name));
                contentValues.put(Ven_List_Struct.Ven_Des, data.get(i).get(Ven_List_Struct.Ven_Des));
                contentValues.put(Ven_List_Struct.Ven_Assure, data.get(i).get(Ven_List_Struct.Ven_Assure));
                contentValues.put(Ven_List_Struct.Ven_Lat, data.get(i).get(Ven_List_Struct.Ven_Lat));
                contentValues.put(Ven_List_Struct.Ven_Long, data.get(i).get(Ven_List_Struct.Ven_Long));
                contentValues.put(Ven_List_Struct.Ven_No_Img, data.get(i).get(Ven_List_Struct.Ven_No_Img));
                contentValues.put(Ven_List_Struct.Ven_Url, data.get(i).get(Ven_List_Struct.Ven_Url));
                contentValues.put(Ven_List_Struct.Ven_Price, data.get(i).get(Ven_List_Struct.Ven_Price));
                contentValues.put(Ven_List_Struct.Ven_Quality, data.get(i).get(Ven_List_Struct.Ven_Quality));
                contentValues.put(Ven_List_Struct.Ven_Timings_Close,data.get(i).get(Ven_List_Struct.Ven_Timings_Close));
                contentValues.put(Ven_List_Struct.Ven_Timings_Open,data.get(i).get(Ven_List_Struct.Ven_Timings_Open));
                contentValues.put(Ven_List_Struct.Ven_price_high,data.get(i).get(Ven_List_Struct.Ven_price_high));
                contentValues.put(Ven_List_Struct.Ven_price_low,data.get(i).get(Ven_List_Struct.Ven_price_low));
                contentValues.put(Ven_List_Struct.Ven_Serve,data.get(i).get(Ven_List_Struct.Ven_Serve));
                contentValues.put(Ven_List_Struct.Ven_Segment,data.get(i).get(Ven_List_Struct.Ven_Segment));
                long row = write.insertWithOnConflict(Ven_List_Struct.Table_Name, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                Log.d("Vendor_List", "" + row);
            }
            write.setTransactionSuccessful();
            write.endTransaction();


        } catch (SQLiteException e) {
            Log.d("error", e.toString());

        }

        return true;
    }

    public ArrayList<HashMap<String, String>> Get_Campaigns()
    {
        ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
        Cursor res = read.rawQuery("select * from "+Campaign_Struct.Table_Name,null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            HashMap<String,String> map = new HashMap<String, String>();
            map.put(Campaign_Struct.Camp_Url,res.getString(res.getColumnIndex(Campaign_Struct.Camp_Url)));
            map.put(Campaign_Struct.Camp_Name,res.getString(res.getColumnIndex(Campaign_Struct.Camp_Name)));
            data.add(map);
            Log.d("campaign",res.getString(res.getColumnIndex(Campaign_Struct.Camp_Name)));
            res.moveToNext();
        }
        return  data;
    }


    public boolean Insert_All_Car(ArrayList<HashMap<String,String>> data) {
        ContentValues contentValues = new ContentValues();
        try {

            write.beginTransaction();
            for (int i =0;i<data.size();i++)
            {
                contentValues.put(Car_Struct.Car_Model, data.get(i).get(Car_Struct.Car_Model));
                contentValues.put(Car_Struct.Car_Brand, data.get(i).get(Car_Struct.Car_Brand));
                contentValues.put(Car_Struct.Car_Segment, data.get(i).get(Car_Struct.Car_Segment));
                contentValues.put(Car_Struct.Car_Year, data.get(i).get(Car_Struct.Car_Year));
                contentValues.put(Car_Struct.Car_FUEL, data.get(i).get(Car_Struct.Car_FUEL));
                contentValues.put(Car_Struct.Car_Code, data.get(i).get(Car_Struct.Car_Code));
                long row = write.insertWithOnConflict(Car_List, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                Log.d("all_cars_inserted", "" + row);


            }
            write.setTransactionSuccessful();
            write.endTransaction();

        }catch (SQLiteException e)
        {
            Log.d("Error","inserting car");
                }


        return true;
    }



    public boolean Insert_User_Car(String car_name, String car_model, String car_brand, String car_year, String car_segment, String car_fuel, String car_code) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Car_Struct.Car_Name, car_name);
        contentValues.put(Car_Struct.Car_Model, car_model);
        contentValues.put(Car_Struct.Car_Brand, car_brand);
        contentValues.put(Car_Struct.Car_Segment, car_segment);
        contentValues.put(Car_Struct.Car_Year, car_year);
        contentValues.put(Car_Struct.Car_FUEL, car_fuel);
        contentValues.put(Car_Struct.Car_Code, car_code);
        long row = write.insertWithOnConflict(Car_Struct.Table_Name, null, null, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("user_cars_inserted", "" + row);
        write.close();
        return true;
    }





    public ArrayList<Garage_Car_Bean> Get_User_Cars() {
        ArrayList<Garage_Car_Bean> data = new ArrayList<Garage_Car_Bean>();
        Cursor res = read.rawQuery("select * from " + Car_Struct.Table_Name, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Garage_Car_Bean garage_car_bean = new Garage_Car_Bean();
            garage_car_bean.setCar_Name(res.getString(res.getColumnIndex(Car_Struct.Car_Name)));
            garage_car_bean.setCar_Model(res.getString(res.getColumnIndex(Car_Struct.Car_Model)));
            garage_car_bean.setCar_Brand(res.getString(res.getColumnIndex(Car_Struct.Car_Brand)));
            garage_car_bean.setCar_FUEL(res.getString(res.getColumnIndex(Car_Struct.Car_FUEL)));
            garage_car_bean.setCar_Segment(res.getString(res.getColumnIndex(Car_Struct.Car_Segment)));
            garage_car_bean.setCar_Year(res.getString(res.getColumnIndex(Car_Struct.Car_Year)));
            garage_car_bean.setCar_Code(res.getString(res.getColumnIndex(Car_Struct.Car_Code)));
            garage_car_bean.setCar_User(res.getString(res.getColumnIndex(Car_Struct.Car_User)));
            Log.d("car_model", res.getString(res.getColumnIndex(Car_Struct.Car_Model)));
            res.moveToNext();
        }

        write.close();

        return data;
    }

    public ArrayList<String> Get_Car_Brand() {

        ArrayList<String> data = new ArrayList<String>();
        Cursor res = read.rawQuery("select * from " + Car_List + " group by " + Car_Struct.Car_Brand, null);
        Log.d("sql_query","select * from " + Car_List + " group by " + Car_Struct.Car_Brand);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            data.add(res.getString(res.getColumnIndex(Car_Struct.Car_Brand)));
            Log.d("brand_car", "" + res.getString(res.getColumnIndex(Car_Struct.Car_Brand)));
            res.moveToNext();
        }

        return data;
    }

    public ArrayList<String> Get_Car_Model(String car_brand) {
        ArrayList<String> data = new ArrayList<String>();
        Cursor res = read.rawQuery("select * from " + Car_List + " where " + Car_Struct.Car_Brand + " = '" + car_brand + "' group by " + Car_Struct.Car_Model, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            data.add(res.getString(res.getColumnIndex(Car_Struct.Car_Model)));
            Log.d("car_model_returned", res.getString(res.getColumnIndex(Car_Struct.Car_Model)));
            res.moveToNext();
        }
        return data;
    }

    public ArrayList<String> Get_Car_Year(String car_name) {
        ArrayList<String> data = new ArrayList<String>();
        Cursor res = read.rawQuery("select * from " + Car_List + " where " + Car_Struct.Car_Model + " = '" + car_name + "' group by " + Car_Struct.Car_Year, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            data.add(res.getString(res.getColumnIndex(Car_Struct.Car_Year)));
            Log.d("car_year_returned", res.getString(res.getColumnIndex(Car_Struct.Car_Year)));
            res.moveToNext();
        }
        return data;
    }

    public ArrayList<String> Get_Car_Fuel(String car_name, String car_year) {
        ArrayList<String> data = new ArrayList<String>();
        Cursor res = read.rawQuery("select * from " + Car_List + " where " + Car_Struct.Car_Model + " = '" + car_name + "' and " + Car_Struct.Car_Year + " = '" + car_year + "' group by " + Car_Struct.Car_FUEL, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            data.add(res.getString(res.getColumnIndex(Car_Struct.Car_FUEL)));
            Log.d("car_year_returned", res.getString(res.getColumnIndex(Car_Struct.Car_FUEL)));
            res.moveToNext();
        }

        return data;
    }

    public String Get_Car_Code(String car_model, String car_brand, String car_year, String car_fuel)
    {
        String car_code = null;
        Cursor res  = read.rawQuery("select * from "+Car_List+" where "+Car_Struct.Car_FUEL+" = '"+car_fuel+"' and "+Car_Struct.Car_Year+" = '"+car_year+"' and "+Car_Struct.Car_Model+" = '"+car_model+"' and "+Car_Struct.Car_Brand+" = '"+car_brand+"'", null);
        res.moveToFirst();
        while (res.isFirst())
        {
            car_code = res.getString(res.getColumnIndex(Car_Struct.Car_Code));
            Log.d("car_code_returned",""+res.getString(res.getColumnIndex(Car_Struct.Car_Code)));
            res.moveToNext();
        }

        return  car_code;
    }

    public String Get_Car_Segment(String car_model, String car_brand, String car_year, String car_code, String car_fuel)
    {
        String Car_Segment = "";
        Cursor res = read.rawQuery("select * from "+Car_Struct.Table_Name+" where "+Car_Struct.Car_Model+" = '"+car_model+"' and "+Car_Struct.Car_Brand+" = '"+car_brand+"' and "+Car_Struct.Car_Year+" = '"+car_year+"' and "+Car_Struct.Car_Code+" = '"+car_code+"' and "+Car_Struct.Car_FUEL+" = '"+car_fuel+"' ",null);
        res.moveToFirst();
        while (res.isFirst())
        {
            Car_Segment = res.getString(res.getColumnIndex(Car_Struct.Car_Segment));
            Log.d("car_segment",Car_Segment);
            res.moveToNext();
        }

        return Car_Segment;
    }

    public ArrayList<Vendor_List_Bean> Get_Filtered_Vendor_List_Quality( String price, String segment, String service, Location location)
    {
        ArrayList<Vendor_List_Bean> data = new ArrayList<Vendor_List_Bean>();
        Cursor res = read.rawQuery("select * from "+ Ven_List_Struct.Table_Name+" where "+Ven_List_Struct.Ven_price_low+" < "+price+"  and "+Ven_List_Struct.Ven_Segment+" = '"+segment+"' and "+Ven_List_Struct.Ven_Serve+" = '"+service+"' ",null);
        Log.d("vendor_filter","select * from "+ Ven_List_Struct.Table_Name+" where "+Ven_List_Struct.Ven_price_low+" < "+price+"  and "+Ven_List_Struct.Ven_Segment+" = '"+segment+"' and "+Ven_List_Struct.Ven_Serve+" = '"+service+"' ");
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Vendor_List_Bean vendor_list_bean = new Vendor_List_Bean();
            vendor_list_bean.setVend_id(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Id)));
            vendor_list_bean.setVend_Name(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Name)));
            vendor_list_bean.setVend_quanlity(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Quality)));
            vendor_list_bean.setVend_price_low(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_price_low)));
            vendor_list_bean.setVend_price_high(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_price_high)));
            vendor_list_bean.setVend_Lat(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Lat)));
            vendor_list_bean.setVend_long(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Long)));
            vendor_list_bean.setVend_Segment(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Segment)));
            vendor_list_bean.setVend_Assure(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Assure)));
            vendor_list_bean.setVend_Timings_Open(Integer.parseInt(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Timings_Open))));
            vendor_list_bean.setVend_Timings_Close(Integer.parseInt(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Timings_Close))));
            Location temp = new Location("");
            location.setLatitude(Double.parseDouble(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Lat))));
            location.setLongitude(Double.parseDouble(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Long))));
            vendor_list_bean.setVend_Distance(location.distanceTo(temp));
            Log.d("vendor_distance",""+location.distanceTo(temp));

            data.add(vendor_list_bean);
            res.moveToNext();

        }

        Collections.reverse(data);

        return data;
    }

    public ArrayList<Vendor_List_Bean> Get_Filtered_Vendor_List_Price( String price, String segment, String service, Location location)
    {
        ArrayList<Vendor_List_Bean> data = new ArrayList<Vendor_List_Bean>();
        Cursor res = read.rawQuery("select * from "+ Ven_List_Struct.Table_Name+" where "+Ven_List_Struct.Ven_price_low+" < "+price+" and "+Ven_List_Struct.Ven_Segment+" = '"+segment+"' and "+Ven_List_Struct.Ven_Serve+" = '"+service+"' order by "+Ven_List_Struct.Ven_price_low+" asc",null);
        Log.d("query_price","select * from "+ Ven_List_Struct.Table_Name+" where "+Ven_List_Struct.Ven_price_low+" < "+price+" and "+Ven_List_Struct.Ven_Segment+" = '"+segment+"' and "+Ven_List_Struct.Ven_Serve+" = '"+service+"' order by "+Ven_List_Struct.Ven_price_low+" asc");
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Vendor_List_Bean vendor_list_bean = new Vendor_List_Bean();
            vendor_list_bean.setVend_id(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Id)));
            vendor_list_bean.setVend_Name(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Name)));
            vendor_list_bean.setVend_quanlity(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Quality)));
            vendor_list_bean.setVend_price_low(String.valueOf(res.getInt(res.getColumnIndex(Ven_List_Struct.Ven_price_low))));
            vendor_list_bean.setVend_price_high(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_price_high)));
            vendor_list_bean.setVend_Lat(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Lat)));
            vendor_list_bean.setVend_long(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Long)));
            vendor_list_bean.setVend_Segment(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Segment)));
            vendor_list_bean.setVend_Assure(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Assure)));
            vendor_list_bean.setVend_Timings_Open(Integer.parseInt(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Timings_Open))));
            vendor_list_bean.setVend_Timings_Close(Integer.parseInt(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Timings_Close))));
            vendor_list_bean.setVend_Serve(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Serve)));
            Location temp = new Location("");
            location.setLatitude(Double.parseDouble(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Lat))));
            location.setLongitude(Double.parseDouble(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Long))));
            vendor_list_bean.setVend_Distance(location.distanceTo(temp));


            data.add(vendor_list_bean);
            res.moveToNext();

        }

        return data;
    }

    public ArrayList<Vendor_List_Bean> Get_Vendor_Car_Service(String segment, String service, Location location)
    {
        ArrayList<Vendor_List_Bean> data = new ArrayList<Vendor_List_Bean>();
        Cursor res = read.rawQuery("select * from "+ Ven_List_Struct.Table_Name+" where "+Ven_List_Struct.Ven_Segment+" = '"+segment+"' and "+Ven_List_Struct.Ven_Serve+" = '"+service+"'",null);

        res.moveToFirst();
        Log.d("query_serv_car","select * from "+ Ven_List_Struct.Table_Name+" where "+Ven_List_Struct.Ven_Segment+" = '"+segment+"' and "+Ven_List_Struct.Ven_Serve+" = '"+service+"'");
        while (!res.isAfterLast())
        {
            Vendor_List_Bean vendor_list_bean = new Vendor_List_Bean();
            Location temp = new Location("");
            vendor_list_bean.setVend_id(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Id)));
            vendor_list_bean.setVend_Name(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Name)));
            vendor_list_bean.setVend_quanlity(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Quality)));
            vendor_list_bean.setVend_price_low(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_price_low)));
            vendor_list_bean.setVend_price_high(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_price_high)));
            vendor_list_bean.setVend_Lat(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Lat)));
            vendor_list_bean.setVend_long(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Long)));
            vendor_list_bean.setVend_Segment(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Segment)));
            vendor_list_bean.setVend_Assure(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Assure)));
            vendor_list_bean.setVend_Timings_Open(Integer.parseInt(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Timings_Open))));
            vendor_list_bean.setVend_Timings_Close(Integer.parseInt(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Timings_Close))));
            vendor_list_bean.setVend_Serve(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Serve)));
            location.setLatitude(Double.parseDouble(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Lat))));
            Log.d("latutide",""+res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Lat)));
            location.setLongitude(Double.parseDouble(res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Long))));
            vendor_list_bean.setVend_Distance(location.distanceTo(temp));
            Log.d("inside_service_car",res.getString(res.getColumnIndex(Ven_List_Struct.Ven_Serve))+" "+location.distanceTo(temp));
            data.add(vendor_list_bean);
            res.moveToNext();

        }

        return data;
    }

    public ArrayList<Search_Bean> Get_Search_Data()
    {
        ArrayList<Search_Bean> data = new ArrayList<Search_Bean>();
       Cursor res = read.rawQuery("select * from "+Search_Struct.Table_Name,null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Search_Bean search_bean = new Search_Bean();
            search_bean.setTAG(res.getString(res.getColumnIndex(Search_Struct.tag)));
            search_bean.setColoumn(res.getString(res.getColumnIndex(Search_Struct.col)));
            search_bean.setType(res.getString(res.getColumnIndex(Search_Struct.type)));
            data.add(search_bean);
            res.moveToNext();
        }

        return data;
    }
}
