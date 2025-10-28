package com.kyl.zflix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kyl.zflix.R;

public class SettingFragment extends Fragment {
    
    private Switch switchPushNotification;
    private Switch switchPropertyUpdate;
    private LinearLayout btnPrivacy;
    private LinearLayout btnTerms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // 뒤로가기 버튼
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // 스위치 초기화
        switchPushNotification = view.findViewById(R.id.switch_push_notification);
        switchPropertyUpdate = view.findViewById(R.id.switch_property_update);

        // 스위치 리스너
        switchPushNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), 
                isChecked ? "푸시 알림이 활성화되었습니다." : "푸시 알림이 비활성화되었습니다.", 
                Toast.LENGTH_SHORT).show();
        });

        switchPropertyUpdate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), 
                isChecked ? "매물 업데이트 알림이 활성화되었습니다." : "매물 업데이트 알림이 비활성화되었습니다.", 
                Toast.LENGTH_SHORT).show();
        });

        // 개인정보 처리방침
        btnPrivacy = view.findViewById(R.id.btn_privacy);
        btnPrivacy.setOnClickListener(v -> {
            Toast.makeText(getContext(), "개인정보 처리방침 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 개인정보 처리방침 웹페이지 또는 별도 화면으로 이동
        });

        // 이용약관
        btnTerms = view.findViewById(R.id.btn_terms);
        btnTerms.setOnClickListener(v -> {
            Toast.makeText(getContext(), "이용약관 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 이용약관 웹페이지 또는 별도 화면으로 이동
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 설정 화면 진입 시 하단바 숨김
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 설정 화면 빠져나갈 때 하단바 다시 표시
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
