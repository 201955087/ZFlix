package com.kyl.zflix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.kyl.zflix.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class NewsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // 뒤로가기 버튼 클릭 -> fragment_more 이동
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MoreFragment()); // MainActivity의 FrameLayout id
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // 지난 소식 보기 클릭 -> fragment_all_news 이동
        TextView btnAllNews = view.findViewById(R.id.btn_all_news);
        btnAllNews.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new AllNewsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // NewsFragment 진입 시 하단바 숨김
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // NewsFragment 빠져나갈 때 하단바 다시 표시
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
