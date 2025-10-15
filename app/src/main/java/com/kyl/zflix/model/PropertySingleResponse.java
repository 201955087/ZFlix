package com.kyl.zflix.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PropertySingleResponse {

    @SerializedName("data")
    private List<PropertyItem> data;  // 단일 응답도 배열로 감싸져 있으므로 List

    public List<PropertyItem> getData() {
        return data;
    }

    public void setData(List<PropertyItem> data) {
        this.data = data;
    }
}
