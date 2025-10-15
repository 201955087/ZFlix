package com.kyl.zflix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kyl.zflix.adapter.FavoritesPagerAdapter;
import com.kyl.zflix.R;

public class FavoritesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);

        // 어댑터 연결
        FavoritesPagerAdapter adapter = new FavoritesPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // 탭 이름 설정
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("최근 본 방");
                            break;
                        case 1:
                            tab.setText("찜한 방");
                            break;
                        case 2:
                            tab.setText("즐겨찾기");
                            break;
                    }
                }).attach();

        return view;
    }
}
