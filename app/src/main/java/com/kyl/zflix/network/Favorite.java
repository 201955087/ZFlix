package com.kyl.zflix.network;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Favorite {
    private String uid;
    private String type;
    private String listing_id;

    public Favorite() {
        // 기본 생성자 필수
    }

    public Favorite(String uid, String type, String listing_id) {
        this.uid = uid;
        this.type = type;
        this.listing_id = listing_id;
    }

    // Getter와 Setter
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }
}