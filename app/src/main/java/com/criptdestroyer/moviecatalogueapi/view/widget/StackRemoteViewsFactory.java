package com.criptdestroyer.moviecatalogueapi.view.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.bumptech.glide.Glide;
import com.criptdestroyer.moviecatalogueapi.R;
import com.criptdestroyer.moviecatalogueapi.model.db.FavoriteHelper;
import com.criptdestroyer.moviecatalogueapi.model.entity.MovieItems;
import com.criptdestroyer.moviecatalogueapi.model.entity.TvShowItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<String> list = new ArrayList<>();
    private final Context context;
    private final long identityToken;
    private FavoriteHelper favoriteHelper;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
        favoriteHelper = FavoriteHelper.getInstance(context);
        identityToken = Binder.clearCallingIdentity();
        favoriteHelper.open();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        List<MovieItems> movieItems = favoriteHelper.getFavoriteMovie();
        List<TvShowItems> tvShowItems = favoriteHelper.getFavoriteTv();
        list.clear();

        for (MovieItems movie:
             movieItems) {
            list.add(movie.getPhoto());
        }

        for (TvShowItems tvShow:
             tvShowItems) {
            list.add(tvShow.getPhoto());
        }

    }

    @Override
    public void onDestroy() {
        Binder.restoreCallingIdentity(identityToken);
        favoriteHelper.close();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        try {
            rv.setImageViewBitmap(R.id.imageView, Glide.with(context).asBitmap().load("https://image.tmdb.org/t/p/w500"+list.get(position)).into(250, 250).get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
