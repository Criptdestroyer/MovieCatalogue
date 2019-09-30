package com.criptdestroyer.moviecatalogueapi.model.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.criptdestroyer.moviecatalogueapi.model.db.DatabaseContract;

import org.json.JSONException;
import org.json.JSONObject;

import static com.criptdestroyer.moviecatalogueapi.model.db.DatabaseContract.getColumnString;

public class MovieItems implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String date;
    private String photo;

    public MovieItems() {

    }

    public MovieItems(int id, String title, String description, String date, String photo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.photo = photo;
    }

    public MovieItems(Cursor cursor){
        this.id = Integer.parseInt(getColumnString(cursor, DatabaseContract.FavColumns.ID));
        this.title = getColumnString(cursor, DatabaseContract.FavColumns.TITLE);
        this.description = getColumnString(cursor, DatabaseContract.FavColumns.DESCRIPTION);
        this.date = getColumnString(cursor, DatabaseContract.FavColumns.DATE);
    }

    public MovieItems(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.title = object.getString("title");
            this.description = object.getString("overview");
            this.date = object.getString("release_date");
            this.photo = object.getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getDate() {
        return date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.date);
        dest.writeString(this.photo);
    }

    private MovieItems(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.photo = in.readString();
    }

    public static final Parcelable.Creator<MovieItems> CREATOR = new Parcelable.Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}
