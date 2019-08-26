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
import com.criptdestroyer.moviecatalogueapi.model.entity.TvShowItems;
import com.criptdestroyer.moviecatalogueapi.view.activity.MainActivity;
import com.criptdestroyer.moviecatalogueapi.view.adapter.TvShowAdapter;
import com.criptdestroyer.moviecatalogueapi.viewmodel.MainViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    private TvShowAdapter adapter;
    private ProgressBar progressBar;
    private boolean favorite;
    private FavoriteHelper favoriteHelper;

    public TvShowFragment() {

    }

    public TvShowFragment(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tv_show, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_tv_show);
        progressBar = rootView.findViewById(R.id.progressBarTvShow);

        adapter = new TvShowAdapter();
        adapter.notifyDataSetChanged();

        favoriteHelper = FavoriteHelper.getInstance(rootView.getContext());
        favoriteHelper.open();

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getDataTvShow().observe(this, getTvShow);
        mainViewModel.setTvShow(MainActivity.locale_language);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        showLoading(true);
        return rootView;
    }

    private Observer<ArrayList<TvShowItems>> getTvShow = new Observer<ArrayList<TvShowItems>>() {
        @Override
        public void onChanged(ArrayList<TvShowItems> dataItems) {
            if (dataItems != null) {
                if (favorite) {
                    ArrayList<TvShowItems> favoriteTv = new ArrayList<>();
                    ArrayList<TvShowItems> favoriteTvSql = favoriteHelper.getFavoriteTv();
                    for (int i = 0; i < favoriteTvSql.size(); i++) {
                        for (int j = 0; j < dataItems.size(); j++) {
                            if (favoriteTvSql.get(i).getId() == dataItems.get(j).getId()) {
                                favoriteTv.add(dataItems.get(j));
                                break;
                            }
                        }
                    }
                    adapter.setData(favoriteTv);
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
