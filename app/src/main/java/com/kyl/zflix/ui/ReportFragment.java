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

public class ReportFragment extends Fragment {

    private RecyclerView recyclerReports;
    private LinearLayout layoutEmpty;
    private Button btnReportProperty;
    private ImageButton btnNewReport;
    private List<String> reportList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        // 뒤로가기 버튼
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // RecyclerView 초기화
        recyclerReports = view.findViewById(R.id.recycler_reports);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        btnReportProperty = view.findViewById(R.id.btn_report_property);
        btnNewReport = view.findViewById(R.id.btn_new_report);

        // 샘플 데이터
        reportList = new ArrayList<>();
        reportList.add("강남구 아파트 - 가격이 부정확함");
        reportList.add("성남시 단독주택 - 이미 매매완료됨");
        reportList.add("부산 해운대 - 사진과 실제가 다름");

        // RecyclerView 설정
        recyclerReports.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: ReportAdapter 구현 필요
        // recyclerReports.setAdapter(new ReportAdapter(reportList));

        // 신고하기 버튼들
        btnReportProperty.setOnClickListener(v -> {
            Toast.makeText(getContext(), "매물 신고 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 매물 신고 화면으로 이동
        });

        btnNewReport.setOnClickListener(v -> {
            Toast.makeText(getContext(), "새 신고 작성 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // TODO: 새 신고 작성 화면으로 이동
        });

        // 빈 상태 처리
        if (reportList.isEmpty()) {
            recyclerReports.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerReports.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 신고내역 화면 진입 시 하단바 숨김
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 신고내역 화면 빠져나갈 때 하단바 다시 표시
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }
}


