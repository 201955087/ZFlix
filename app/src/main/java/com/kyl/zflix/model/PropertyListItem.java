package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PropertyListItem implements Serializable {
    private String summary;
    @SerializedName("listing_id")
    private String listingId;

    @SerializedName("city")
    private String city;

    @SerializedName("district")
    private String district;

    @SerializedName("legal_dong")
    private String legal_dong;
    @SerializedName("detail_address")
    private String detail_address;
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
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getLegal_dong() { return legal_dong; }
    public void setLegal_dong(String legal_dong) { this.legal_dong = legal_dong; }

    public String getDetail_address() { return detail_address; }
    public void setDetail_address(String detail_address) { this.detail_address = detail_address; }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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