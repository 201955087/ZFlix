package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PropertyItem implements Serializable {

    @SerializedName("listing_id")
    private String listingId;

    @SerializedName("city")
    private String city;

    @SerializedName("district")
    private String district;

    @SerializedName("legal_dong")
    private String legalDong;

    @SerializedName("detail_address")
    private String detailAddress;

    @SerializedName("property_type")
    private String propertyType;

    @SerializedName("deposit")
    private String deposit;

    @SerializedName("monthly_rent")
    private String monthlyRent;

    @SerializedName("map_image_url")
    private String mapImageUrl;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("gross_area")
    private String grossArea;
    @SerializedName("contract_area")
    private String contract_area;

    @SerializedName("net_area")
    private String netArea;

    @SerializedName("property_features")
    private String propertyFeatures;

    @SerializedName("maintenance_fee")
    private String maintenanceFee;

    @SerializedName("available_move_in_date")
    private String availableMoveInDate;

    @SerializedName("direction")
    private String direction;

    @SerializedName("description")
    private String description;

    @SerializedName("agent")
    private String agent;

    @SerializedName("interior_facilities")
    private String interiorFacilities;

    @SerializedName("brokerage_fee")
    private String brokerageFee;

    @SerializedName("floor")
    private String floor;

    @SerializedName("total_floors")
    private String totalFloors;

    @SerializedName("num_rooms")
    private String numRooms;

    @SerializedName("num_bathrooms")
    private String numBathrooms;

    @SerializedName("loan_amount")
    private String loanAmount;

    @SerializedName("is_duplex")
    private String isDuplex;

    @SerializedName("illegal_building")
    private String illegalBuilding;

    @SerializedName("total_parking_spaces")
    private String totalParkingSpaces;

    @SerializedName("parking_available")
    private String parkingAvailable;

    @SerializedName("approval_age_group")
    private String approvalAgeGroup;

    // --- Getter & Setter ---
    public String getListingId() { return listingId; }
    public void setListingId(String listingId) { this.listingId = listingId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getLegalDong() { return legalDong; }
    public void setLegalDong(String legalDong) { this.legalDong = legalDong; }

    public String getDetailAddress() { return detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public String getDeposit() { return deposit; }
    public void setDeposit(String deposit) { this.deposit = deposit; }

    public String getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(String monthlyRent) { this.monthlyRent = monthlyRent; }

    public String getMapImageUrl() { return mapImageUrl; }
    public void setMapImageUrl(String mapImageUrl) { this.mapImageUrl = mapImageUrl; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getGrossArea() { return grossArea; }
    public void setGrossArea(String grossArea) { this.grossArea = grossArea; }

    public String getContract_area() { return contract_area; }
    public void setContract_area(String contract_area) { this.contract_area = contract_area; }

    public String getNetArea() { return netArea; }
    public void setNetArea(String netArea) { this.netArea = netArea; }

    public String getPropertyFeatures() { return propertyFeatures; }
    public void setPropertyFeatures(String propertyFeatures) { this.propertyFeatures = propertyFeatures; }

    public String getMaintenanceFee() { return maintenanceFee; }
    public void setMaintenanceFee(String maintenanceFee) { this.maintenanceFee = maintenanceFee; }

    public String getAvailableMoveInDate() { return availableMoveInDate; }
    public void setAvailableMoveInDate(String availableMoveInDate) { this.availableMoveInDate = availableMoveInDate; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAgent() { return agent; }
    public void setAgent(String agent) { this.agent = agent; }

    public String getInteriorFacilities() { return interiorFacilities; }
    public void setInteriorFacilities(String interiorFacilities) { this.interiorFacilities = interiorFacilities; }

    public String getBrokerageFee() { return brokerageFee; }
    public void setBrokerageFee(String brokerageFee) { this.brokerageFee = brokerageFee; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public String getTotalFloors() { return totalFloors; }
    public void setTotalFloors(String totalFloors) { this.totalFloors = totalFloors; }

    public String getNumRooms() { return numRooms; }
    public void setNumRooms(String numRooms) { this.numRooms = numRooms; }

    public String getNumBathrooms() { return numBathrooms; }
    public void setNumBathrooms(String numBathrooms) { this.numBathrooms = numBathrooms; }

    public String getLoanAmount() { return loanAmount; }
    public void setLoanAmount(String loanAmount) { this.loanAmount = loanAmount; }

    public String getIsDuplex() { return isDuplex; }
    public void setIsDuplex(String isDuplex) { this.isDuplex = isDuplex; }

    public String getIllegalBuilding() { return illegalBuilding; }
    public void setIllegalBuilding(String illegalBuilding) { this.illegalBuilding = illegalBuilding; }

    public String getTotalParkingSpaces() { return totalParkingSpaces; }
    public void setTotalParkingSpaces(String totalParkingSpaces) { this.totalParkingSpaces = totalParkingSpaces; }

    public String getParkingAvailable() { return parkingAvailable; }
    public void setParkingAvailable(String parkingAvailable) { this.parkingAvailable = parkingAvailable; }

    public String getApprovalAgeGroup() { return approvalAgeGroup; }
    public void setApprovalAgeGroup(String approvalAgeGroup) { this.approvalAgeGroup = approvalAgeGroup; }

}
