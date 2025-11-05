package com.kyl.zflix;

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

import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.java.GenerativeModelFutures; // Java Futures Wrapper 클래스
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    // ✅ Gemini 모델 (모델 이름 수정)
    private GenerativeModel generativeModel;
    private GenerativeModelFutures generativeModelFutures;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // ActivityResultLauncher (필터)
    private final ActivityResultLauncher<Intent> filterLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        currentFilterRequest.setDistrict(data.getStringExtra("district"));
                        currentFilterRequest.setLegalDong(data.getStringExtra("legal_dong"));
                        currentFilterRequest.setDepositMin(data.getStringExtra("deposit_min"));
                        currentFilterRequest.setDepositMax(data.getStringExtra("deposit_max"));
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
                        currentFilterRequest.setNetAreaMin(data.getStringExtra("net_min"));
                        currentFilterRequest.setNetAreaMax(data.getStringExtra("net_max"));
                        currentFilterRequest.setApprovalAgeGroup(data.getStringExtra("approval_age_group"));
                        currentFilterRequest.setInteriorFacilities(data.getStringArrayListExtra("interior_facilities_list"));
                        loadPropertyList();
                    }
                }
            });
    String geminiKey = BuildConfig.GEMINI_API_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        propertyType = getIntent().getStringExtra("type");

        // ✅ 모델 이름 수정: "gemini-1.5-flash" -> "gemini-2.5-flash"
        generativeModel = new GenerativeModel("gemini-2.5-flash", geminiKey);
        generativeModelFutures = GenerativeModelFutures.from(generativeModel);

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
            if ("아파트".equals(propertyType) || "오피스텔".equals(propertyType)) {
                intent = new Intent(PropertyActivity.this, FiltermActivity.class);
            } else {
                intent = new Intent(PropertyActivity.this, FilterActivity.class);
            }

            intent.putExtra("type", propertyType);
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

                        // AI 요약 요청을 처음 5개 항목으로 제한
                        int limit = Math.min(items.size(), 5);

                        for (int i = 0; i < limit; i++) {
                            PropertyListItem item = items.get(i);
                            requestAiSummary(item);
                        }
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

    /**
     * AI 요약 요청 (Java Futures 방식)
     */
    private void requestAiSummary(PropertyListItem item) {
        // 1️⃣ 주소 조합
        String address = String.join(" ",
                safeString(item.getCity()),
                safeString(item.getDistrict()),
                safeString(item.getLegal_dong()),
                safeString(item.getDetail_address())
        ).trim();

        if (address.isEmpty()) {
            Log.w(TAG, "AI 요약 요청 건너뜀: 주소가 비어 있음 (" + item.getListingId() + ")");
            return;
        }

        // 2️⃣ 프롬프트 확인 로그
        String prompt = "주소: " + address + "\n"
                + "이 위치 주변의 주요 인프라, 가장 가까운 지하철역 이름, "
                + "도보 거리(분 단위)를 10~20글자정도의 한 문장으로 요약해줘. "
                + "예: '홍대입구역 도보 5분 거리, 근처에 카페와 편의점 밀집'";
        Log.d(TAG, "AI 요청 시작 (" + item.getListingId() + "): " + prompt);

        try {
            // 3️⃣ Content 생성
            Content content = new Content.Builder()
                    .addText(prompt)
                    .build();

            // 4️⃣ Futures 비동기 호출
            ListenableFuture<GenerateContentResponse> responseFuture =
                    generativeModelFutures.generateContent(content);

            // 5️⃣ 결과 콜백 처리
            Futures.addCallback(responseFuture, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    // 결과 텍스트 로그 출력
                    String aiSummary = result.getText();
                    Log.d(TAG, "AI 응답 성공 (" + item.getListingId() + "): " + aiSummary);

                    if (aiSummary != null && !aiSummary.isEmpty()) {
                        // ✅ UI 스레드에서 어댑터 업데이트
                        runOnUiThread(() -> {
                            adapter.setSummaryForListing(item.getListingId(), aiSummary);
                            // Toast.makeText(PropertyActivity.this, "AI 요약 완료: " + item.getListingId(), Toast.LENGTH_SHORT).show(); // 디버깅용으로 주석 처리
                        });
                    } else {
                        Log.w(TAG, "AI 응답이 비어 있음 (" + item.getListingId() + ")");
                        runOnUiThread(() ->
                                adapter.setSummaryForListing(item.getListingId(), "AI가 응답하지 못했습니다.")
                        );
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    // ❌ 실패 원인 로깅
                    Log.e(TAG, "AI 요약 생성 실패 (" + item.getListingId() + "): " + t.getMessage(), t);
                    runOnUiThread(() ->
                            adapter.setSummaryForListing(item.getListingId(), "AI 정보 로딩 실패")
                    );
                }
            }, executorService);
        } catch (Exception e) {
            Log.e(TAG, "AI 요청 중 예외 발생 (" + item.getListingId() + "): " + e.getMessage(), e);
        }
    }
    private String safeString(String s) {
        return s == null ? "" : s;
    }

    @Override
    public void onItemClick(String listingId, String itemPropertyType) {
        Intent intent = new Intent(PropertyActivity.this, PropertyDetailsActivity.class);
        intent.putExtra("listingId", listingId);
        intent.putExtra("type", propertyType);
        startActivity(intent);
    }
}