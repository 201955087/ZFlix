package com.kyl.zflix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kyl.zflix.R;
import com.kyl.zflix.model.PropertyListItem;
import java.util.List;
import java.util.ArrayList; // ArrayList를 사용하여 clear() 및 addAll() 가능하도록 추가

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<PropertyListItem> favoriteProperties;
    // 클릭 리스너 인터페이스 선언
    private OnItemClickListener listener;

    // 클릭 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(PropertyListItem item);
    }

    // 클릭 리스너 설정 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // List를 수정 가능한 ArrayList로 초기화 (수정 가능하게 변경)
    public FavoriteAdapter(List<PropertyListItem> favoriteProperties) {
        // 내부적으로 ArrayList를 사용하여 clear/addAll이 가능하도록 합니다.
        this.favoriteProperties = new ArrayList<>(favoriteProperties);
    }

    /**
     * 수정된 부분: 리스트를 명시적으로 초기화 후 추가하여 데이터 꼬임을 방지합니다.
     */
    public void setFavoriteProperties(List<PropertyListItem> newProperties) {
        // 기존 목록을 명시적으로 비웁니다. (데이터 꼬임 및 누적 방지)
        this.favoriteProperties.clear();
        // 새로운 목록의 데이터를 추가합니다.
        this.favoriteProperties.addAll(newProperties);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        PropertyListItem item = favoriteProperties.get(position);
        // bind 메서드에 listener 전달
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return favoriteProperties.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView roomImage;
        TextView roomPrice;
        TextView roomDescription;
        // ✨핵심: item_room.xml에 다른 뷰가 있다면 여기에 추가 선언해야 합니다.
        // 예: private TextView roomStatusText;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.room_image);
            roomPrice = itemView.findViewById(R.id.room_price);
            roomDescription = itemView.findViewById(R.id.room_description);
            // ✨핵심: item_room.xml에 다른 뷰가 있다면 여기에 findViewById를 추가해야 합니다.
            // 예: roomStatusText = itemView.findViewById(R.id.room_status_text);
        }

        /**
         * 수정된 부분: 뷰 재활용 시 잔여 데이터가 남지 않도록 모든 뷰 상태를 명시적으로 설정해야 합니다.
         */
        void bind(final PropertyListItem item, final OnItemClickListener listener) {
            // 이미지 로드 로직
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                String[] imageUrls = item.getImageUrl().split(",");
                if (imageUrls.length > 1) {
                    String secondImageUrl = imageUrls[1].trim();
                    Glide.with(itemView.getContext())
                            .load(secondImageUrl)
                            .placeholder(R.drawable.ic_home)
                            .into(roomImage);
                } else {
                    String firstImageUrl = imageUrls[0].trim();
                    Glide.with(itemView.getContext())
                            .load(firstImageUrl)
                            .placeholder(R.drawable.ic_home)
                            .into(roomImage);
                }
            } else {
                roomImage.setImageResource(R.drawable.ic_home);
            }

            // 텍스트 설정 (기존 로직 유지)
            roomPrice.setText("보증금 " + item.getDeposit() + " / 월세 " + item.getMonthlyRent());
            roomDescription.setText(item.getPropertyFeatures());

            // ✨가장 중요한 점검: item_room.xml에 위에서 설정하지 않은 다른 뷰가 있다면,
            // 이 곳에서 그 뷰들의 상태(예: Visibility, Text, Color 등)를
            // 현재 item 데이터에 맞게 **반드시 명시적으로 설정하거나 초기화**해야 합니다.
            // (예: roomStatusText.setVisibility(item.isSoldOut() ? View.VISIBLE : View.GONE);)

            // 아이템 클릭 이벤트 리스너 설정
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(item);
                    }
                }
            });
        }
    }
}