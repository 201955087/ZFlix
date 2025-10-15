package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class PropertyData implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("price")
    private int price;
    @SerializedName("location")
    private String location;

    // Add all other properties from your API response
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("property_type")
    private String property_type;
    @SerializedName("deposit")
    private String deposit;
    @SerializedName("monthly_rent")
    private String monthly_rent;
    @SerializedName("gross_area")
    private String gross_area;
    @SerializedName("floor")
    private String floor;
    @SerializedName("total_floors")
    private String total_floors;
    @SerializedName("direction")
    private String direction;
    @SerializedName("detail_address")
    private String detail_address;
    @SerializedName("description")
    private String description;
    @SerializedName("parking_available")
    private String parking_available;
    @SerializedName("agent")
    private String agent;

    // Getter and Setter methods for all fields
    public String getImage_url() { return image_url; }
    public String getProperty_type() { return property_type; }
    public String getDeposit() { return deposit; }
    public String getMonthly_rent() { return monthly_rent; }
    public String getGross_area() { return gross_area; }
    public String getFloor() { return floor; }
    public String getTotal_floors() { return total_floors; }
    public String getDirection() { return direction; }
    public String getDetail_address() { return detail_address; }
    public String getDescription() { return description; }
    public String getParking_available() { return parking_available; }
    public String getAgent() { return agent; }
}