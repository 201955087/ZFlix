package com.kyl.zflix.model;

public class AlarmItem {
    private String title;
    private String time;
    private boolean isRead; // 읽었는지 여부

    public AlarmItem(String title, String time, boolean isRead) {
        this.title = title;
        this.time = time;
        this.isRead = isRead;
    }

    public String getTitle() { return title; }
    public String getTime() { return time; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
