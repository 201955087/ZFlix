package com.kyl.zflix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kyl.zflix.R;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {
    
    private RecyclerView recyclerNotes;
    private LinearLayout layoutEmpty;
    private Button btnWriteNote;
    private ImageButton btnAddNote;
    private List<String> noteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        // 뒤로가기 버튼
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // RecyclerView 초기화
        recyclerNotes = view.findViewById(R.id.recycler_notes);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        btnWriteNote = view.findViewById(R.id.btn_write_note);
        btnAddNote = view.findViewById(R.id.btn_add_note);

        // 샘플 데이터
        noteList = new ArrayList<>();
        noteList.add("강남구 아파트 - 교통편이 좋고 주변 편의시설 많음");
        noteList.add("성남시 단독주택 - 조용하고 공원이 가까움");
        noteList.add("부산 해운대 - 바다뷰 좋고 관광지 근처");

        // RecyclerView 설정
        recyclerNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: NoteAdapter 구현 필요
        // recyclerNotes.setAdapter(new NoteAdapter(noteList));

        // 노트 작성 버튼들
        btnWriteNote.setOnClickListener(v -> {
            Toast.makeText(getContext(), "노트 작성 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 노트 작성 화면으로 이동
        });

        btnAddNote.setOnClickListener(v -> {
            Toast.makeText(getContext(), "새 노트 추가 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 새 노트 추가 화면으로 이동
        });

        // 빈 상태 처리
        if (noteList.isEmpty()) {
            recyclerNotes.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerNotes.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 매물노트 화면 진입 시 하단바 숨김
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 매물노트 화면 빠져나갈 때 하단바 다시 표시
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
