package com.kyl.zflix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kyl.zflix.model.PropertyListItem;
import java.util.ArrayList;
import java.util.List;
import com.kyl.zflix.R;
import java.util.Arrays;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private final Context context;
    private final List<PropertyListItem> items;
    private final OnItemClickListener listener;
    private String propertyType;

    public interface OnItemClickListener {
        void onItemClick(String listingId, String propertyType);
    }

    public PropertyAdapter(Context context, List<PropertyListItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.listener = listener;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        PropertyListItem item = items.get(position);

        String imageUrls = item.getImageUrl();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String[] urlArray = imageUrls.split(",");
            if (urlArray.length > 1) {
                Glide.with(context)
                        // ⭐ .asGif() 메소드를 삭제합니다.
                        .load(urlArray[1].trim()) // 첫 번째 URL을 로드
                        .placeholder(R.drawable.icons_loading) // 이 GIF는 로딩 중에만 표시됩니다.
                        .error(R.drawable.baseline_home_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(holder.profileImage);
            } else {
                holder.profileImage.setImageResource(R.drawable.baseline_home_24);
            }
        } else {
            holder.profileImage.setImageResource(R.drawable.baseline_home_24);
        }

        holder.title.setText(item.getPropertyType());

        String body1Text = item.getDeposit() + "/" + item.getMonthlyRent() + ", " +
                item.getNetArea() + ", " +
                item.getFloor() + "/" + item.getTotalFloors() + "층, " +
                item.getDirection();
        holder.body1.setText(body1Text);

        holder.body2.setText(item.getPropertyFeatures());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item.getListingId(), propertyType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<PropertyListItem> newList) {
        items.clear();
        if (newList != null) {
            items.addAll(newList);
        }
        notifyDataSetChanged();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView title, body1, body2;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.propertyProfile);
            title = itemView.findViewById(R.id.propertyTitle);
            body1 = itemView.findViewById(R.id.propertyBody1);
            body2 = itemView.findViewById(R.id.propertyBody2);
        }
    }
}