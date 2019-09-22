package com.example.favoritemoviecatalogue.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.favoritemoviecatalogue.R;
import com.example.favoritemoviecatalogue.model.entity.FavoriteItem;
import com.example.favoritemoviecatalogue.view.activity.DetailFavoriteActivity;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.DataViewHolder> {
    private ArrayList<FavoriteItem> mData = new ArrayList<>();
    private final Activity activity;

    public FavoriteAdapter(Activity activity){
        this.activity = activity;
    }

    public void setData(ArrayList<FavoriteItem> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new DataViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.bind(mData.get(position));

        final int i = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailFavoriteActivity.class);
                intent.putExtra(DetailFavoriteActivity.EXTRA_FILM, mData.get(i));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtDate;
        private TextView txtDescription;
        private ImageView imgPhoto;
        private View view;

        DataViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtTitle = itemView.findViewById(R.id.tv_item_title);
            txtDate = itemView.findViewById(R.id.tv_item_date);
            txtDescription = itemView.findViewById(R.id.tv_item_description);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
        }

        void bind(FavoriteItem dataItems) {
            txtTitle.setText(dataItems.getTitle());
            txtDate.setText(dataItems.getDate());
            txtDescription.setText(dataItems.getDescription());
            Glide.with(view).load("https://image.tmdb.org/t/p/w500" + dataItems.getPhoto()).placeholder(R.drawable.notfound).error(R.drawable.notfound).into(imgPhoto);
        }
    }
}
