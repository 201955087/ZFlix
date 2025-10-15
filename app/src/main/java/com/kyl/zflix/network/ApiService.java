package com.kyl.zflix.network;

import com.kyl.zflix.model.PropertyListResponse;
import com.kyl.zflix.model.PropertySingleResponse;
import com.kyl.zflix.model.PropertyRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    // ⭐ 기존 API 메서드를 유지하되, 필터링 정보를 담은 PropertyRequest를 받도록 수정합니다.
    // 백엔드에서 이 정보를 처리하여 필터링된 데이터를 반환해야 합니다.

    // 원룸
    @POST("/one_roomPropertyApi/propertyData")
    Call<PropertySingleResponse> getOneRoomPropertyDetail(@Body PropertyRequest request);

    @POST("/one_roomPropertyApi/propertyDataList")
    Call<PropertyListResponse> getOneRoomProperties(@Body PropertyRequest request);


    // 빌라
    @POST("/villaPropertyApi/propertyData")
    Call<PropertySingleResponse> getVillaPropertyDetail(@Body PropertyRequest request);

    @POST("/villaPropertyApi/propertyDataList")
    Call<PropertyListResponse> getVillaProperties(@Body PropertyRequest request);


    // 오피스텔
    @POST("/officetelPropertyApi/propertyData")
    Call<PropertySingleResponse> getOfficetelPropertyDetail(@Body PropertyRequest request);

    @POST("/officetelPropertyApi/propertyDataList")
    Call<PropertyListResponse> getOfficetelProperties(@Body PropertyRequest request);


    // 아파트
    @POST("/apartmentPropertyApi/propertyData")
    Call<PropertySingleResponse> getApartmentPropertyDetail(@Body PropertyRequest request);

    @POST("/apartmentPropertyApi/propertyDataList")
    Call<PropertyListResponse> getApartmentProperties(@Body PropertyRequest request);


    // 단독/다가구
    @POST("/detached_multiPropertyApi/propertyData")
    Call<PropertySingleResponse> getDetachedMultiPropertyDetail(@Body PropertyRequest request);

    @POST("/detached_multiPropertyApi/propertyDataList")
    Call<PropertyListResponse> getDetachedMultiProperties(@Body PropertyRequest request);

}