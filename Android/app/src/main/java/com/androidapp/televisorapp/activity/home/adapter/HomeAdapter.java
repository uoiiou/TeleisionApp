package com.androidapp.televisorapp.activity.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.models.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements Filterable {

    private final ArrayList<Item> list;
    private ArrayList<Item> fullList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView_televisionImage;
        public TextView textView_brand;
        public TextView textView_model;
        public TextView textView_year;
        public TextView textView_price;
        public ImageView imageView_deleteIcon;

        public ViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);

            imageView_televisionImage = itemView.findViewById(R.id.imageView_televisionImage);
            textView_brand = itemView.findViewById(R.id.textView_brand);
            textView_model = itemView.findViewById(R.id.textView_model);
            textView_year = itemView.findViewById(R.id.textView_year);
            textView_price = itemView.findViewById(R.id.textView_price);
            imageView_deleteIcon = itemView.findViewById(R.id.imageView_deleteIcon);

            itemView.setOnClickListener(view -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(position);
                    }
                }
            });

            imageView_deleteIcon.setOnClickListener(view -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    public HomeAdapter(ArrayList<Item> list) {
        this.list = list;
    }

    public void setFullList() {
        fullList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = list.get(position);

        Picasso.get().load(item.getImageURL()).into(holder.imageView_televisionImage);
        holder.textView_brand.setText(item.getBrand());
        holder.textView_model.setText(item.getModel());
        holder.textView_year.setText(String.valueOf(item.getYear()));
        holder.textView_price.setText(String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Item> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Item item : fullList) {
                    if (item.getBrand().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };
}