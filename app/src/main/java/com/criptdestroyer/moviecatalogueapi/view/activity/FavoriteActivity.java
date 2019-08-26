package com.criptdestroyer.moviecatalogueapi.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.criptdestroyer.moviecatalogueapi.R;
import com.criptdestroyer.moviecatalogueapi.view.fragment.MovieFragment;
import com.criptdestroyer.moviecatalogueapi.view.fragment.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.Objects;

import static com.criptdestroyer.moviecatalogueapi.view.activity.MainActivity.locale_language;

public class FavoriteActivity extends AppCompatActivity {
    private int currentTab;
    private BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    currentTab = R.id.navigation_movie;
                    fragment = new MovieFragment(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                    return true;
                case R.id.navigation_tv_show:
                    currentTab = R.id.navigation_tv_show;
                    fragment = new TvShowFragment(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (Locale.getDefault().getLanguage().equals("in")) {
            locale_language = "id";
            Objects.requireNonNull(getSupportActionBar()).setTitle("Favorit");
        } else {
            locale_language = Locale.getDefault().getLanguage();
            Objects.requireNonNull(getSupportActionBar()).setTitle("Favorite");
        }

        Log.d("Language: ", locale_language);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movie);
        } else {
            navView.setSelectedItemId(savedInstanceState.getInt("TAB"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("TAB", currentTab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navView.setSelectedItemId(currentTab);
    }
}
