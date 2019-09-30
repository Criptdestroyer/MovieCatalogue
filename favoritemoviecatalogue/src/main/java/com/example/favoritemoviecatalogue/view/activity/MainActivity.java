package com.example.favoritemoviecatalogue.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.favoritemoviecatalogue.R;
import com.example.favoritemoviecatalogue.model.entity.FavoriteItem;
import com.example.favoritemoviecatalogue.view.adapter.FavoriteAdapter;
import com.example.favoritemoviecatalogue.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static com.example.favoritemoviecatalogue.model.db.DatabaseContract.FavColumns.CONTENT_URI;
import static com.example.favoritemoviecatalogue.model.db.DatabaseContract.FavColumns.DATE;
import static com.example.favoritemoviecatalogue.model.db.DatabaseContract.FavColumns.DESCRIPTION;
import static com.example.favoritemoviecatalogue.model.db.DatabaseContract.FavColumns.ID;
import static com.example.favoritemoviecatalogue.model.db.DatabaseContract.FavColumns.PHOTO;
import static com.example.favoritemoviecatalogue.model.db.DatabaseContract.FavColumns.TITLE;

public class MainActivity extends AppCompatActivity {
    public static String locale_language;
    private FavoriteAdapter adapter;
    private ProgressBar progressBar;
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Favorite Movie Catalogue");
        if (Locale.getDefault().getLanguage().equals("in")) {
            locale_language = "id";
        } else {
            locale_language = Locale.getDefault().getLanguage();
        }

        Log.d("Language: ", locale_language);

        resolver = getContentResolver();

        RecyclerView recyclerView = findViewById(R.id.rv_movie);
        progressBar = findViewById(R.id.progressBarMovie);

        adapter = new FavoriteAdapter(this);
        adapter.notifyDataSetChanged();

        final MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getDataMovie().observe(this, getMovie);
        mainViewModel.setFavorite(locale_language);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        HandlerThread handlerThread = new HandlerThread("DataObserver");
//        handlerThread.start();
//        Handler handler = new Handler(handlerThread.getLooper());
//        DataObserver myObserver = new DataObserver(handler, this);
//        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);



        showLoading(true);
    }

    private Observer<ArrayList<FavoriteItem>> getMovie = new Observer<ArrayList<FavoriteItem>>() {
        @Override
        public void onChanged(ArrayList<FavoriteItem> dataItems) {
            if (dataItems != null) {
                ArrayList<FavoriteItem> favoriteMovie = new ArrayList<>();
                ArrayList<FavoriteItem> favoriteMovieSql = mapCursorMovieToArrayList(Objects.requireNonNull(resolver.query(CONTENT_URI, null, null, null, null)));
                for (int i = 0; i < favoriteMovieSql.size(); i++) {
                    for (int j = 0; j < dataItems.size(); j++) {
                        if (favoriteMovieSql.get(i).getId() == dataItems.get(j).getId()) {
                            favoriteMovie.add(dataItems.get(j));
                            break;
                        }
                        if (j == dataItems.size() - 1) {
                            favoriteMovie.add(favoriteMovieSql.get(i));
                        }
                    }
                }
                adapter.setData(favoriteMovie);
                showLoading(false);
            }
        }
    };

    public static ArrayList<FavoriteItem> mapCursorMovieToArrayList(Cursor notesCursor) {
        ArrayList<FavoriteItem> movieItem = new ArrayList<>();

        while(notesCursor.moveToNext()){
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));
            String photo = notesCursor.getString(notesCursor.getColumnIndexOrThrow(PHOTO));
            movieItem.add(new FavoriteItem(id, title, description, date, photo));
        }
        return movieItem;
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
    }
}
