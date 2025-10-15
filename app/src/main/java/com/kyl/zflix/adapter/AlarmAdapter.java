package com.kyl.zflix.adapter;

import com.kyl.zflix.model.AlarmItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.kyl.zflix.R;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private Context context;
    private List<AlarmItem> alarmList;

    public AlarmAdapter(Context context, List<AlarmItem> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R                                             .layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmItem item = alarmList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvTime.setText(item.getTime());

        // 읽음 여부에 따라 배경 변경
        if (item.isRead()) {
            holder.rootLayout.setBackgroundResource(R.drawable.bg_alarm_read);
        } else {
            holder.rootLayout.setBackgroundResource(R.drawable.bg_alarm_unread);
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void markAllAsRead() {
        for (AlarmItem item : alarmList) {
            item.setRead(true);
        }
        notifyDataSetChanged();
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rootLayout;
        TextView tvTitle, tvTime;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            tvTitle = itemView.findViewById(R.id.tv_alarm_title);
            tvTime = itemView.findViewById(R.id.tv_alarm_time);
        }
    }
}
