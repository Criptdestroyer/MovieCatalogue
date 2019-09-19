package com.criptdestroyer.moviecatalogueapi.view.reminder;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.criptdestroyer.moviecatalogueapi.model.entity.MovieItems;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ReleaseReminder extends JobService {
    public static final String TAG = ReleaseReminder.class.getSimpleName();
    final String APP_ID = "4c6b99dfcd747c39e86b3552395539e2";
    private List<MovieItems> listItems = new ArrayList<>();


    @Override
    public boolean onStartJob(JobParameters params) {
        getReleaseToday(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob() Executed");
        return true;
    }

    private void getReleaseToday(final JobParameters job){
        Log.d(TAG, "Running");
        AsyncHttpClient client = new AsyncHttpClient();

        long milis = System.currentTimeMillis();
        Date date = new Date(milis);
        String url = "https://api.themoviedb.org/3/discover/movie?api_key="+APP_ID+"&primary_release_date.gte="+date+"&primary_release_date.lte="+date;
        Log.e(TAG, "getReleaseToday: "+url );

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);

                try{
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems dataItems = new MovieItems(movie);
                        listItems.add(dataItems);
                    }

                    jobFinished(job, false);
                } catch (JSONException e) {
                    jobFinished(job, true);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                jobFinished(job, true);
            }
        });
    }
}
