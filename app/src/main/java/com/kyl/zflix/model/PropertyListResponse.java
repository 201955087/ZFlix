package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PropertyListResponse {

    @SerializedName("data")
    private List<PropertyListItem> data;

    public List<PropertyListItem> getData() {
        return data;
    }
}