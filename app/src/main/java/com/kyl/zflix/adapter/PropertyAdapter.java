package com.kyl.zflix.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import com.kyl.zflix.R;
import java.util.ArrayList;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private static final String TAG = "PropertyAdapter";
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

        // 이미지 처리
        String imageUrls = item.getImageUrl();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String[] urlArray = imageUrls.split(",");
            if (urlArray.length > 1) {
                Glide.with(context)
                        .load(urlArray[1].trim())
                        .placeholder(R.drawable.icons_loading)
                        .error(R.drawable.baseline_home_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(holder.profileImage);
            } else {
                Glide.with(context)
                        .load(urlArray[0].trim())
                        .placeholder(R.drawable.icons_loading)
                        .error(R.drawable.baseline_home_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(holder.profileImage);
            }
        } else {
            holder.profileImage.setImageResource(R.drawable.baseline_home_24);
        }

        // 타이틀/본문 설정
        holder.title.setText(item.getPropertyType());

        String body1Text = item.getDeposit() + "/" + item.getMonthlyRent() + ", " +
                item.getGrossArea() + ", " +
                item.getFloor() + "/" + item.getTotalFloors() + "층, " +
                item.getDirection();
        holder.body1.setText(body1Text);

        // PropertyListItem의 summary를 body2에 표시
        String aiSummary = item.getSummary();
        if (aiSummary != null && !aiSummary.isEmpty() && !aiSummary.equals("AI 정보 로딩 실패") && !aiSummary.equals("AI가 응답하지 못했습니다.")) {
            // 요약 결과가 있으면 표시
            holder.body2.setText(aiSummary);
        } else if (aiSummary != null) {
            // 실패 또는 응답 없음 메시지가 있으면 그대로 표시
            holder.body2.setText(aiSummary);
        } else {
            // 요약 요청 전이거나 summary가 null이면 로딩 텍스트 표시
            holder.body2.setText("주변 정보 로딩 중...");
        }

        String city = safeString(item.getCity());
        String district = safeString(item.getDistrict());
        String legalDong = safeString(item.getLegal_dong());
        String detailAddress = safeString(item.getDetail_address());

        String fullAddress = String.join(" ", city, district, legalDong, detailAddress).trim();
        holder.itemView.setTag(R.id.property_item_address_tag, fullAddress);

        // 클릭 처리
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

    /**
     * Activity에서 AI 요약을 받아서 해당 listingId의 아이템을 업데이트하고 RecyclerView를 갱신합니다.
     */
    public void setSummaryForListing(String listingId, String summaryText) {
        if (listingId == null) return;
        for (int i = 0; i < items.size(); i++) {
            PropertyListItem it = items.get(i);
            if (listingId.equals(it.getListingId())) {

                // ⭐ 수정된 부분: i의 값을 final 변수에 복사
                final int finalI = i;

                // PropertyListItem의 summary 필드를 업데이트
                it.setSummary(summaryText);

                // UI 스레드에서 특정 아이템만 갱신. finalI를 사용합니다.
                ((Activity)context).runOnUiThread(() -> notifyItemChanged(finalI));

                break;
            }
        }
    }

    private String safeString(String s) {
        return s == null ? "" : s;
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