package com.criptdestroyer.moviecatalogueapi.model.provider;

import android.database.Cursor;

import com.criptdestroyer.moviecatalogueapi.model.entity.MovieItems;
import com.criptdestroyer.moviecatalogueapi.model.entity.TvShowItems;

import java.util.ArrayList;

import static com.criptdestroyer.moviecatalogueapi.model.db.DatabaseContract.FavColumns.DATE;
import static com.criptdestroyer.moviecatalogueapi.model.db.DatabaseContract.FavColumns.DESCRIPTION;
import static com.criptdestroyer.moviecatalogueapi.model.db.DatabaseContract.FavColumns.ID;
import static com.criptdestroyer.moviecatalogueapi.model.db.DatabaseContract.FavColumns.PHOTO;
import static com.criptdestroyer.moviecatalogueapi.model.db.DatabaseContract.FavColumns.TITLE;

public class MappingHelper {
    public static ArrayList<MovieItems> mapCursorMovieToArrayList(Cursor notesCursor) {
        ArrayList<MovieItems> movieItem = new ArrayList<>();

        while(notesCursor.moveToNext()){
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));
            String photo = notesCursor.getString(notesCursor.getColumnIndexOrThrow(PHOTO));
            movieItem.add(new MovieItems(id, title, description, date, photo));
        }
        return movieItem;
    }

    public static ArrayList<TvShowItems> mapCursorTvToArrayList(Cursor notesCursor){
        ArrayList<TvShowItems> tvShowItem = new ArrayList<>(0);
        while(notesCursor.moveToNext()){
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));
            String photo = notesCursor.getString(notesCursor.getColumnIndexOrThrow(PHOTO));
            tvShowItem.add(new TvShowItems(id, title, description, date, photo));
        }
        return tvShowItem;
    }
}
