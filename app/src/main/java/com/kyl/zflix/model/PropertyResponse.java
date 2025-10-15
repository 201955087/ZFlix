package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PropertyResponse {

    @SerializedName("data")
    private List<PropertyItem> data;

    public List<PropertyItem> getData() {
        return data;
    }

    public void setData(List<PropertyItem> data) {
        this.data = data;
    }
}
