package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PropertyRequest {

    @SerializedName("type")
    private String type;

    @SerializedName("listing_id")
    private String listingId;

    // --- 필터링 관련 필드 ---
    @SerializedName("district")
    private String district;

    @SerializedName("legal_dong")
    private String legalDong;

    @SerializedName("deposit_min")
    private String depositMin;

    @SerializedName("deposit_max")
    private String depositMax;

    @SerializedName("monthly_min")
    private Integer monthlyRentMin;

    @SerializedName("monthly_max")
    private Integer monthlyRentMax;

    @SerializedName("net_min")
    private String netAreaMin;

    @SerializedName("net_max")
    private String netAreaMax;

    @SerializedName("interior_facilities_list")
    private List<String> interiorFacilities;

    // 사용승인일 필드
    @SerializedName("approval_age_group")
    private String approvalAgeGroup;

    // --- 생성자 ---
    public PropertyRequest(String type) {
        this.type = type;
    }

    public PropertyRequest(String type, String listingId) {
        this.type = type;
        this.listingId = listingId;
    }

    public PropertyRequest(String type, String district, String legalDong,
                           String depositMin, String depositMax, Integer monthlyRentMin,
                           Integer monthlyRentMax, String netAreaMin, String netAreaMax,
                           String approvalDateLimitYears, List<String> interiorFacilities) {
        this.type = type;
        this.district = district;
        this.legalDong = legalDong;
        this.depositMin = depositMin;
        this.depositMax = depositMax;
        this.monthlyRentMin = monthlyRentMin;
        this.monthlyRentMax = monthlyRentMax;
        this.netAreaMin = netAreaMin;
        this.netAreaMax = netAreaMax;
        this.approvalAgeGroup = approvalAgeGroup;
        this.interiorFacilities = interiorFacilities;
    }

    // --- Getter / Setter ---
    public String getType() { return type; }
    public String getListingId() { return listingId; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getLegalDong() { return legalDong; }
    public void setLegalDong(String legalDong) { this.legalDong = legalDong; }

    public String getDepositMin() { return depositMin; }
    public void setDepositMin(String depositMin) { this.depositMin = depositMin; }

    public String getDepositMax() { return depositMax; }
    public void setDepositMax(String depositMax) { this.depositMax = depositMax; }

    public Integer getMonthlyRentMin() { return monthlyRentMin; }
    public void setMonthlyRentMin(Integer monthlyRentMin) { this.monthlyRentMin = monthlyRentMin; }

    public Integer getMonthlyRentMax() { return monthlyRentMax; }
    public void setMonthlyRentMax(Integer monthlyRentMax) { this.monthlyRentMax = monthlyRentMax; }

    public List<String> getInteriorFacilities() { return interiorFacilities; }
    public void setInteriorFacilities(List<String> interiorFacilities) { this.interiorFacilities = interiorFacilities; }

    public String getNetAreaMin() { return netAreaMin; }
    public void setNetAreaMin(String netAreaMin) { this.netAreaMin = netAreaMin; }

    public String getNetAreaMax() { return netAreaMax; }
    public void setNetAreaMax(String netAreaMax) { this.netAreaMax = netAreaMax; }

    public String getApprovalAgeGroup() { return approvalAgeGroup; }
    public void setApprovalAgeGroup(String approvalAgeGroup) { this.approvalAgeGroup = approvalAgeGroup; }
}

