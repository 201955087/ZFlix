package com.kyl.zflix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kyl.zflix.PropertyDetailsActivity;
import com.kyl.zflix.R;
import com.kyl.zflix.adapter.FavoriteAdapter;
import com.kyl.zflix.model.PropertyItem; // PropertyItem import가 필요합니다.
import com.kyl.zflix.model.PropertyListItem;
import com.kyl.zflix.model.PropertyListResponse;
import com.kyl.zflix.model.PropertyRequest;
import com.kyl.zflix.model.PropertySingleResponse;
import com.kyl.zflix.network.ApiClient;
import com.kyl.zflix.network.ApiService;
import com.kyl.zflix.network.Favorite;
import com.kyl.zflix.network.FirestoreManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritePlacesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private FirestoreManager firestoreManager;
    private ApiService apiService;
    private ProgressBar progressBar;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_places, container, false);

        recyclerView = view.findViewById(R.id.favorite_recycler_view);
        progressBar = view.findViewById(R.id.favorite_progress_bar);
        emptyTextView = view.findViewById(R.id.favorite_empty_text_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoriteAdapter(new ArrayList<>());

        // 어댑터에 클릭 리스너 설정
        adapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PropertyListItem item) {
                // 클릭 시 상세 페이지로 이동
                navigateToPropertyDetails(item);
            }
        });

        recyclerView.setAdapter(adapter);

        firestoreManager = new FirestoreManager();
        apiService = ApiClient.getApiService();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchFavoriteProperties();
    }

    private void fetchFavoriteProperties() {
        showLoading(true);
        firestoreManager.getFavoritesForCurrentUser(new FirestoreManager.FavoriteFetchCallback() {
            @Override
            public void onFavoritesFetched(List<Favorite> favorites) {
                if (favorites.isEmpty()) {
                    showEmptyView(true);
                    showLoading(false);
                    return;
                }
                showEmptyView(false);
                fetchPropertyDetailsFromApi(favorites);
            }

            @Override
            public void onFailure(Exception e) {
                showLoading(false);
                showEmptyView(true);
                Toast.makeText(getContext(), "즐겨찾기 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e("FavoritePlacesFragment", "Error fetching favorites from Firestore", e);
            }
        });
    }

    private void fetchPropertyDetailsFromApi(List<Favorite> favorites) {
        List<PropertyListItem> favoriteProperties = new ArrayList<>(favorites.size());
        for (int i = 0; i < favorites.size(); i++) {
            favoriteProperties.add(null);
        }

        final int[] completedCalls = {0};

        for (int i = 0; i < favorites.size(); i++) {
            Favorite favorite = favorites.get(i);
            final int index = i;

            Call<PropertySingleResponse> call = getApiCall(favorite.getType(), favorite.getListing_id());

            if (call != null) {
                call.enqueue(new Callback<PropertySingleResponse>() {
                    @Override
                    public void onResponse(Call<PropertySingleResponse> call, Response<PropertySingleResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {

                            // 1. PropertyItem 객체를 가져옵니다.
                            PropertyItem propertyItem = response.body().getData().get(0);

                            // 2. PropertyItem을 PropertyListItem으로 변환합니다. (✨수정된 핵심 부분: Setter 사용✨)
                            PropertyListItem listItem = new PropertyListItem();

                            // PropertyItem의 데이터를 PropertyListItem의 Setter를 이용해 복사합니다.
                            listItem.setListingId(propertyItem.getListingId());
                            listItem.setPropertyType(propertyItem.getPropertyType());
                            listItem.setDeposit(propertyItem.getDeposit());
                            listItem.setMonthlyRent(propertyItem.getMonthlyRent());
                            // PropertyListItem에 정의된 모든 필드를 복사합니다.
                            listItem.setGrossArea(propertyItem.getGrossArea());
                            listItem.setNetArea(propertyItem.getNetArea());
                            listItem.setFloor(propertyItem.getFloor());
                            listItem.setTotalFloors(propertyItem.getTotalFloors());
                            listItem.setDirection(propertyItem.getDirection());
                            listItem.setPropertyFeatures(propertyItem.getPropertyFeatures());
                            listItem.setImageUrl(propertyItem.getImageUrl());

                            // 3. 변환된 PropertyListItem을 리스트에 저장합니다.
                            favoriteProperties.set(index, listItem);
                        } else {
                            Log.w("FavoritePlacesFragment", "API 응답 실패 또는 데이터 없음: " + favorite.getType() + " - " + favorite.getListing_id());
                        }

                        completedCalls[0]++;
                        if (completedCalls[0] == favorites.size()) {
                            favoriteProperties.removeIf(item -> item == null);
                            updateUI(favoriteProperties);
                        }
                    }

                    @Override
                    public void onFailure(Call<PropertySingleResponse> call, Throwable t) {
                        Log.e("FavoritePlacesFragment", "API 호출 실패: " + favorite.getType() + " - " + favorite.getListing_id(), t);

                        completedCalls[0]++;
                        if (completedCalls[0] == favorites.size()) {
                            favoriteProperties.removeIf(item -> item == null);
                            updateUI(favoriteProperties);
                        }
                    }
                });
            } else {
                Log.w("FavoritePlacesFragment", "지원하지 않는 매물 유형: " + favorite.getType());
                completedCalls[0]++;
                if (completedCalls[0] == favorites.size()) {
                    favoriteProperties.removeIf(item -> item == null);
                    updateUI(favoriteProperties);
                }
            }
        }
    }

    private Call<PropertySingleResponse> getApiCall(String type, String listingId) {
        PropertyRequest request = new PropertyRequest(type, listingId);
        switch (type) {
            case "원룸":
                return apiService.getOneRoomPropertyDetail(request);
            case "빌라":
                return apiService.getVillaPropertyDetail(request);
            case "아파트":
                return apiService.getApartmentPropertyDetail(request);
            case "오피스텔":
                return apiService.getOfficetelPropertyDetail(request);
            case "단독/다가구":
                return apiService.getDetachedMultiPropertyDetail(request);
            default:
                return null;
        }
    }

    private void updateUI(List<PropertyListItem> properties) {
        showLoading(false);
        if (properties.isEmpty()) {
            showEmptyView(true);
        } else {
            showEmptyView(false);
            adapter.setFavoriteProperties(properties);
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
    }

    private void showEmptyView(boolean show) {
        emptyTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void navigateToPropertyDetails(PropertyListItem item) {
        Intent intent = new Intent(getContext(), PropertyDetailsActivity.class);
        intent.putExtra("property_item", (Serializable) item);
        startActivity(intent);
    }
}