package com.example.favoritemoviecatalogue.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.favoritemoviecatalogue.model.entity.FavoriteItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class DetailViewModel extends ViewModel {
    private static final String API_KEY = "4c6b99dfcd747c39e86b3552395539e2";
    private MutableLiveData<FavoriteItem> listDataFavorite = new MutableLiveData<>();

    public void setFavorite(int id, String language) {
        AsyncHttpClient client = new AsyncHttpClient();
        String urlMovie = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + API_KEY + "&language=" + language;
        String urlTv = "https://api.themoviedb.org/3/tv/" + id + "?api_key=" + API_KEY + "&language=" + language;

        client.get(urlMovie, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    FavoriteItem dataItems = new FavoriteItem(responseObject, "movie");
                    listDataFavorite.postValue(dataItems);
                } catch (JSONException e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });

        client.get(urlTv, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    FavoriteItem dataItems = new FavoriteItem(responseObject, "tv");
                    listDataFavorite.postValue(dataItems);
                } catch (JSONException e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<FavoriteItem> getDataFavorite() {
        return listDataFavorite;
    }

}
