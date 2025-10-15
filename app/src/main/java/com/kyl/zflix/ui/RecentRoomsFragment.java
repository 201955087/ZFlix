package com.kyl.zflix.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kyl.zflix.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import com.kyl.zflix.model.RoomItem;

public class RecentRoomsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<RoomItem> roomList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_rooms, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 샘플 데이터
        roomList = new ArrayList<>();
        roomList.add(new RoomItem("월세 1000/100", "오피스텔 • 59.19m²", "비공개", "https://example.com/img1.jpg"));
        roomList.add(new RoomItem("월세 200/36", "원룸 • 16.52m²", "삭제", "https://example.com/img2.jpg"));
        roomList.add(new RoomItem("월세 300/38", "원룸 • 19.83m²", "비공개", "https://example.com/img3.jpg"));
        roomList.add(new RoomItem("전세 3억6000", "아파트 • 111.71m²", "거래완료", "https://example.com/img4.jpg"));

        adapter = new RoomAdapter(roomList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
