package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PropertyListItem implements Serializable {

    @SerializedName("listing_id")
    private String listingId;

    @SerializedName("property_type")
    private String propertyType;

    @SerializedName("deposit")
    private String deposit;

    @SerializedName("monthly_rent")
    private String monthlyRent;

    @SerializedName("gross_area")
    private String grossArea;

    @SerializedName("net_area")
    private String netArea;

    @SerializedName("floor")
    private String floor;

    @SerializedName("total_floors")
    private String totalFloors;

    @SerializedName("direction")
    private String direction;

    @SerializedName("property_features")
    private String propertyFeatures;

    @SerializedName("image_url")
    private String imageUrl; // 새로운 필드 추가

    // Getter 메서드
    public String getListingId() {
        return listingId;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getDeposit() {
        return deposit;
    }

    public String getMonthlyRent() {
        return monthlyRent;
    }

    public String getGrossArea() {
        return grossArea;
    }

    public String getNetArea() {
        return netArea;
    }

    public String getFloor() {
        return floor;
    }

    public String getTotalFloors() {
        return totalFloors;
    }

    public String getDirection() {
        return direction;
    }

    public String getPropertyFeatures() {
        return propertyFeatures;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter 메서드
    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public void setMonthlyRent(String monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public void setGrossArea(String grossArea) {
        this.grossArea = grossArea;
    }

    public void setNetArea(String netArea) {
        this.netArea = netArea;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setTotalFloors(String totalFloors) {
        this.totalFloors = totalFloors;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setPropertyFeatures(String propertyFeatures) {
        this.propertyFeatures = propertyFeatures;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}