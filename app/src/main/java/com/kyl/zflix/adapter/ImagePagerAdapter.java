package com.kyl.zflix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import com.kyl.zflix.R;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {

    private final List<String> imageUrls;

    public ImagePagerAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position).trim();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                // ⭐️ 여기를 수정했습니다. placeholder와 error 이미지 리소스를 직접 지정해야 합니다.
                // res/drawable 폴더에 있는 이미지 파일 이름으로 변경해주세요.
                .placeholder(R.drawable.load) // 예: 로딩 중일 때 보여줄 이미지
                .error(R.drawable.error) // 예: 로딩 실패 시 보여줄 이미지
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImageView);
        }
    }
}