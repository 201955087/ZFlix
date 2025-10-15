package com.kyl.zflix;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView; // ImageView 추가
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
import com.kyl.zflix.model.PropertyRequest; // PropertyRequest 임포트
import com.kyl.zflix.network.ApiClient;
import com.kyl.zflix.network.ApiService;
import com.kyl.zflix.ui.FilterActivity;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyActivity extends AppCompatActivity implements PropertyAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private ApiService apiService;
    private String propertyType;
    private PropertyRequest currentFilterRequest; // 필터 조건을 저장할 객체

    // ActivityResultLauncher를 사용해 결과를 받습니다.
    private final ActivityResultLauncher<Intent> filterLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // ⭐ 수정된 부분: PropertyRequest 객체를 새로 생성하지 않고 필드만 업데이트합니다.
                        currentFilterRequest.setDistrict(data.getStringExtra("district"));
                        currentFilterRequest.setLegalDong(data.getStringExtra("legalDong"));
                        currentFilterRequest.setDepositMin(data.getStringExtra("depositMin"));
                        currentFilterRequest.setDepositMax(data.getStringExtra("depositMax"));
                        currentFilterRequest.setMonthlyRentMin(data.getStringExtra("monthlyRentMin"));
                        currentFilterRequest.setMonthlyRentMax(data.getStringExtra("monthlyRentMax"));
                        currentFilterRequest.setInteriorFacilities(data.getStringArrayListExtra("interiorFacilities"));

                        // 필터가 적용된 데이터를 다시 불러옵니다.
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
            // 필터 아이콘 클릭 시 FilterActivity 시작
            Intent intent = new Intent(PropertyActivity.this, FilterActivity.class);
            filterLauncher.launch(intent);
        });

        recyclerView = findViewById(R.id.propertyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PropertyAdapter(this, new ArrayList<>(), this);
        adapter.setPropertyType(propertyType);
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getApiService();

        // 초기 로딩 시 기본 필터 요청 객체 생성
        currentFilterRequest = new PropertyRequest(propertyType);
        loadPropertyList();
    }

    private void loadPropertyList() {
        Call<PropertyListResponse> call;

        // ⭐ 필터링 조건을 담은 currentFilterRequest 객체를 @Body로 보냅니다.
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