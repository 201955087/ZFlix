package com.kyl.zflix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.kyl.zflix.MainActivity;
import com.kyl.zflix.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private TextView tvNicknameValue;
    private TextView tvPhoneValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageButton backBtn = view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        tvNicknameValue = view.findViewById(R.id.tv_nickname_value);
        tvPhoneValue = view.findViewById(R.id.tv_phone_value);

        tvNicknameValue.setText("내닉네임");
        tvPhoneValue.setText("010-1234-5678");

        ImageButton changeNicknameBtn = view.findViewById(R.id.btn_change_nickname);
        changeNicknameBtn.setOnClickListener(v -> {
            // 닉네임 변경 로직
        });

        ImageButton verifyPhoneBtn = view.findViewById(R.id.btn_verify_phone);
        verifyPhoneBtn.setOnClickListener(v -> {
            // 본인 인증 로직
        });

        // 로그아웃 버튼 리스너 수정
        Button logoutBtn = view.findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(v -> {
            // Firebase에서 로그아웃
            FirebaseAuth.getInstance().signOut();

            // MoreFragment로 돌아가기 위해 MainActivity로 이동
            Intent intent = new Intent(getActivity(), MainActivity.class);
            // "more" 프래그먼트로 이동하도록 추가 데이터 전달 (필요시)
            intent.putExtra("navigate_to_fragment", "more");
            startActivity(intent);
            // 현재 액티비티를 종료하여 뒤로가기 버튼으로 돌아오지 못하게 할 수 있습니다.
            // getActivity().finish();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}