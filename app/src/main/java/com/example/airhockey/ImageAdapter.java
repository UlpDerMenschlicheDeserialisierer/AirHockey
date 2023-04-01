package com.example.airhockey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<Integer> imageList;
    private ViewPager2 viewPager2;

    public ImageAdapter(ArrayList<Integer> imageList, ViewPager2 viewPager2) {
        this.imageList = imageList;
        this.viewPager2 = viewPager2;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_container, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.imageView.setImageResource(imageList.get(position));
        if (position == imageList.size()-1){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imageList.addAll(imageList);
            notifyDataSetChanged();
        }
    };

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

