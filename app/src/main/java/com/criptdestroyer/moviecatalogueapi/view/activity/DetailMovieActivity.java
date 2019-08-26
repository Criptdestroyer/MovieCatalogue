package com.criptdestroyer.moviecatalogueapi.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.criptdestroyer.moviecatalogueapi.R;
import com.criptdestroyer.moviecatalogueapi.model.db.FavoriteHelper;
import com.criptdestroyer.moviecatalogueapi.model.entity.MovieItems;
import com.criptdestroyer.moviecatalogueapi.viewmodel.DetailViewModel;

import java.util.ArrayList;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_FILM = "extra_film";
    private TextView tvTitle;
    private TextView tvDate;
    private ImageView imgPhoto;
    private ProgressBar progressBar;
    private MovieItems dataItem;
    private Button btnFavorite;
    private FavoriteHelper favoriteHelper;
    private boolean isFavorite = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        TextView tvDescription = findViewById(R.id.tv_detail_movie_description);
        TextView tvTrTitle = findViewById(R.id.tr_tv_movie_title);
        TextView tvTrDate = findViewById(R.id.tr_tv_movie_date);
        btnFavorite = findViewById(R.id.btn_fav_movie);
        progressBar = findViewById(R.id.progressBarMovieDetail);
        tvTitle = findViewById(R.id.tv_detail_movie_title);
        tvDate = findViewById(R.id.tv_detail_movie_year);
        imgPhoto = findViewById(R.id.img_detail_movie_photo);

        btnFavorite.setOnClickListener(this);

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        dataItem = getIntent().getParcelableExtra(EXTRA_FILM);

        if (dataItem != null) {
            if (dataItem.getDescription() != null) {
                tvDescription.setText(dataItem.getDescription());
            }
            tvTrTitle.setText(dataItem.getTitle());
            tvTrDate.setText(dataItem.getDate());

            DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
            detailViewModel.getDataMovie().observe(this, getMovie);
            detailViewModel.setMovie(dataItem.getId(), MainActivity.locale_language);
        }

        ArrayList<MovieItems> favorite = favoriteHelper.getFavoriteMovie();
        for (int i = 0; i < favorite.size(); i++) {
            if (favorite.get(i).getId() == dataItem.getId()) {
                isFavorite = true;
                btnFavorite.setText("Unfavorite");
            }
        }

        showLoading(true);
    }

    private Observer<MovieItems> getMovie = new Observer<MovieItems>() {
        @Override
        public void onChanged(MovieItems movieItems) {
            if (movieItems != null) {
                tvTitle.setText(movieItems.getTitle());
                tvDate.setText(movieItems.getDate());
                Glide.with(DetailMovieActivity.this).load("https://image.tmdb.org/t/p/w500" + movieItems.getPhoto()).placeholder(R.drawable.notfound).error(R.drawable.notfound).into(imgPhoto);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_fav_movie) {
            if (isFavorite) {
                long result = favoriteHelper.deleteFav(dataItem.getId());
                if (result > 0) {
                    Toast.makeText(this, "Success Delete from Favorite", Toast.LENGTH_SHORT).show();
                    btnFavorite.setText("Favorite");
                    isFavorite = false;
                } else {
                    Toast.makeText(this, "Failed Delete from Favorite", Toast.LENGTH_SHORT).show();
                }
            } else {
                long result = favoriteHelper.insertFavoriteMovie(dataItem);
                if (result > 0) {
                    Toast.makeText(this, "Success Add to Favorite", Toast.LENGTH_SHORT).show();
                    btnFavorite.setText("Unfavorite");
                    isFavorite = true;
                } else {
                    Toast.makeText(this, "Failed Add to Favorite", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
