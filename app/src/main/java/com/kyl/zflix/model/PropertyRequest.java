package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PropertyRequest {

    @SerializedName("type")
    private String type;

    @SerializedName("listing_id")
    private String listingId;

    // --- 필터링 관련 필드 추가 ---
    @SerializedName("district")
    private String district;

    @SerializedName("legal_dong")
    private String legalDong;

    @SerializedName("deposit_min")
    private String depositMin;

    @SerializedName("deposit_max")
    private String depositMax;

    @SerializedName("monthly_rent_min")
    private String monthlyRentMin;

    @SerializedName("monthly_rent_max")
    private String monthlyRentMax;

    @SerializedName("interior_facilities")
    private List<String> interiorFacilities;

    // --- 생성자 및 Getter/Setter 추가 ---

    public PropertyRequest(String type) {
        this.type = type;
    }

    public PropertyRequest(String type, String listingId) {
        this.type = type;
        this.listingId = listingId;
    }

    // 모든 필터 조건을 포함하는 생성자 추가
    public PropertyRequest(String type, String district, String legalDong, String depositMin, String depositMax, String monthlyRentMin, String monthlyRentMax, List<String> interiorFacilities) {
        this.type = type;
        this.district = district;
        this.legalDong = legalDong;
        this.depositMin = depositMin;
        this.depositMax = depositMax;
        this.monthlyRentMin = monthlyRentMin;
        this.monthlyRentMax = monthlyRentMax;
        this.interiorFacilities = interiorFacilities;
    }

    // 기존 Getter/Setter 유지
    public String getType() {
        return type;
    }

    public String getListingId() {
        return listingId;
    }

    // 새로 추가된 필드의 Getter/Setter
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLegalDong() {
        return legalDong;
    }

    public void setLegalDong(String legalDong) {
        this.legalDong = legalDong;
    }

    public String getDepositMin() {
        return depositMin;
    }

    public void setDepositMin(String depositMin) {
        this.depositMin = depositMin;
    }

    public String getDepositMax() {
        return depositMax;
    }

    public void setDepositMax(String depositMax) {
        this.depositMax = depositMax;
    }

    public String getMonthlyRentMin() {
        return monthlyRentMin;
    }

    public void setMonthlyRentMin(String monthlyRentMin) {
        this.monthlyRentMin = monthlyRentMin;
    }

    public String getMonthlyRentMax() {
        return monthlyRentMax;
    }

    public void setMonthlyRentMax(String monthlyRentMax) {
        this.monthlyRentMax = monthlyRentMax;
    }

    public List<String> getInteriorFacilities() {
        return interiorFacilities;
    }

    public void setInteriorFacilities(List<String> interiorFacilities) {
        this.interiorFacilities = interiorFacilities;
    }
}