package com.criptdestroyer.moviecatalogueapi.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.criptdestroyer.moviecatalogueapi.model.MovieItems;
import com.criptdestroyer.moviecatalogueapi.model.TvShowItems;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;


public class DetailViewModel extends ViewModel {
    private static final String API_KEY = "4c6b99dfcd747c39e86b3552395539e2";
    private MutableLiveData<MovieItems> listDataMovie = new MutableLiveData<>();
    private MutableLiveData<TvShowItems> listDataTvShow = new MutableLiveData<>();

    public void setMovie(int id, String language) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/"+id+"?api_key=" + API_KEY + "&language="+language;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    MovieItems dataItems = new MovieItems(responseObject);
                    listDataMovie.postValue(dataItems);
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

    public void setTvShow(int id, String language) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/tv/"+id+"?api_key=" + API_KEY + "&language="+language;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    TvShowItems dataItems = new TvShowItems(responseObject);
                    listDataTvShow.postValue(dataItems);
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

    public LiveData<MovieItems> getDataMovie() {
        return listDataMovie;
    }

    public LiveData<TvShowItems> getDataTvShow() {
        return listDataTvShow;
    }
}
