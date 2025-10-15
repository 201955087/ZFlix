package com.kyl.zflix.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kyl.zflix.model.RoomItem;
import java.util.List;
import com.kyl.zflix.R;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<RoomItem> roomList;

    public RoomAdapter(List<RoomItem> roomList) {
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomItem item = roomList.get(position);

        holder.title.setText(item.getTitle());
        holder.detail.setText(item.getDetail());
        holder.status.setText(item.getStatus());

        // 오직 URL만 사용해서 이미지 로드
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())    // RoomItem의 imageUrl 사용
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, detail, status;

        RoomViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.room_image);
            title = itemView.findViewById(R.id.room_price);
            detail = itemView.findViewById(R.id.room_description);
            status = itemView.findViewById(R.id.room_status);
        }
    }
}
