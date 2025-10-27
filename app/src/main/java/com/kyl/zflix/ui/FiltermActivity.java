package com.kyl.zflix.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu; // 🌟 추가: Menu 클래스 임포트
import android.view.MenuItem; // 🌟 추가: MenuItem 클래스 임포트
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.kyl.zflix.R;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.slider.RangeSlider;

public class FiltermActivity extends AppCompatActivity {

    private static final String TAG = "FiltermActivity";

    // 뷰 변수 선언
    private Spinner spinnerDistrict, spinnerLegalDong;
    private TextView tvDepositRange, tvMonthlyRentRange, tvNetAreaRange;
    private RangeSlider sliderDeposit, sliderMonthlyRent, sliderNetArea;
    private LinearLayout interiorFacilitiesContainer;
    private Button btnApplyFilter;
    // private Button btnResetFilter; // 하단 초기화 버튼 변수 제거됨

    // 현재 필터 상태를 저장할 변수
    private String currentDistrict, currentLegalDong;
    private String currentDepositMin, currentDepositMax;
    private String currentMonthlyMin, currentMonthlyMax;
    private String currentNetMin, currentNetMax;
    private List<String> currentFacilities;

    // 필터링 가능한 내부 시설 목록 (예시)
    private static final List<String> INTERIOR_FACILITIES = Arrays.asList(
            "냉장고", "세탁기", "에어컨", "인덕션레인지", "전자레인지"
    );

    // RangeSlider 최대값 설정
    private static final float MAX_DEPOSIT = 50000.0f; // 만원 단위
    private static final float MAX_MONTHLY_RENT = 500.0f; // 만원 단위
    private static final float MAX_NET_AREA = 200.0f; // m² 단위

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_m);

        //  추가: Intent에서 필터값 읽기
        loadCurrentFilterData();

        initViews();
        setupToolbar();
        // 수정: 스피너 초기화 시 저장된 필터 값 반영
        setupSpinners();
        //  수정: RangeSlider 초기화 시 저장된 필터 값 반영
        setupRangeSlidersWithCurrentValues();
        //  수정: Checkbox 초기화 시 저장된 필터 값 반영
        createFacilityCheckboxesWithCurrentValues();

        btnApplyFilter.setOnClickListener(v -> applyFilter());
        // 하단 초기화 버튼 리스너 제거됨
    }

    private void initViews() {
        spinnerDistrict = findViewById(R.id.spinner_district);
        spinnerLegalDong = findViewById(R.id.spinner_legal_dong);
        tvDepositRange = findViewById(R.id.tv_deposit_range);
        sliderDeposit = findViewById(R.id.slider_deposit);
        tvMonthlyRentRange = findViewById(R.id.tv_monthly_rent_range);
        sliderMonthlyRent = findViewById(R.id.slider_monthly_rent);
        tvNetAreaRange = findViewById(R.id.tv_net_area_range);
        sliderNetArea = findViewById(R.id.slider_net_area);
        interiorFacilitiesContainer = findViewById(R.id.interior_facilities_checkbox_container);
        btnApplyFilter = findViewById(R.id.btn_apply_filter);
        // 하단 초기화 버튼 findViewById 제거됨
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // 툴바 NavigationClickListener 제거됨 (onOptionsItemSelected에서 처리)
    }

    //  툴바에 메뉴(초기화 버튼) 로드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    //  툴바 메뉴 클릭 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            resetFilter(); // 초기화 버튼 클릭 시 resetFilter 호출
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish(); // 뒤로가기 화살표 클릭 처리
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //  새 메서드: Intent에서 현재 필터 값을 로드합니다.
    private void loadCurrentFilterData() {
        Intent intent = getIntent();
        if (intent != null) {
            currentDistrict = intent.getStringExtra("current_district");
            currentLegalDong = intent.getStringExtra("current_legal_dong");
            currentDepositMin = intent.getStringExtra("current_deposit_min");
            currentDepositMax = intent.getStringExtra("current_deposit_max");
            currentMonthlyMin = intent.getStringExtra("current_monthly_min");
            currentMonthlyMax = intent.getStringExtra("current_monthly_max");
            currentNetMin = intent.getStringExtra("current_net_min");
            currentNetMax = intent.getStringExtra("current_net_max");
            currentFacilities = intent.getStringArrayListExtra("current_facilities");
        }
    }

    //  수정된 setupSpinners: 현재 필터 값으로 Spinner 선택
    private void setupSpinners() {
        List<String> districts = Arrays.asList("마포구");
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);

        //  현재 District 값으로 스피너 초기 선택 설정 (마포구 하나이므로 생략 가능하나 로직 유지)
        if (currentDistrict != null) {
            int position = districts.indexOf(currentDistrict);
            if (position >= 0) {
                spinnerDistrict.setSelection(position);
            }
        }

        List<String> legalDongs = Arrays.asList(
                "전체","공덕동", "신공덕동", "아현동", "도화동", "마포동", "용강동",
                "토정동", "하중동", "대흥동", "염리동", "신수동", "현석동", "구수동",
                "상수동", "하수동", "당인동", "창전동", "서교동", "동교동", "노고산동",
                "합정동", "망원동", "연남동", "성산동", "중동", "상암동"
        );
        ArrayAdapter<String> legalDongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, legalDongs);
        legalDongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLegalDong.setAdapter(legalDongAdapter);

        //  현재 LegalDong 값으로 스피너 초기 선택 설정
        if (currentLegalDong != null) {
            int position = legalDongs.indexOf(currentLegalDong);
            if (position >= 0) {
                spinnerLegalDong.setSelection(position);
            }
        }
    }

    //  새 메서드: RangeSlider 초기 값 설정 및 리스너 등록
    private void setupSlider(RangeSlider slider, TextView textView, float maxValue, String unit, String minStr, String maxStr) {
        float minVal = 0.0f;
        float maxVal = maxValue;

        try {
            if (minStr != null && !minStr.equals("null")) minVal = Float.parseFloat(minStr);
            if (maxStr != null && !maxStr.equals("null")) maxVal = Float.parseFloat(maxStr);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing slider values: " + e.getMessage());
        }

        List<Float> values = new ArrayList<>();
        values.add(minVal);
        values.add(maxVal);
        slider.setValues(values);

        updateRangeTextView(textView, minVal, maxVal, unit);

        slider.addOnChangeListener((s, value, fromUser) ->
                updateRangeTextView(textView, s.getValues().get(0), s.getValues().get(1), unit));
    }

    //  수정된 setupRangeSliders
    private void setupRangeSlidersWithCurrentValues() {
        sliderDeposit.setValueTo(MAX_DEPOSIT);
        sliderMonthlyRent.setValueTo(MAX_MONTHLY_RENT);
        sliderNetArea.setValueTo(MAX_NET_AREA);

        setupSlider(sliderDeposit, tvDepositRange, MAX_DEPOSIT, "만원", currentDepositMin, currentDepositMax);
        setupSlider(sliderMonthlyRent, tvMonthlyRentRange, MAX_MONTHLY_RENT, "만원", currentMonthlyMin, currentMonthlyMax);
        setupSlider(sliderNetArea, tvNetAreaRange, MAX_NET_AREA, "m²", currentNetMin, currentNetMax);
    }

    private void updateRangeTextView(TextView textView, float minVal, float maxVal, String unit) {
        String minText, maxText;
        float maxValue = (textView.getId() == R.id.tv_deposit_range) ? MAX_DEPOSIT :
                (textView.getId() == R.id.tv_monthly_rent_range) ? MAX_MONTHLY_RENT : MAX_NET_AREA;

        boolean isArea = textView.getId() == R.id.tv_net_area_range;

        // 보증금은 정수, 전용면적은 소수점 첫째 자리까지 표시
        minText = (minVal == 0.0f) ? "최소" :
                isArea ? String.format(Locale.getDefault(), "%,.1f%s", minVal, unit) :
                        String.format(Locale.getDefault(), "%,.0f%s", minVal, unit);

        maxText = (maxVal == maxValue) ? "최대" :
                isArea ? String.format(Locale.getDefault(), "%,.1f%s", maxVal, unit) :
                        String.format(Locale.getDefault(), "%,.0f%s", maxVal, unit);

        textView.setText(String.format("%s ~ %s", minText, maxText));
    }

    private GradientDrawable getButtonBackground(boolean isSelected) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(30);

        if (isSelected) {
            int colorPrimary = getResources().getColor(R.color.black, getTheme());
            drawable.setColor(colorPrimary);
            drawable.setStroke(0, 0);
        } else {
            drawable.setColor(Color.WHITE);
            drawable.setStroke(2, getResources().getColor(R.color.grey_light, getTheme()));
        }
        return drawable;
    }

    //  수정된 createFacilityCheckboxes: 현재 필터 값으로 CheckBox 초기 선택 설정
    private void createFacilityCheckboxesWithCurrentValues() {
        // 초기화를 위해 컨테이너의 모든 뷰를 제거합니다. (resetFilter 대비)
        interiorFacilitiesContainer.removeAllViews();

        LinearLayout currentLine = new LinearLayout(this);
        currentLine.setOrientation(LinearLayout.HORIZONTAL);
        currentLine.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        interiorFacilitiesContainer.addView(currentLine);

        LinearLayout.LayoutParams cbParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

        for (int i = 0; i < INTERIOR_FACILITIES.size(); i++) {
            String facility = INTERIOR_FACILITIES.get(i);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(facility);
            checkBox.setLayoutParams(cbParams);
            checkBox.setTextColor(getResources().getColor(R.color.black_light, getTheme()));

            // 현재 선택된 시설 목록에 이 시설이 포함되어 있으면 체크
            if (currentFacilities != null && currentFacilities.contains(facility)) {
                checkBox.setChecked(true);
            }

            currentLine.addView(checkBox);

            if ((i + 1) % 3 == 0 && i < INTERIOR_FACILITIES.size() - 1) {
                currentLine = new LinearLayout(this);
                currentLine.setOrientation(LinearLayout.HORIZONTAL);
                currentLine.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                interiorFacilitiesContainer.addView(currentLine);
            }
        }
    }

    // 이 메서드의 이름은 이전 버전과 호환성을 위해 createFacilityCheckboxes로 남겨둡니다.
    private void createFacilityCheckboxes() {
        createFacilityCheckboxesWithCurrentValues();
    }

    //  수정: 필터 초기화 메서드 - applyFilter() 호출 제거
    private void resetFilter() {
        // 1. 스피너 초기화 (법정동: 전체)
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> legalDongAdapter = (ArrayAdapter<String>) spinnerLegalDong.getAdapter();

        int position = legalDongAdapter.getPosition("전체");

        if (position >= 0) {
            spinnerLegalDong.setSelection(position);
        }

        // 2. RangeSlider 초기화 (최소~최대)
        List<Float> defaultDeposit = Arrays.asList(0.0f, MAX_DEPOSIT);
        sliderDeposit.setValues(defaultDeposit);
        updateRangeTextView(tvDepositRange, defaultDeposit.get(0), defaultDeposit.get(1), "만원");

        List<Float> defaultMonthly = Arrays.asList(0.0f, MAX_MONTHLY_RENT);
        sliderMonthlyRent.setValues(defaultMonthly);
        updateRangeTextView(tvMonthlyRentRange, defaultMonthly.get(0), defaultMonthly.get(1), "만원");

        List<Float> defaultNetArea = Arrays.asList(0.0f, MAX_NET_AREA);
        sliderNetArea.setValues(defaultNetArea);
        updateRangeTextView(tvNetAreaRange, defaultNetArea.get(0), defaultNetArea.get(1), "m²");

        // 3. 내부 시설 Checkbox 초기화 (모두 체크 해제)
        for (int i = 0; i < interiorFacilitiesContainer.getChildCount(); i++) {
            View row = interiorFacilitiesContainer.getChildAt(i);
            if (row instanceof LinearLayout) {
                LinearLayout rowLayout = (LinearLayout) row;
                for (int j = 0; j < rowLayout.getChildCount(); j++) {
                    View view = rowLayout.getChildAt(j);
                    if (view instanceof CheckBox) {
                        ((CheckBox) view).setChecked(false);
                    }
                }
            }
        }

    }


    private void applyFilter() {
        String district = spinnerDistrict.getSelectedItem().toString();
        String legalDong = spinnerLegalDong.getSelectedItem().toString();

        String depositMin = String.valueOf(sliderDeposit.getValues().get(0).intValue());
        String depositMax = String.valueOf(sliderDeposit.getValues().get(1).intValue());

        // 월세는 정수형으로 변환
        String monthlyRentMin = String.valueOf(sliderMonthlyRent.getValues().get(0).intValue());
        String monthlyRentMax = String.valueOf(sliderMonthlyRent.getValues().get(1).intValue());

        // 전용면적은 소수점까지 유지
        String netAreaMin = String.valueOf(sliderNetArea.getValues().get(0));
        String netAreaMax = String.valueOf(sliderNetArea.getValues().get(1));

        ArrayList<String> selectedFacilities = new ArrayList<>();
        for (int i = 0; i < interiorFacilitiesContainer.getChildCount(); i++) {
            View row = interiorFacilitiesContainer.getChildAt(i);
            if (row instanceof LinearLayout) {
                LinearLayout rowLayout = (LinearLayout) row;
                for (int j = 0; j < rowLayout.getChildCount(); j++) {
                    View view = rowLayout.getChildAt(j);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        if (checkBox.isChecked()) {
                            selectedFacilities.add(checkBox.getText().toString());
                        }
                    }
                }
            }
        }

        Intent resultIntent = new Intent();

        // 선택된 값이 기본값(전체, 0)과 같으면 null을 반환하여 필터링하지 않도록 합니다.
        resultIntent.putExtra("district", district.equals("전체") || district.equals("마포구") ? null : district);
        resultIntent.putExtra("legal_dong", legalDong.equals("전체") ? null : legalDong);

        resultIntent.putExtra("deposit_min", depositMin.equals("0") ? null : depositMin);
        resultIntent.putExtra("deposit_max", depositMax.equals(String.valueOf((int)MAX_DEPOSIT)) ? null : depositMax);

        resultIntent.putExtra("monthly_min", monthlyRentMin.equals("0") ? null : monthlyRentMin);
        // 최대 월세가 최대값과 같으면 null이 아닌 최대값 자체를 보냅니다. (PropertyActivity에서 Integer.parseInt를 시도하기 때문)
        resultIntent.putExtra("monthly_max", monthlyRentMax.equals(String.valueOf((int)MAX_MONTHLY_RENT))
                ? String.valueOf((int)MAX_MONTHLY_RENT) : monthlyRentMax);

        resultIntent.putExtra("net_min", netAreaMin.equals("0.0") ? null : netAreaMin);
        resultIntent.putExtra("net_max", netAreaMax.equals(String.valueOf(MAX_NET_AREA)) ? null : netAreaMax);

        //  사용승인일 필터 항목 추가 시 여기에 putExtra 추가 필요 (현재 XML에는 있으나 로직 없음)
        resultIntent.putExtra("approval_date_limit_years", (String) null);

        resultIntent.putStringArrayListExtra("interior_facilities_list",
                selectedFacilities.isEmpty() ? null : selectedFacilities);

        Log.d(TAG, "=== 필터 결과 ===");
        Log.d(TAG, "deposit_min=" + depositMin + ", deposit_max=" + depositMax);
        Log.d(TAG, "monthly_min=" + monthlyRentMin + ", monthly_max=" + monthlyRentMax);
        Log.d(TAG, "net_min=" + netAreaMin + ", net_max=" + netAreaMax);
        Log.d(TAG, "selectedFacilities=" + selectedFacilities.toString());

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}