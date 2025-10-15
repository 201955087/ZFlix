package com.kyl.zflix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.kyl.zflix.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.kyl.zflix.PropertyActivity;

public class HomeFragment extends Fragment {

    public HomeFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 버튼 참조
        Button btnHouseDetached = view.findViewById(R.id.btn_house_detached);
        Button btnVilla       = view.findViewById(R.id.btn_villa);
        Button btnApartment   = view.findViewById(R.id.btn_apartment);
        Button btnOfficetel   = view.findViewById(R.id.btn_officetel);
        Button btnOneRoom     = view.findViewById(R.id.btn_one_room);

        // 클릭 시 PropertyActivity로 이동 + "type" 값 전달
        btnHouseDetached.setOnClickListener(v -> openProperty("단독/다가구"));
        btnVilla.setOnClickListener(v -> openProperty("빌라"));
        btnApartment.setOnClickListener(v -> openProperty("아파트"));
        btnOfficetel.setOnClickListener(v -> openProperty("오피스텔"));
        btnOneRoom.setOnClickListener(v -> openProperty("원룸"));

        return view;
    }

    private void openProperty(String type) {
        Intent intent = new Intent(requireActivity(), PropertyActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}