package com.kyl.zflix.ui;

public class Property {
    private String title;
    private String body1;
    private String body2;
    private int imageResId; // 추후엔 URL로 교체 예정

    public Property(String title, String body1, String body2, int imageResId) {
        this.title = title;
        this.body1 = body1;
        this.body2 = body2;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public String getBody1() { return body1; }
    public String getBody2() { return body2; }
    public int getImageResId() { return imageResId; }
}
