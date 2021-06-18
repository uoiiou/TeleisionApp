package com.androidapp.televisorapp.activity.gallery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.models.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final ArrayList<Image> list;
    private GalleryAdapter.OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(GalleryAdapter.OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView_televisionImage;

        public ViewHolder(@NonNull View itemView, GalleryAdapter.OnItemClickListener clickListener) {
            super(itemView);

            imageView_televisionImage = itemView.findViewById(R.id.imageView_televisionImage1);

            itemView.setOnClickListener(view -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public GalleryAdapter(ArrayList<Image> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_gallery, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image item = list.get(position);

        Picasso.get().load(item.getImageURL()).into(holder.imageView_televisionImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}