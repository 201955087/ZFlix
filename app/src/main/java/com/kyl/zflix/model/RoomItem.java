package com.kyl.zflix.model;
public class RoomItem {
    private String title;
    private String detail;
//    private String status;
    private String propertyType;
    private String imageUrl; // <-- 리소스 ID 대신 URL 사용

    public RoomItem(String title, String detail, String status, String imageUrl) {
        this.title = title;
        this.detail = detail;
        this.detail = detail;
//        this.propertyType = propertyType;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getDetail() { return detail; }
//    public String getStatus() { return status; }
    public String getImageUrl() { return imageUrl; }
}
