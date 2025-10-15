package com.kyl.zflix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kyl.zflix.R;
import androidx.appcompat.widget.Toolbar;

public class FilterActivity extends AppCompatActivity {

    private Spinner spinnerDistrict, spinnerLegalDong;
    private EditText etDepositMin, etDepositMax, etMonthlyRentMin, etMonthlyRentMax;
    private LinearLayout interiorFacilitiesContainer;
    private Button btnApplyFilter;

    // 필터링 가능한 내부 시설 목록 (예시)
    private static final List<String> INTERIOR_FACILITIES = Arrays.asList(
            "냉장고", "세탁기", "에어컨", "인덕션레인지", "전자레인지", "책상", "옷장"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initViews();
        setupSpinners();
        createFacilityCheckboxes();

        // 🔹 Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 🔹 뒤로가기 버튼 클릭 → 그냥 Activity 닫기
        toolbar.setNavigationOnClickListener(v -> finish());

        btnApplyFilter.setOnClickListener(v -> applyFilter());
    }

    private void initViews() {
        spinnerDistrict = findViewById(R.id.spinner_district);
        spinnerLegalDong = findViewById(R.id.spinner_legal_dong);
        etDepositMin = findViewById(R.id.et_deposit_min);
        etDepositMax = findViewById(R.id.et_deposit_max);
        etMonthlyRentMin = findViewById(R.id.et_monthly_rent_min);
        etMonthlyRentMax = findViewById(R.id.et_monthly_rent_max);
        interiorFacilitiesContainer = findViewById(R.id.interior_facilities_checkbox_container);
        btnApplyFilter = findViewById(R.id.btn_apply_filter);
    }

    private void setupSpinners() {
        // 지역구 스피너 설정 (예시 데이터)
        List<String> districts = Arrays.asList("전체", "마포구", "용산구", "강남구");
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);

        // 법정동 스피너 설정 (예시 데이터)
        List<String> legalDongs = Arrays.asList("전체", "신수동", "이태원동", "청담동");
        ArrayAdapter<String> legalDongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, legalDongs);
        legalDongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLegalDong.setAdapter(legalDongAdapter);
    }

    private void createFacilityCheckboxes() {
        for (String facility : INTERIOR_FACILITIES) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(facility);
            interiorFacilitiesContainer.addView(checkBox);
        }
    }

    private void applyFilter() {
        String district = spinnerDistrict.getSelectedItem().toString();
        String legalDong = spinnerLegalDong.getSelectedItem().toString();
        String depositMin = etDepositMin.getText().toString();
        String depositMax = etDepositMax.getText().toString();
        String monthlyRentMin = etMonthlyRentMin.getText().toString();
        String monthlyRentMax = etMonthlyRentMax.getText().toString();

        List<String> selectedFacilities = new ArrayList<>();
        for (int i = 0; i < interiorFacilitiesContainer.getChildCount(); i++) {
            View view = interiorFacilitiesContainer.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    selectedFacilities.add(checkBox.getText().toString());
                }
            }
        }

        // 결과 Intent 생성 및 데이터 담기
        Intent resultIntent = new Intent();
        resultIntent.putExtra("district", district.equals("전체") ? null : district);
        resultIntent.putExtra("legalDong", legalDong.equals("전체") ? null : legalDong);
        resultIntent.putExtra("depositMin", depositMin.isEmpty() ? null : depositMin);
        resultIntent.putExtra("depositMax", depositMax.isEmpty() ? null : depositMax);
        resultIntent.putExtra("monthlyRentMin", monthlyRentMin.isEmpty() ? null : monthlyRentMin);
        resultIntent.putExtra("monthlyRentMax", monthlyRentMax.isEmpty() ? null : monthlyRentMax);
        resultIntent.putStringArrayListExtra("interiorFacilities", new ArrayList<>(selectedFacilities));

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
