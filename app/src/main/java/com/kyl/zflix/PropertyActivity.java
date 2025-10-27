package com.kyl.zflix;
// ... (기존 import 문)
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kyl.zflix.adapter.PropertyAdapter;
import com.kyl.zflix.model.PropertyListItem;
import com.kyl.zflix.model.PropertyListResponse;
import com.kyl.zflix.model.PropertyRequest;
import com.kyl.zflix.network.ApiClient;
import com.kyl.zflix.network.ApiService;
import com.kyl.zflix.ui.FilterActivity;
import com.kyl.zflix.ui.FiltermActivity;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyActivity extends AppCompatActivity implements PropertyAdapter.OnItemClickListener {

    private static final String TAG = "PropertyActivity";

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private ApiService apiService;
    private String propertyType;
    private PropertyRequest currentFilterRequest;

    // ActivityResultLauncher는 동일하게 사용
    private final ActivityResultLauncher<Intent> filterLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {

                        // 보증금, 법정동 등 필드
                        currentFilterRequest.setDistrict(data.getStringExtra("district"));
                        currentFilterRequest.setLegalDong(data.getStringExtra("legal_dong"));

                        currentFilterRequest.setDepositMin(data.getStringExtra("deposit_min"));
                        currentFilterRequest.setDepositMax(data.getStringExtra("deposit_max"));

                        // 월세 처리
                        String minStr = data.getStringExtra("monthly_min");
                        String maxStr = data.getStringExtra("monthly_max");

                        Integer minRent = null;
                        Integer maxRent = null;

                        try {
                            if (minStr != null) minRent = Integer.parseInt(minStr);
                            if (maxStr != null) maxRent = Integer.parseInt(maxStr);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        currentFilterRequest.setMonthlyRentMin(minRent);
                        currentFilterRequest.setMonthlyRentMax(maxRent);

                        // 전용면적 필터
                        currentFilterRequest.setNetAreaMin(data.getStringExtra("net_min"));
                        currentFilterRequest.setNetAreaMax(data.getStringExtra("net_max"));

                        // 사용승인일 필터
                        currentFilterRequest.setApprovalAgeGroup(data.getStringExtra("approval_age_group"));

                        // 내부 시설
                        currentFilterRequest.setInteriorFacilities(data.getStringArrayListExtra("interior_facilities_list"));

                        // 필터 적용 후 다시 매물 리스트 로드
                        loadPropertyList();
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        propertyType = getIntent().getStringExtra("type");

        LinearLayout logo = findViewById(R.id.logo_zflix);
        logo.setOnClickListener(v -> {
            Intent intent = new Intent(PropertyActivity.this, MainActivity.class);
            intent.putExtra("navigateTo", "home");
            startActivity(intent);
            finish();
        });

        ImageView filterIcon = findViewById(R.id.filter_icon);
        filterIcon.setOnClickListener(v -> {
            Intent intent;

            // 매물 유형에 따라 이동할 필터 액티비티를 결정합니다.
            if ("아파트".equals(propertyType) || "오피스텔".equals(propertyType)) {
                // 아파트, 오피스텔: FiltermActivity로 이동
                intent = new Intent(PropertyActivity.this, FiltermActivity.class);
            } else {
                // 원룸, 빌라, 단독/다가구: FilterActivity로 이동
                intent = new Intent(PropertyActivity.this, FilterActivity.class);
            }

            intent.putExtra("type", propertyType);

            // 🌟🌟🌟 추가: 현재 적용된 필터 값을 Intent에 담아 전달합니다. 🌟🌟🌟

            // 지역/법정동
            intent.putExtra("current_district", currentFilterRequest.getDistrict());
            intent.putExtra("current_legal_dong", currentFilterRequest.getLegalDong());

            // 보증금
            intent.putExtra("current_deposit_min", currentFilterRequest.getDepositMin());
            intent.putExtra("current_deposit_max", currentFilterRequest.getDepositMax());

            // 월세 (Integer 값을 String으로 변환)
            if (currentFilterRequest.getMonthlyRentMin() != null) {
                intent.putExtra("current_monthly_min", currentFilterRequest.getMonthlyRentMin().toString());
            } else {
                intent.putExtra("current_monthly_min", (String) null); // null 명시
            }
            if (currentFilterRequest.getMonthlyRentMax() != null) {
                intent.putExtra("current_monthly_max", currentFilterRequest.getMonthlyRentMax().toString());
            } else {
                intent.putExtra("current_monthly_max", (String) null); // null 명시
            }

            // 전용면적
            intent.putExtra("current_net_min", currentFilterRequest.getNetAreaMin());
            intent.putExtra("current_net_max", currentFilterRequest.getNetAreaMax());

            // 사용승인일
            intent.putExtra("current_approval_age_group", currentFilterRequest.getApprovalAgeGroup());

            // 내부 시설 🌟🌟🌟 List<String>을 ArrayList<String>으로 변환하여 전달 (오류 수정) 🌟🌟🌟
            List<String> facilitiesList = currentFilterRequest.getInteriorFacilities();
            if (facilitiesList != null) {
                intent.putStringArrayListExtra("current_facilities", new ArrayList<>(facilitiesList));
            } else {
                intent.putStringArrayListExtra("current_facilities", null);
            }

            filterLauncher.launch(intent);
        });

        recyclerView = findViewById(R.id.propertyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PropertyAdapter(this, new ArrayList<>(), this);
        adapter.setPropertyType(propertyType);
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getApiService();

        currentFilterRequest = new PropertyRequest(propertyType);
        loadPropertyList();
    }

    private void loadPropertyList() {
        Log.d(TAG, "Request Type: " + propertyType);
        // 디버깅을 위해 현재 요청 필터 로그 출력
        Log.d(TAG, "Current Filter: " + currentFilterRequest.toString());

        Call<PropertyListResponse> call;

        switch (propertyType) {
            case "원룸":
                call = apiService.getOneRoomProperties(currentFilterRequest);
                break;
            case "빌라":
                call = apiService.getVillaProperties(currentFilterRequest);
                break;
            case "아파트":
                call = apiService.getApartmentProperties(currentFilterRequest);
                break;
            case "오피스텔":
                call = apiService.getOfficetelProperties(currentFilterRequest);
                break;
            case "단독/다가구":
                call = apiService.getDetachedMultiProperties(currentFilterRequest);
                break;
            default:
                Toast.makeText(this, "지원하지 않는 매물 유형입니다.", Toast.LENGTH_SHORT).show();
                return;
        }

        call.enqueue(new Callback<PropertyListResponse>() {
            @Override
            public void onResponse(Call<PropertyListResponse> call, Response<PropertyListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PropertyListItem> items = response.body().getData();
                    if (items != null && !items.isEmpty()) {
                        adapter.updateData(items);
                    } else {
                        Toast.makeText(PropertyActivity.this, "매물 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                        adapter.updateData(new ArrayList<>());
                    }
                } else {
                    Toast.makeText(PropertyActivity.this, "리스트 데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PropertyListResponse> call, Throwable t) {
                Toast.makeText(PropertyActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(String listingId, String itemPropertyType) {
        Intent intent = new Intent(PropertyActivity.this, PropertyDetailsActivity.class);
        intent.putExtra("listingId", listingId);
        intent.putExtra("type", propertyType);
        startActivity(intent);
    }
}