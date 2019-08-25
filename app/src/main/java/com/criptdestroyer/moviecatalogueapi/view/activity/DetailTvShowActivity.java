package com.criptdestroyer.moviecatalogueapi.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.criptdestroyer.moviecatalogueapi.R;
import com.criptdestroyer.moviecatalogueapi.model.TvShowItems;
import com.criptdestroyer.moviecatalogueapi.viewmodel.DetailViewModel;

public class DetailTvShowActivity extends AppCompatActivity {
    public static final String EXTRA_FILM = "extra_film";
    private TextView tvTitle;
    private TextView tvDate;
    private ImageView imgPhoto;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

        TextView tvDescription = findViewById(R.id.tv_detail_tv_show_description);
        TextView tvTrTitle = findViewById(R.id.tr_tv_tv_show_title);
        TextView tvTrDate = findViewById(R.id.tr_tv_tv_show_date);
        progressBar = findViewById(R.id.progressBarTvShowDetail);
        tvTitle = findViewById(R.id.tv_detail_tv_show_title);
        tvDate = findViewById(R.id.tv_detail_tv_show_year);
        imgPhoto = findViewById(R.id.img_detail_tv_show_photo);

        TvShowItems dataItem = getIntent().getParcelableExtra(EXTRA_FILM);

        if (dataItem != null) {
            if(dataItem.getDescription() != null){
                tvDescription.setText(dataItem.getDescription());
            }
            tvTrTitle.setText(dataItem.getTitle());
            tvTrDate.setText(dataItem.getDate());

            DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
            detailViewModel.getDataTvShow().observe(this, getTvShow);
            detailViewModel.setTvShow(dataItem.getId(), MainActivity.locale_language);
        }

        showLoading(true);
    }

    private Observer<TvShowItems> getTvShow = new Observer<TvShowItems>() {
        @Override
        public void onChanged(TvShowItems tvShowItems) {
            if(tvShowItems != null){
                tvTitle.setText(tvShowItems.getTitle());
                tvDate.setText(tvShowItems.getDate());
                Glide.with(DetailTvShowActivity.this).load("https://image.tmdb.org/t/p/w500"+tvShowItems.getPhoto()).placeholder(R.drawable.notfound).error(R.drawable.notfound).into(imgPhoto);
                showLoading(false);
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
