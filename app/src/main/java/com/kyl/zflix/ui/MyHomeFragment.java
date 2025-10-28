package com.kyl.zflix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kyl.zflix.R;

public class MyHomeFragment extends Fragment {
    
    private Button btnRegisterHome;
    private ImageButton btnAddHome;
    private LinearLayout homeItem1;
    private LinearLayout homeItem2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_home, container, false);

        // 뒤로가기 버튼
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // 집 등록 버튼들
        btnRegisterHome = view.findViewById(R.id.btn_register_home);
        btnAddHome = view.findViewById(R.id.btn_add_home);

        btnRegisterHome.setOnClickListener(v -> {
            Toast.makeText(getContext(), "집 등록 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 집 등록 화면으로 이동
        });

        btnAddHome.setOnClickListener(v -> {
            Toast.makeText(getContext(), "새 집 추가 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 새 집 추가 화면으로 이동
        });

        // 집 목록 아이템들
        homeItem1 = view.findViewById(R.id.home_item_1);
        homeItem2 = view.findViewById(R.id.home_item_2);

        homeItem1.setOnClickListener(v -> {
            Toast.makeText(getContext(), "서울시 강남구 아파트 상세보기", Toast.LENGTH_SHORT).show();
            // TODO: 집 상세보기 화면으로 이동
        });

        homeItem2.setOnClickListener(v -> {
            Toast.makeText(getContext(), "경기도 성남시 단독주택 상세보기", Toast.LENGTH_SHORT).show();
            // TODO: 집 상세보기 화면으로 이동
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 우리집 화면 진입 시 하단바 숨김
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 우리집 화면 빠져나갈 때 하단바 다시 표시
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
