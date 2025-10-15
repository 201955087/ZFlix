package com.kyl.zflix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kyl.zflix.adapter.ImagePagerAdapter;
import com.kyl.zflix.model.PropertyItem;
import com.kyl.zflix.model.PropertyListItem; // PropertyListItem import 추가
import com.kyl.zflix.model.PropertyRequest;
import com.kyl.zflix.model.PropertySingleResponse;
import com.kyl.zflix.network.ApiClient;
import com.kyl.zflix.network.ApiService;
import com.kyl.zflix.network.FirestoreManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyDetailsActivity extends AppCompatActivity {

    private ViewPager2 roomImageViewPager;
    private TextView title, priceDifference, address, description;
    private Button contactButton;
    private ImageView mapImageView;
    private TextView mapTitleTextView;

    // 즐겨찾기 아이콘
    private ImageView favoriteIcon;

    // XML에 추가된 TextView 변수들 선언
    private TextView listingId, propertyType, depositMonthlyRent, area, maintenanceFee, availableMoveInDate, direction, approvalDate, roomBathroom, floorInfo, isDuplex, illegalBuilding, parkingAvailable, totalParkingSpaces, propertyFeatures, interiorFacilities, brokerageFee, loanAmount, brokerName, brokerPhone;

    private FirestoreManager firestoreManager; // 추가된 FirestoreManager 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_details_page);

        // FirestoreManager 인스턴스 초기화
        firestoreManager = new FirestoreManager();

        initViews();

        // Intent로부터 PropertyListItem 객체 받기
        PropertyListItem itemFromIntent = (PropertyListItem) getIntent().getSerializableExtra("property_item");

        if (itemFromIntent != null) {
            // 받은 객체로 UI 업데이트
            bindDataFromListItem(itemFromIntent);
            // 필요에 따라 상세 API 호출
            fetchPropertyDetails(itemFromIntent.getListingId(), itemFromIntent.getPropertyType());
        } else {
            // 기존 API 호출 로직은 유지하되, 만약 아이템이 null이면 기존의 listingId와 type을 사용
            String listingIdFromIntent = getIntent().getStringExtra("listingId");
            String propertyTypeFromIntent = getIntent().getStringExtra("type");

            if (listingIdFromIntent != null && propertyTypeFromIntent != null) {
                fetchPropertyDetails(listingIdFromIntent, propertyTypeFromIntent);
            } else {
                Toast.makeText(this, "매물 정보가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void initViews() {
        roomImageViewPager = findViewById(R.id.roomImageViewPager);
        title = findViewById(R.id.title);
        priceDifference = findViewById(R.id.priceDifference);
        address = findViewById(R.id.address);
        description = findViewById(R.id.description);
        contactButton = findViewById(R.id.contactButton);
        mapImageView = findViewById(R.id.mapImageView);
        mapTitleTextView = findViewById(R.id.mapTitleTextView);

        favoriteIcon = findViewById(R.id.favoriteIcon);

        listingId = findViewById(R.id.listingId);
        propertyType = findViewById(R.id.propertyType);
        depositMonthlyRent = findViewById(R.id.depositMonthlyRent);
        area = findViewById(R.id.area);
        maintenanceFee = findViewById(R.id.maintenanceFee);
        availableMoveInDate = findViewById(R.id.availableMoveInDate);
        direction = findViewById(R.id.direction);
        approvalDate = findViewById(R.id.approvalDate);
        roomBathroom = findViewById(R.id.roomBathroom);
        floorInfo = findViewById(R.id.floorInfo);
        isDuplex = findViewById(R.id.isDuplex);
        illegalBuilding = findViewById(R.id.illegalBuilding);
        parkingAvailable = findViewById(R.id.parkingAvailable);
        totalParkingSpaces = findViewById(R.id.totalParkingSpaces);
        propertyFeatures = findViewById(R.id.propertyFeatures);
        interiorFacilities = findViewById(R.id.interiorFacilities);
        brokerageFee = findViewById(R.id.brokerageFee);
        loanAmount = findViewById(R.id.loanAmount);
        brokerName = findViewById(R.id.brokerName);
        brokerPhone = findViewById(R.id.brokerPhone);
    }

    // PropertyListItem 객체에서 데이터 바인딩하는 새로운 메서드 추가
    private void bindDataFromListItem(PropertyListItem item) {
        // 이 메서드에서는 PropertyListItem이 제공하는 데이터만 바인딩
        depositMonthlyRent.setText(item.getDeposit() + " / " + item.getMonthlyRent());
        title.setText(item.getPropertyType());
        // ... 필요한 다른 데이터 바인딩 ...
    }


    private void fetchPropertyDetails(String listingId, String type) {
        ApiService apiService = ApiClient.getApiService();
        Call<PropertySingleResponse> call;
        PropertyRequest request = new PropertyRequest(type, listingId);

        switch (type) {
            case "원룸":
                call = apiService.getOneRoomPropertyDetail(request);
                break;
            case "빌라":
                call = apiService.getVillaPropertyDetail(request);
                break;
            case "아파트":
                call = apiService.getApartmentPropertyDetail(request);
                break;
            case "오피스텔":
                call = apiService.getOfficetelPropertyDetail(request);
                break;
            case "단독/다가구":
                call = apiService.getDetachedMultiPropertyDetail(request);
                break;
            default:
                Toast.makeText(this, "지원하지 않는 매물 유형입니다.", Toast.LENGTH_SHORT).show();
                Log.e("API_CALL", "Unsupported property type: " + type);
                return;
        }

        Log.d("API_CALL", "Requesting details for listingId: " + listingId + " with type: " + type);

        call.enqueue(new Callback<PropertySingleResponse>() {
            @Override
            public void onResponse(Call<PropertySingleResponse> call, Response<PropertySingleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Received successful response.");
                    PropertySingleResponse fullResponse = response.body();
                    List<PropertyItem> propertyItemList = fullResponse.getData();

                    if (propertyItemList != null && !propertyItemList.isEmpty()) {
                        PropertyItem propertyItem = propertyItemList.get(0);
                        bindDataToViews(propertyItem);
                    } else {
                        Log.w("API_WARNING", "Response successful, but data list is empty.");
                        Toast.makeText(PropertyDetailsActivity.this, "상세 정보를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String errorMessage = "Failed to load details. Code: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMessage += ", Error Body: " + response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("API_ERROR", "Error reading error body.", e);
                    }
                    Log.e("API_ERROR", errorMessage);
                    Toast.makeText(PropertyDetailsActivity.this, "상세 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PropertySingleResponse> call, Throwable t) {
                Log.e("API_FAILURE", "API Call Failed: " + t.getMessage(), t);
                Toast.makeText(PropertyDetailsActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bindDataToViews(PropertyItem propertyItem) {
        // 이미지 URL 리스트 생성
        List<String> imageUrlsList = new ArrayList<>();
        String imageUrls = propertyItem.getImageUrl();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            imageUrlsList.addAll(Arrays.asList(imageUrls.split(",")));
        }

        if (!imageUrlsList.isEmpty()) {
            ImagePagerAdapter imageAdapter = new ImagePagerAdapter(imageUrlsList);
            roomImageViewPager.setAdapter(imageAdapter);
            roomImageViewPager.setVisibility(View.VISIBLE);
        } else {
            roomImageViewPager.setVisibility(View.GONE);
        }

        // map image
        String mapImageUrl = propertyItem.getMapImageUrl();
        if (mapImageUrl != null && !mapImageUrl.isEmpty()) {
            Glide.with(this).load(mapImageUrl).into(mapImageView);
            mapTitleTextView.setVisibility(View.VISIBLE);
            mapImageView.setVisibility(View.VISIBLE);
        } else {
            mapTitleTextView.setVisibility(View.GONE);
            mapImageView.setVisibility(View.GONE);
        }

        // 텍스트 바인딩
        title.setText(propertyItem.getPropertyType());
        priceDifference.setText("보증금 " + propertyItem.getDeposit() + " / 월세 " + propertyItem.getMonthlyRent());
        String fullAddress = propertyItem.getCity() + " " + propertyItem.getDistrict() + " " + propertyItem.getLegalDong() + " " + propertyItem.getDetailAddress();
        address.setText(fullAddress);

        listingId.setText(propertyItem.getListingId());
        propertyType.setText(propertyItem.getPropertyType());
        depositMonthlyRent.setText(propertyItem.getDeposit() + " / " + propertyItem.getMonthlyRent());
        area.setText(propertyItem.getGrossArea() + " / " + propertyItem.getNetArea());
        description.setText(propertyItem.getDescription());
        maintenanceFee.setText(propertyItem.getMaintenanceFee());
        availableMoveInDate.setText(propertyItem.getAvailableMoveInDate());
        direction.setText(propertyItem.getDirection());
        approvalDate.setText(propertyItem.getApprovalDate());
        roomBathroom.setText(propertyItem.getNumRooms() + " / " + propertyItem.getNumBathrooms());
        floorInfo.setText(propertyItem.getFloor() + "/" + propertyItem.getTotalFloors() + "층");
        isDuplex.setText(propertyItem.getIsDuplex());
        illegalBuilding.setText(propertyItem.getIllegalBuilding());
        parkingAvailable.setText(propertyItem.getParkingAvailable());
        totalParkingSpaces.setText(propertyItem.getTotalParkingSpaces());
        propertyFeatures.setText(propertyItem.getPropertyFeatures());
        interiorFacilities.setText(propertyItem.getInteriorFacilities());
        brokerageFee.setText(propertyItem.getBrokerageFee());
        loanAmount.setText(propertyItem.getLoanAmount());
        brokerName.setText(propertyItem.getAgent());
        //        brokerPhone.setText(propertyItem.getBrokerPhone());


        // -------------------
        // 즐겨찾기 관련 처리 - Firestore로 교체
        // -------------------
        final String propertyTypeStr = propertyItem.getPropertyType();
        final String listingIdStr = propertyItem.getListingId();

        // 초기 즐겨찾기 상태 확인
        checkFavoriteStatus(propertyTypeStr, listingIdStr);

        // 클릭 리스너 - Firestore로 즐겨찾기 추가/삭제
        favoriteIcon.setOnClickListener(v -> {
            boolean isCurrentlyFavorite = (boolean) favoriteIcon.getTag();

            if (isCurrentlyFavorite) {
                // 즐겨찾기 취소
                firestoreManager.removeFavoriteFromFirestore(propertyTypeStr, listingIdStr, new FirestoreManager.FavoriteActionCallback() {
                    @Override
                    public void onSuccess() {
                        updateFavoriteIcon(false);
                        favoriteIcon.setTag(false);
                        Toast.makeText(PropertyDetailsActivity.this, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("FAVORITE_REMOVE_FAIL", "즐겨찾기 삭제 실패", e);
                        Toast.makeText(PropertyDetailsActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // 즐겨찾기 추가
                firestoreManager.addFavoriteToFirestore(propertyTypeStr, listingIdStr, new FirestoreManager.FavoriteActionCallback() {
                    @Override
                    public void onSuccess() {
                        updateFavoriteIcon(true);
                        favoriteIcon.setTag(true);
                        Toast.makeText(PropertyDetailsActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("FAVORITE_ADD_FAIL", "즐겨찾기 추가 실패", e);
                        Toast.makeText(PropertyDetailsActivity.this, "추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // 초기 즐겨찾기 상태를 확인하는 새로운 메서드 추가
    private void checkFavoriteStatus(String propertyTypeStr, String listingIdStr) {
        firestoreManager.isFavorite(propertyTypeStr, listingIdStr, new FirestoreManager.FavoriteStatusCallback() {
            @Override
            public void onResult(boolean isFav) {
                updateFavoriteIcon(isFav);
                favoriteIcon.setTag(isFav); // 상태를 태그로 저장
            }
        });
    }

    private void updateFavoriteIcon(boolean isFav) {
        if (isFav) {
            favoriteIcon.setImageResource(R.drawable.ic_starfull);
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_star);
        }
    }
}