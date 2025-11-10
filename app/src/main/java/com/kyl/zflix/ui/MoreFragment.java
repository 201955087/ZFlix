package com.kyl.zflix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kyl.zflix.R;
import com.kyl.zflix.ui.LoginActivity;

public class MoreFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView tvNickname;
    private TextView tvLoginStatus; // 뷰 변수 추가

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        mAuth = FirebaseAuth.getInstance();
        tvNickname = view.findViewById(R.id.tvNickname);
        tvLoginStatus = view.findViewById(R.id.tvLoginStatus); // 뷰 초기화

        // 🔹 상단 버튼
        ImageButton btnAlarm = view.findViewById(R.id.btnAlarm);
        ImageButton btnSetting = view.findViewById(R.id.btnsettings);

        btnAlarm.setOnClickListener(v -> openFragment(new AlarmFragment()));
        btnSetting.setOnClickListener(v -> openFragment(new SettingFragment()));

        // 🔹 프로필 영역 (로그인 상태에 따라 동작 변경)
        LinearLayout btnProfileArea = view.findViewById(R.id.btnProfileArea);
        updateProfileArea(); // 초기 상태 설정

        btnProfileArea.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // 로그인 상태: 프로필 화면으로 이동
                openFragment(new ProfileFragment());
            } else {
                // 로그인 안 된 상태: 로그인 화면으로 이동
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 🔹 바로가기 메뉴 (2분할)
        LinearLayout btnNote = view.findViewById(R.id.btnNote);
        LinearLayout btnStory = view.findViewById(R.id.btnStory);

        btnNote.setOnClickListener(v -> openFragment(new NoteFragment()));
        btnStory.setOnClickListener(v -> openFragment(new StoryFragment()));

        // 🔹 리스트 바로가기 메뉴
        LinearLayout btnEvent = view.findViewById(R.id.btnEvent);
        LinearLayout btnNews = view.findViewById(R.id.btnNews);
        LinearLayout btnSupport = view.findViewById(R.id.btnSupport);
        LinearLayout btnReport = view.findViewById(R.id.btnReport);

        btnEvent.setOnClickListener(v -> openFragment(new EventFragment()));
        btnNews.setOnClickListener(v -> openFragment(new NewsFragment()));
        btnSupport.setOnClickListener(v -> openFragment(new SupportFragment()));
        btnReport.setOnClickListener(v -> openFragment(new ReportFragment()));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProfileArea();
    }

    /**
     * 로그인 상태에 따라 프로필 영역 텍스트와 뷰를 업데이트하는 메서드
     */
    private void updateProfileArea() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // 로그인 상태일 때
            String displayName = currentUser.getDisplayName();
            String userIdentifier;

            if (displayName != null && !displayName.isEmpty()) {
                userIdentifier = displayName + "님";
            } else {
                userIdentifier = currentUser.getEmail();
            }
            tvNickname.setText(userIdentifier);
            tvLoginStatus.setVisibility(View.GONE); // 뷰 숨기기
        } else {
            // 로그아웃 상태일 때
            tvNickname.setText("로그인을 해주세요");
            tvLoginStatus.setVisibility(View.VISIBLE); // 뷰 보이게 하기
        }
    }

    /**
     * 프래그먼트 교체를 처리하는 공통 메서드
     * @param fragment 전환할 프래그먼트 객체
     */
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}