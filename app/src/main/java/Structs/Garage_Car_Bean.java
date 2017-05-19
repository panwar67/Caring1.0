package Structs;

import java.io.Serializable;

/**
 * Created by Panwar on 01/03/17.
 */
public class Garage_Car_Bean implements Serializable {

    private String Car_Name;
    private String Car_Model;
    private String Car_Year;
    private String Car_Brand;
    private String Car_FUEL;
    private String Car_Segment;
    private String Car_Code;
    private String Car_User ;
    private String Car_Id ;

    public String getCar_Id() {
        return this.Car_Id;
    }

    public void setCar_Id(String car_Id) {
        this.Car_Id = car_Id;
    }

    public String getCar_Name() {
        return this.Car_Name;
    }

    public void setCar_Name(String car_Name) {
        this.Car_Name = car_Name;
    }

    public String getCar_Model() {
        return this.Car_Model;
    }

    public void setCar_Model(String car_Model) {
        this.Car_Model = car_Model;
    }

    public String getCar_Year() {
        return this.Car_Year;
    }

    public void setCar_Year(String car_Year) {
        this.Car_Year = car_Year;
    }

    public String getCar_Brand() {
        return this.Car_Brand;
    }

    public void setCar_Brand(String car_Brand) {
        this.Car_Brand = car_Brand;
    }

    public String getCar_FUEL() {
        return this.Car_FUEL;
    }

    public void setCar_FUEL(String car_FUEL) {
        this.Car_FUEL = car_FUEL;
    }

    public String getCar_Segment() {
        return this.Car_Segment;
    }

    public void setCar_Segment(String car_Segment) {
        this.Car_Segment = car_Segment;
    }

    public String getCar_Code() {
        return this.Car_Code;
    }

    public void setCar_Code(String car_Code) {
        this.Car_Code = car_Code;
    }

    public String getCar_User() {
        return this.Car_User;
    }

    public void setCar_User(String car_User) {
        this.Car_User = car_User;
    }
}
