package com.kyl.zflix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kyl.zflix.R;

public class ReportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("허위매물 신고내역");

        // TODO: 신고내역 리스트 구현
        TextView tvEmpty = view.findViewById(R.id.tvEmpty);
        tvEmpty.setText("신고한 내역이 없습니다.");

        return view;
    }
}

