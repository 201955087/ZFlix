package com.kyl.zflix.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable; // 💡 추가됨
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat; // 💡 추가됨
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
            // 요약 결과가 있으면 표시하고 아이콘을 보여줍니다. (크기 조절)
            holder.body2.setText(aiSummary);
            setBody2Icon(holder.body2, R.drawable.ic_gemini);

        } else if (aiSummary != null) {
            // 실패 또는 응답 없음 메시지가 있으면 그대로 표시하고 아이콘을 숨깁니다.
            holder.body2.setText(aiSummary);
            setBody2Icon(holder.body2, 0);

        } else {
            // 요약 요청 전이거나 summary가 null이면 로딩 텍스트 표시하고 아이콘을 숨깁니다.
            holder.body2.setText("주변 정보 로딩 중...");
            setBody2Icon(holder.body2, 0);
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

    // =======================================================
    // 💡 이미지 크기 조절 및 설정 관련 유틸리티 함수 추가
    // =======================================================

    /**
     * 아이콘을 텍스트 뷰의 텍스트 크기에 맞게 조절하여 반환합니다.
     * @param textView 대상 TextView
     * @param drawableId 사용할 Drawable 리소스 ID
     * @return 크기가 조절된 Drawable
     */
    private Drawable resizeDrawable(TextView textView, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable == null) {
            return null;
        }

        // 텍스트 폰트 크기(픽셀)를 이미지의 높이 기준으로 사용합니다.
        int textHeight = (int) textView.getTextSize();

        // 이미지의 원본 가로세로 비율을 유지합니다.
        float ratio = (float) drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
        int newWidth = Math.round(textHeight * ratio);

        // 크기 설정 (setBounds)
        drawable.setBounds(0, 0, newWidth, textHeight);
        return drawable;
    }

    /**
     * TextView의 drawableStart에 크기가 조절된 이미지를 설정합니다.
     * @param textView 대상 TextView
     * @param drawableId 사용할 Drawable 리소스 ID (아이콘을 숨길 때는 0)
     */
    private void setBody2Icon(TextView textView, int drawableId) {
        if (drawableId != 0) {
            // 아이콘을 표시할 때: 크기를 조절하여 설정
            Drawable icon = resizeDrawable(textView, drawableId);
            // setCompoundDrawables를 사용하며, drawableStart에 icon을 설정합니다.
            // setCompoundDrawables(left, top, right, bottom)
            textView.setCompoundDrawables(icon, null, null, null);

            // drawablePadding도 여기서 설정 (4dp)
            int paddingDp = 4;
            int paddingPx = (int) (paddingDp * textView.getContext().getResources().getDisplayMetrics().density);
            textView.setCompoundDrawablePadding(paddingPx);

        } else {
            // 아이콘을 숨길 때: null로 설정
            textView.setCompoundDrawables(null, null, null, null);
            textView.setCompoundDrawablePadding(0);
        }
    }

    // =======================================================

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