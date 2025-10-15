package com.kyl.zflix.ui;

import com.kyl.zflix.model.AlarmItem;
import com.kyl.zflix.adapter.AlarmAdapter;
import com.kyl.zflix.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlarmFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
    private List<AlarmItem> alarmList;
    private ImageButton btnBack;
    private TextView btnReadAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

        recyclerView = root.findViewById(R.id.recycler_alarm);
        btnBack = root.findViewById(R.id.btn_back);
        btnReadAll = root.findViewById(R.id.btn_read_all);

        // 샘플 데이터
        alarmList = new ArrayList<>();
        alarmList.add(new AlarmItem("새로운 집들을 구경해보세요.", "09/14 10:53", false));
        alarmList.add(new AlarmItem("새로운 기능이 추가되었습니다", "09/13 13:48", false));

        adapter = new AlarmAdapter(requireContext(), alarmList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // 뒤로가기 버튼 → Fragment라서 액티비티 스택에서 제거
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // 모두읽음 버튼
        btnReadAll.setOnClickListener(v -> adapter.markAllAsRead());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 메인 액티비티의 하단바 숨기기
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Fragment 나갈 때 하단바 다시 보이기
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
