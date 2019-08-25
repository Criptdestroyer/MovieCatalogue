package com.criptdestroyer.moviecatalogueapi.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.criptdestroyer.moviecatalogueapi.R;
import com.criptdestroyer.moviecatalogueapi.model.MovieItems;
import com.criptdestroyer.moviecatalogueapi.viewmodel.DetailViewModel;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_FILM = "extra_film";
    private TextView tvTitle;
    private TextView tvDate;
    private ImageView imgPhoto;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        TextView tvDescription = findViewById(R.id.tv_detail_movie_description);
        TextView tvTrTitle = findViewById(R.id.tr_tv_movie_title);
        TextView tvTrDate = findViewById(R.id.tr_tv_movie_date);
        Button btnFavorite = findViewById(R.id.btn_fav);
        progressBar = findViewById(R.id.progressBarMovieDetail);
        tvTitle = findViewById(R.id.tv_detail_movie_title);
        tvDate = findViewById(R.id.tv_detail_movie_year);
        imgPhoto = findViewById(R.id.img_detail_movie_photo);

        btnFavorite.setOnClickListener(this);

        MovieItems dataItem = getIntent().getParcelableExtra(EXTRA_FILM);

        if (dataItem != null) {
            if(dataItem.getDescription() != null){
                tvDescription.setText(dataItem.getDescription());
            }
            tvTrTitle.setText(dataItem.getTitle());
            tvTrDate.setText(dataItem.getDate());

            DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
            detailViewModel.getDataMovie().observe(this, getMovie);
            detailViewModel.setMovie(dataItem.getId(), MainActivity.locale_language);
        }

        showLoading(true);
    }

    private Observer<MovieItems> getMovie = new Observer<MovieItems>() {
        @Override
        public void onChanged(MovieItems movieItems) {
            if(movieItems != null){
                tvTitle.setText(movieItems.getTitle());
                tvDate.setText(movieItems.getDate());
                Glide.with(DetailMovieActivity.this).load("https://image.tmdb.org/t/p/w500"+movieItems.getPhoto()).placeholder(R.drawable.notfound).error(R.drawable.notfound).into(imgPhoto);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_fav:
                Toast.makeText(this, "Add to Favorite", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
