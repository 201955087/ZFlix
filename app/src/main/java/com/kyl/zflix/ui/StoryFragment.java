package com.kyl.zflix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kyl.zflix.R;

import java.util.ArrayList;
import java.util.List;

public class StoryFragment extends Fragment {
    
    private RecyclerView recyclerStories;
    private LinearLayout layoutEmpty;
    private Button btnWriteFirstStory;
    private ImageButton btnWriteStory;
    private TextView tabRecent;
    private TextView tabPopular;
    private List<String> storyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        // 뒤로가기 버튼
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // RecyclerView 초기화
        recyclerStories = view.findViewById(R.id.recycler_stories);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        btnWriteFirstStory = view.findViewById(R.id.btn_write_first_story);
        btnWriteStory = view.findViewById(R.id.btn_write_story);

        // 탭 메뉴
        tabRecent = view.findViewById(R.id.tab_recent);
        tabPopular = view.findViewById(R.id.tab_popular);

        // 샘플 데이터
        storyList = new ArrayList<>();
        storyList.add("강남구 아파트 후기 - 정말 좋은 곳이에요!");
        storyList.add("성남시 단독주택 체험기");
        storyList.add("부산 해운대 전망 좋은 집");

        // RecyclerView 설정
        recyclerStories.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: StoryAdapter 구현 필요
        // recyclerStories.setAdapter(new StoryAdapter(storyList));

        // 글쓰기 버튼들
        btnWriteFirstStory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "글쓰기 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 글쓰기 화면으로 이동
        });

        btnWriteStory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "새 글 작성 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 새 글 작성 화면으로 이동
        });

        // 탭 클릭 리스너
        tabRecent.setOnClickListener(v -> {
            tabRecent.setBackgroundResource(R.drawable.bg_tab_selected);
            tabRecent.setTextColor(getResources().getColor(R.color.primary));
            tabPopular.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabPopular.setTextColor(getResources().getColor(R.color.text_secondary));
            Toast.makeText(getContext(), "최신순으로 정렬", Toast.LENGTH_SHORT).show();
        });

        tabPopular.setOnClickListener(v -> {
            tabPopular.setBackgroundResource(R.drawable.bg_tab_selected);
            tabPopular.setTextColor(getResources().getColor(R.color.primary));
            tabRecent.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabRecent.setTextColor(getResources().getColor(R.color.text_secondary));
            Toast.makeText(getContext(), "인기순으로 정렬", Toast.LENGTH_SHORT).show();
        });

        // 빈 상태 처리
        if (storyList.isEmpty()) {
            recyclerStories.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerStories.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 커뮤니티 화면 진입 시 하단바 숨김
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 커뮤니티 화면 빠져나갈 때 하단바 다시 표시
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
