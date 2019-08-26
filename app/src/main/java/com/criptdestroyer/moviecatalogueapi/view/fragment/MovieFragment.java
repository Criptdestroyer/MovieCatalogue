package com.criptdestroyer.moviecatalogueapi.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.criptdestroyer.moviecatalogueapi.R;
import com.criptdestroyer.moviecatalogueapi.model.db.FavoriteHelper;
import com.criptdestroyer.moviecatalogueapi.model.entity.MovieItems;
import com.criptdestroyer.moviecatalogueapi.view.adapter.MovieAdapter;
import com.criptdestroyer.moviecatalogueapi.viewmodel.MainViewModel;

import java.util.ArrayList;

import static com.criptdestroyer.moviecatalogueapi.view.activity.MainActivity.locale_language;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private boolean favorite;
    private FavoriteHelper favoriteHelper;

    public MovieFragment() {

    }

    public MovieFragment(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_movie);
        progressBar = rootView.findViewById(R.id.progressBarMovie);

        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();

        favoriteHelper = FavoriteHelper.getInstance(rootView.getContext());
        favoriteHelper.open();

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getDataMovie().observe(this, getMovie);
        mainViewModel.setMovie(locale_language);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        showLoading(true);

        return rootView;
    }

    private Observer<ArrayList<MovieItems>> getMovie = new Observer<ArrayList<MovieItems>>() {
        @Override
        public void onChanged(ArrayList<MovieItems> dataItems) {
            if (dataItems != null) {
                if (favorite) {
                    ArrayList<MovieItems> favoriteMovie = new ArrayList<>();
                    ArrayList<MovieItems> favoriteMovieSql = favoriteHelper.getFavoriteMovie();
                    for (int i = 0; i < favoriteMovieSql.size(); i++) {
                        for (int j = 0; j < dataItems.size(); j++) {
                            if (favoriteMovieSql.get(i).getId() == dataItems.get(j).getId()) {
                                favoriteMovie.add(dataItems.get(j));
                                break;
                            }
                        }
                    }
                    adapter.setData(favoriteMovie);
                } else {
                    adapter.setData(dataItems);
                }
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
