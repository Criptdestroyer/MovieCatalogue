package com.criptdestroyer.moviecatalogueapi.view.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.criptdestroyer.moviecatalogueapi.R;
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

    public TvShowFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tv_show, container, false);

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getDataTvShow().observe(this, getTvShow);
        mainViewModel.setTvShow(MainActivity.locale_language);

        adapter = new TvShowAdapter();
        adapter.notifyDataSetChanged();

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_tv_show);
        progressBar = rootView.findViewById(R.id.progressBarTvShow);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        showLoading(true);
        return rootView;
    }

    private Observer<ArrayList<TvShowItems>> getTvShow = new Observer<ArrayList<TvShowItems>>() {
        @Override
        public void onChanged(ArrayList<TvShowItems> dataItems) {
            if(dataItems != null){
                adapter.setData(dataItems);
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
