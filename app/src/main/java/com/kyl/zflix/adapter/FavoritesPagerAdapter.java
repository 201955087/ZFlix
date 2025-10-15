package com.kyl.zflix.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.kyl.zflix.ui.RecentRoomsFragment;
import com.kyl.zflix.ui.FavoritePlacesFragment;
import com.kyl.zflix.ui.WishRoomsFragment;

public class FavoritesPagerAdapter extends FragmentStateAdapter {

    public FavoritesPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RecentRoomsFragment();   // 최근 본 방 Fragment
            case 1:
                return new WishRoomsFragment();     // 찜한 방 Fragment
            case 2:
                return new FavoritePlacesFragment(); // 즐겨찾기 Fragment
            default:
                return new RecentRoomsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
