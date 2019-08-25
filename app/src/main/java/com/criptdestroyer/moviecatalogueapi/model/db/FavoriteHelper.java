package com.criptdestroyer.moviecatalogueapi.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.criptdestroyer.moviecatalogueapi.model.entity.MovieItems;
import com.criptdestroyer.moviecatalogueapi.model.entity.TvShowItems;

import java.util.ArrayList;

import static com.criptdestroyer.moviecatalogueapi.model.db.DatabaseContract.TABLE_FAV;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_FAV;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTACE;

    private static SQLiteDatabase database;

    private FavoriteHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context){
        if(INSTACE == null){
            synchronized (SQLiteOpenHelper.class){
                if(INSTACE == null){
                    INSTACE = new FavoriteHelper(context);
                }
            }
        }
        return INSTACE;
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if(database.isOpen()){
            database.close();
        }
    }

    public ArrayList<MovieItems> getFavoriteMovie(){
        ArrayList<MovieItems> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                DatabaseContract.FavColumns.TYPE +" = 'movie'",
                null,
                null,
                null,
                DatabaseContract.FavColumns.ID+" ASC",
                null);

        cursor.moveToFirst();
        MovieItems movieItems;
        if(cursor.getCount() > 0){
            do{
                movieItems = new MovieItems();
                movieItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.ID)));
                movieItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.TITLE)));
                movieItems.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.DESCRIPTION)));
                movieItems.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.DATE)));
                movieItems.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.PHOTO)));

                arrayList.add(movieItems);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertFavoriteMovie(MovieItems items){
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.FavColumns.ID, items.getId());
        args.put(DatabaseContract.FavColumns.TITLE, items.getTitle());
        args.put(DatabaseContract.FavColumns.DESCRIPTION, items.getDescription());
        args.put(DatabaseContract.FavColumns.DATE, items.getDate());
        args.put(DatabaseContract.FavColumns.PHOTO, items.getPhoto());
        args.put(DatabaseContract.FavColumns.TYPE, "movie");

        return database.insert(DATABASE_TABLE, null, args);
    }

    public ArrayList<TvShowItems> getFavoriteTv(){
        ArrayList<TvShowItems> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                DatabaseContract.FavColumns.TYPE +" = 'tv'",
                null,
                null,
                null,
                DatabaseContract.FavColumns.ID+" ASC",
                null);

        cursor.moveToFirst();
        TvShowItems movieItems;
        if(cursor.getCount() > 0){
            do{
                movieItems = new TvShowItems();
                movieItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.ID)));
                movieItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.TITLE)));
                movieItems.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.DESCRIPTION)));
                movieItems.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.DATE)));
                movieItems.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.PHOTO)));

                arrayList.add(movieItems);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertFavoriteTv(TvShowItems items){
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.FavColumns.ID, items.getId());
        args.put(DatabaseContract.FavColumns.TITLE, items.getTitle());
        args.put(DatabaseContract.FavColumns.DESCRIPTION, items.getDescription());
        args.put(DatabaseContract.FavColumns.DATE, items.getDate());
        args.put(DatabaseContract.FavColumns.PHOTO, items.getPhoto());
        args.put(DatabaseContract.FavColumns.TYPE, "tv");

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteFav(int id){
        return  database.delete(TABLE_FAV, DatabaseContract.FavColumns.ID+" = "+id, null);
    }

}
