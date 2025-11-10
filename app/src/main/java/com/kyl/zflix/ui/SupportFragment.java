package com.kyl.zflix.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kyl.zflix.R;

public class SupportFragment extends Fragment {
    
    private LinearLayout faqItem1;
    private LinearLayout faqItem2;
    private LinearLayout faqItem3;
    private LinearLayout btnEmailSupport;
    private LinearLayout btnPhoneSupport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        // 뒤로가기 버튼
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // FAQ 아이템들
        faqItem1 = view.findViewById(R.id.faq_item_1);
        faqItem2 = view.findViewById(R.id.faq_item_2);
        faqItem3 = view.findViewById(R.id.faq_item_3);

        faqItem1.setOnClickListener(v -> {
            Toast.makeText(getContext(), "회원가입 방법 안내", Toast.LENGTH_SHORT).show();
            // TODO: FAQ 상세보기 화면으로 이동
        });

        faqItem2.setOnClickListener(v -> {
            Toast.makeText(getContext(), "매물 등록 방법 안내", Toast.LENGTH_SHORT).show();
            // TODO: FAQ 상세보기 화면으로 이동
        });

        faqItem3.setOnClickListener(v -> {
            Toast.makeText(getContext(), "비밀번호 찾기 안내", Toast.LENGTH_SHORT).show();
            // TODO: FAQ 상세보기 화면으로 이동
        });

        // 문의하기 버튼들
        btnEmailSupport = view.findViewById(R.id.btn_email_support);
        btnPhoneSupport = view.findViewById(R.id.btn_phone_support);

        btnEmailSupport.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@zflix.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "ZFlix 문의사항");
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "이메일 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnPhoneSupport.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:1588-1234"));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "전화 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 고객센터 화면 진입 시 하단바 숨김
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 고객센터 화면 빠져나갈 때 하단바 다시 표시
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
