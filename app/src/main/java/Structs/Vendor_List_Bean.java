package Structs;

import android.location.Location;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Panwar on 26/02/17.
 */
public class Vendor_List_Bean implements Serializable
{

    private String Vend_Name ;
    private String Vend_id ;
    private String Vend_Sn;
    private int Vend_Timings_Open ;
    private int Vend_Timings_Close ;
    private String Vend_quanlity ;
    private String Vend_price_low ;
    private String Vend_price_high;
    private String Vend_Lat ;
    private String Vend_long ;
    private String Vend_Segment;
    private String Vend_Segment_Name;
    private String Vend_Assure;
    private float Vend_Distance;
    private String Vend_Serve;
    private String Vend_Description;

    public String getVend_Segment_Name() {
        return this.Vend_Segment_Name;
    }

    public void setVend_Segment_Name(String vend_Segment_Name) {
        this.Vend_Segment_Name = vend_Segment_Name;
    }

    public String getVend_Sn() {
        return this.Vend_Sn;
    }

    public void setVend_Sn(String vend_Sn) {
        this.Vend_Sn = vend_Sn;
    }

    public String getVend_Description() {
        return this.Vend_Description;
    }

    public void setVend_Description(String vend_Description) {
        this.Vend_Description = vend_Description;
    }
    public String getVend_Serve() {
        return this.Vend_Serve;
    }

    public void setVend_Serve(String vend_Serve) {
        this.Vend_Serve = vend_Serve;
    }

    public float getVend_Distance() {
        return this.Vend_Distance;
    }

    public void setVend_Distance(float vend_Distance) {
        this.Vend_Distance = vend_Distance;
    }

    public String getVend_Assure() {
        return this.Vend_Assure;
    }

    public String getVend_Segment() {
        return this.Vend_Segment;
    }

    public void setVend_Assure(String vend_Assure) {
        this.Vend_Assure = vend_Assure;

    }

    public void setVend_Segment(String vend_Segment) {
        this.Vend_Segment = vend_Segment;
    }

    public String getVend_Name() {
        return this.Vend_Name;
    }

    public void setVend_Name(String vend_Name) {
        this.Vend_Name = vend_Name;
    }

    public String getVend_id() {
        return this.Vend_id;
    }

    public void setVend_id(String vend_id) {
        this.Vend_id = vend_id;
    }

    public int getVend_Timings_Open() {
        return this.Vend_Timings_Open;
    }

    public void setVend_Timings_Open(int vend_Timings_Open) {
        this.Vend_Timings_Open = vend_Timings_Open;
    }

    public int getVend_Timings_Close() {
        return this.Vend_Timings_Close;
    }

    public void setVend_Timings_Close(int vend_Timings_Close) {
        this.Vend_Timings_Close = vend_Timings_Close;
    }

    public String getVend_quanlity() {
        return this.Vend_quanlity;
    }

    public void setVend_quanlity(String vend_quanlity) {
        this.Vend_quanlity = vend_quanlity;
    }


    public String getVend_Lat() {
        return this.Vend_Lat;
    }

    public void setVend_Lat(String vend_Lat) {
        this.Vend_Lat = vend_Lat;
    }

    public String getVend_long() {
        return this.Vend_long;
    }

    public void setVend_long(String vend_long) {

        this.Vend_long = vend_long;
    }



    public String getVend_price_low() {
        return this.Vend_price_low;
    }

    public void setVend_price_low(String vend_price_low) {
        this.Vend_price_low = vend_price_low;
    }

    public String getVend_price_high() {
        return this.Vend_price_high;
    }

    public void setVend_price_high(String vend_price_high) {
        this.Vend_price_high = vend_price_high;
    }

}
