package com.kyl.zflix;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kyl.zflix.ui.FavoritesFragment;
import com.kyl.zflix.ui.HomeFragment;
import com.kyl.zflix.ui.MoreFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 앱 첫 실행 시 기본 화면은 홈
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        // BottomNavigation 아이템 선택 시 화면 전환
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selected = null;

            int id = item.getItemId();
            if (id == R.id.nav_home) {
                selected = new HomeFragment();
            } else if (id == R.id.nav_favorites) {
                selected = new FavoritesFragment();
            } else if (id == R.id.nav_more) {
                selected = new MoreFragment();
            }

            if (selected != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selected)
                        .commit();
            }
            return true;
        });

        // 인텐트로 받은 화면 이동 신호
        String navigateTo = getIntent().getStringExtra("navigateTo");
        if ("home".equals(navigateTo)) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else if ("favorites".equals(navigateTo)) {
            bottomNavigationView.setSelectedItemId(R.id.nav_favorites);
        } else if ("more".equals(navigateTo)) {
            bottomNavigationView.setSelectedItemId(R.id.nav_more);
        }
    }
}
