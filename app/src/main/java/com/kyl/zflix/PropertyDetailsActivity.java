package com.kyl.zflix;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.kyl.zflix.adapter.ImagePagerAdapter;
import com.kyl.zflix.model.ListingRequest;
import com.kyl.zflix.model.PropertyItem;
import com.kyl.zflix.model.PropertyListItem;
import com.kyl.zflix.model.PropertyRequest;
import com.kyl.zflix.model.PropertySingleResponse;
import com.kyl.zflix.model.Transaction;
import com.kyl.zflix.model.TransactionsResponse;
import com.kyl.zflix.network.ApiClient;
import com.kyl.zflix.network.ApiService;
import com.kyl.zflix.network.FirestoreManager;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;



public class PropertyDetailsActivity extends AppCompatActivity {

    private ViewPager2 roomImageViewPager;
    private TextView title, priceDifference, address, description, toggleDescription; // toggleDescription 추가
    private Button contactButton;
    private ImageView mapImageView;
    private TextView mapTitleTextView;

    private ImageView copyAddressIcon;
    // 즐겨찾기 아이콘
    private ImageView favoriteIcon;

    // XML에 추가된 TextView 변수들 선언
    private TextView listingId, propertyType, depositMonthlyRent, area, maintenanceFee, availableMoveInDate,
            direction, approvalDate, roomBathroom, floorInfo, isDuplex, illegalBuilding, parkingAvailable,
            totalParkingSpaces, propertyFeatures, interiorFacilities, brokerageFee, brokerName,deposit_diff_pct,rent_diff_pct;

    private FirestoreManager firestoreManager; // 추가된 FirestoreManager 변수

    private LineChart depositChart, rentChart; // [ADD] 거래 이력 차트 뷰

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

            // [ADD] 상세 페이지 진입 시 거래 이력 차트도 불러오기 (POST)
            fetchTransactions(itemFromIntent.getListingId());
        } else {
            // 기존 API 호출 로직
            String listingIdFromIntent = getIntent().getStringExtra("listingId");
            String propertyTypeFromIntent = getIntent().getStringExtra("type");

            if (listingIdFromIntent != null && propertyTypeFromIntent != null) {
                fetchPropertyDetails(listingIdFromIntent, propertyTypeFromIntent);

                // [ADD]
                fetchTransactions(listingIdFromIntent);
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
        deposit_diff_pct = findViewById(R.id.deposit_diff_pct);
        rent_diff_pct= findViewById(R.id.rent_diff_pct);
        address = findViewById(R.id.address);
        description = findViewById(R.id.description);
        toggleDescription = findViewById(R.id.toggleDescription); // 추가
        contactButton = findViewById(R.id.contactButton);
        mapImageView = findViewById(R.id.mapImageView);
        mapTitleTextView = findViewById(R.id.mapTitleTextView);

        favoriteIcon = findViewById(R.id.favoriteIcon);
        copyAddressIcon = findViewById(R.id.copyAddressIcon);
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
        brokerName = findViewById(R.id.brokerName);

        depositChart = findViewById(R.id.depositChart);
        rentChart = findViewById(R.id.rentChart);
    }

    private void bindDataFromListItem(PropertyListItem item) {
        depositMonthlyRent.setText(item.getDeposit() + " / " + item.getMonthlyRent());
        title.setText(item.getPropertyType());
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
                return;
        }

        call.enqueue(new Callback<PropertySingleResponse>() {
            @Override
            public void onResponse(Call<PropertySingleResponse> call, Response<PropertySingleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PropertyItem> propertyItemList = response.body().getData();

                    if (propertyItemList != null && !propertyItemList.isEmpty()) {
                        PropertyItem propertyItem = propertyItemList.get(0);
                        bindDataToViews(propertyItem);
                    } else {
                        Toast.makeText(PropertyDetailsActivity.this, "상세 정보를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PropertyDetailsActivity.this, "상세 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PropertySingleResponse> call, Throwable t) {
                Toast.makeText(PropertyDetailsActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bindDataToViews(PropertyItem propertyItem) {
        // 이미지 설정
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

        // 지도 이미지
        if (propertyItem.getMapImageUrl() != null && !propertyItem.getMapImageUrl().isEmpty()) {
            Glide.with(this).load(propertyItem.getMapImageUrl()).into(mapImageView);
            mapTitleTextView.setVisibility(View.VISIBLE);
            mapImageView.setVisibility(View.VISIBLE);
        } else {
            mapTitleTextView.setVisibility(View.GONE);
            mapImageView.setVisibility(View.GONE);
        }

        // 주소
        title.setText(propertyItem.getPropertyType());
        priceDifference.setText("보증금 " + propertyItem.getDeposit() + " / 월세 " + propertyItem.getMonthlyRent());


//        priceDifference.setText("유사 매물 대비 보증금이"+  propertyItem.getDeposit_diff_pct() +"(낮습니다 or 높습니다) ");
        String Deposit_diffStr = propertyItem.getDeposit_diff_pct(); // 예: "-52.34" 또는 "34.12"
        String statusText;
        int Deposit_diff_color;

// 문자열이 "-"로 시작하면 음수
        if (Deposit_diffStr.startsWith("-")) {
            statusText = "낮습니다";
            Deposit_diff_color = Color.BLUE;
        } else {
            statusText = "높습니다";
            Deposit_diff_color = Color.RED;
        }

// 텍스트 설정
        deposit_diff_pct.setText("유사 매물 대비 보증금이 " + Deposit_diffStr + "% " + statusText);
        deposit_diff_pct.setTextColor(Deposit_diff_color);
//        rent_diff_pct
        String rent_diffStr = propertyItem.getRent_diff_pct(); // 예: "-52.34" 또는 "34.12"
        String rent_diffText;
        int rent_diff_color;

// 문자열이 "-"로 시작하면 음수
        if (rent_diffStr.startsWith("-")) {
            rent_diffText = "낮습니다";
            rent_diff_color = Color.BLUE;
        } else {
            rent_diffText = "높습니다";
            rent_diff_color = Color.RED;
        }

// 텍스트 설정
        rent_diff_pct.setText("유사 매물 대비 월세가 " + rent_diffStr + "% " + rent_diffText);
        rent_diff_pct.setTextColor(rent_diff_color);

//        rent_diff_pct.setText("유사 매물 대비 월세가"+  propertyItem.getRent_diff_pct() +"(낮습니다 or 높습니다) ");



        String fullAddress = propertyItem.getCity() + " " + propertyItem.getDistrict() + " " +
                propertyItem.getLegalDong() + " " + propertyItem.getDetailAddress();
        address.setText(fullAddress);

        copyAddressIcon.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("address", address.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "주소가 복사되었습니다.", Toast.LENGTH_SHORT).show();
        });

        listingId.setText(propertyItem.getListingId());
        propertyType.setText(propertyItem.getPropertyType());
        depositMonthlyRent.setText(propertyItem.getDeposit() + " / " + propertyItem.getMonthlyRent());

        if ("오피스텔".equals(propertyItem.getPropertyType())
                || "오피스텔분양권".equals(propertyItem.getPropertyType())
                || "상가주택".equals(propertyItem.getPropertyType())) {
            area.setText(propertyItem.getContract_area() + " / " + propertyItem.getNetArea());
        } else {
            area.setText(propertyItem.getGrossArea() + " / " + propertyItem.getNetArea());
        }


        // 매물특징 줄바꿈 변환
        String formattedDescription = propertyItem.getDescription().replace("\\", "\n");
        description.setText(formattedDescription);

        // =================================================================
        // ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 이 부분이 수정되었습니다 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
        // =================================================================

        // 더보기 / 접기 기능
        description.post(() -> {
            // XML에서 maxLines를 제거했기 때문에, 여기서 실제 전체 줄 수를 올바르게 가져올 수 있습니다.
            if (description.getLineCount() > 3) {
                // 실제 줄 수가 3줄이 넘으면 버튼을 보이게 하고,
                // 이 시점에서 글자 수를 3줄로 제한합니다.
                toggleDescription.setVisibility(View.VISIBLE);
                description.setMaxLines(3); // ★★★ 핵심: 조건문 안으로 위치 이동
                toggleDescription.setText("더보기");
            } else {
                // 3줄 이하면 버튼을 숨깁니다.
                toggleDescription.setVisibility(View.GONE);
            }
        });

        toggleDescription.setOnClickListener(new View.OnClickListener() {
            private boolean expanded = false;

            @Override
            public void onClick(View v) {
                if (expanded) {
                    description.setMaxLines(3);
                    toggleDescription.setText("더보기");
                } else {
                    description.setMaxLines(Integer.MAX_VALUE);
                    toggleDescription.setText("접기");
                }
                expanded = !expanded;
            }
        });

        // =================================================================
        // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ 수정된 부분 끝 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
        // =================================================================


        maintenanceFee.setText(propertyItem.getMaintenanceFee()+"만원");
        availableMoveInDate.setText(propertyItem.getAvailableMoveInDate());
        direction.setText(propertyItem.getDirection());
        approvalDate.setText(propertyItem.getApprovalAgeGroup());
        roomBathroom.setText(propertyItem.getNumRooms() + " / " + propertyItem.getNumBathrooms()+ "개");
        floorInfo.setText(propertyItem.getFloor() + "/" + propertyItem.getTotalFloors() + "층");
        isDuplex.setText(propertyItem.getIsDuplex());
        illegalBuilding.setText(propertyItem.getIllegalBuilding());
        parkingAvailable.setText(propertyItem.getParkingAvailable());
        totalParkingSpaces.setText(propertyItem.getTotalParkingSpaces());
        propertyFeatures.setText(propertyItem.getPropertyFeatures());

        interiorFacilities.setText(propertyItem.getInteriorFacilities().replace("\\", ","));

        brokerageFee.setText(propertyItem.getBrokerageFee());

        brokerName.setText(propertyItem.getAgent().replace("\\", "\n"));

        // 즐겨찾기 처리
        final String propertyTypeStr = propertyItem.getPropertyType();
        final String listingIdStr = propertyItem.getListingId();
        checkFavoriteStatus(propertyTypeStr, listingIdStr);

        favoriteIcon.setOnClickListener(v -> {
            boolean isCurrentlyFavorite = (boolean) favoriteIcon.getTag();

            if (isCurrentlyFavorite) {
                firestoreManager.removeFavoriteFromFirestore(propertyTypeStr, listingIdStr, new FirestoreManager.FavoriteActionCallback() {
                    @Override
                    public void onSuccess() {
                        updateFavoriteIcon(false);
                        favoriteIcon.setTag(false);
                        Toast.makeText(PropertyDetailsActivity.this, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(PropertyDetailsActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                firestoreManager.addFavoriteToFirestore(propertyTypeStr, listingIdStr, new FirestoreManager.FavoriteActionCallback() {
                    @Override
                    public void onSuccess() {
                        updateFavoriteIcon(true);
                        favoriteIcon.setTag(true);
                        Toast.makeText(PropertyDetailsActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(PropertyDetailsActivity.this, "추가 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void checkFavoriteStatus(String propertyTypeStr, String listingIdStr) {
        firestoreManager.isFavorite(propertyTypeStr, listingIdStr, new FirestoreManager.FavoriteStatusCallback() {
            @Override
            public void onResult(boolean isFav) {
                updateFavoriteIcon(isFav);
                favoriteIcon.setTag(isFav);
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

    // [ADD] 거래 이력 POST 호출
    private void fetchTransactions(String listingId) {
        ApiService api = ApiClient.getApiService();

        // POST 바디
        ListingRequest body = new ListingRequest(listingId);

        api.getTransactions(body).enqueue(new Callback<TransactionsResponse>() {
            @Override
            public void onResponse(Call<TransactionsResponse> call, Response<TransactionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Transaction> items = response.body().data;
                    renderTransactionsChart(items);
                } else {
                    Toast.makeText(PropertyDetailsActivity.this, "거래 이력 응답 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionsResponse> call, Throwable t) {
                Toast.makeText(PropertyDetailsActivity.this, "거래 이력 네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // [ADD] 차트 기본 옵션
    private void setupLineChart(LineChart chart) {
        if (chart == null) return;
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setGranularity(1f);
        x.setDrawGridLines(false);

        chart.getAxisRight().setEnabled(false);
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
    }



    // [ADD] 데이터 렌더링
    private void renderTransactionsChart(List<Transaction> items) {
        if (items == null || items.isEmpty()) {
            if (depositChart != null) { depositChart.setData(null); depositChart.invalidate(); }
            if (rentChart != null)    { rentChart.setData(null);    rentChart.invalidate(); }
            return;
        }

        // 연도 오름차순 보장
        items.sort((a, b) -> Integer.compare(a.contract_date, b.contract_date));

        final ArrayList<Integer> years = new ArrayList<>();
        List<Entry> depositEntries = new ArrayList<>();
        List<Entry> rentEntries    = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            Transaction t = items.get(i);
            years.add(t.contract_date);
            depositEntries.add(new Entry(i, (float) t.deposit_amount)); // 만원
            rentEntries.add(new Entry(i, (float) t.monthly_rent));      // 만원
        }

        // 공통 차트 옵션
        setupLineChart(depositChart);
        setupLineChart(rentChart);

        // 1) 보증금 차트
        LineDataSet depositSet = new LineDataSet(depositEntries, "보증금(만원)");
        depositSet.setLineWidth(2f);
        depositSet.setDrawCircles(true);
        depositSet.setDrawValues(false);
        // 색상(원하면 변경)
         depositSet.setColor(Color.parseColor("#1976D2"));
         depositSet.setCircleColor(Color.parseColor("#1976D2"));

        depositChart.setData(new LineData(depositSet));
        depositChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override public String getFormattedValue(float value) {
                int i = (int) value; return (i >= 0 && i < years.size()) ? String.valueOf(years.get(i)) : "";
            }
        });
        depositChart.invalidate();

        // 2) 월세 차트
        LineDataSet rentSet = new LineDataSet(rentEntries, "월세(만원)");
        rentSet.setLineWidth(2f);
        rentSet.setDrawCircles(true);
        rentSet.setDrawValues(false);
        // 색상(원하면 변경)
         rentSet.setColor(Color.parseColor("#D32F2F"));
         rentSet.setCircleColor(Color.parseColor("#D32F2F"));

        rentChart.setData(new LineData(rentSet));
        rentChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override public String getFormattedValue(float value) {
                int i = (int) value; return (i >= 0 && i < years.size()) ? String.valueOf(years.get(i)) : "";
            }
        });
        rentChart.invalidate();
    }

}
