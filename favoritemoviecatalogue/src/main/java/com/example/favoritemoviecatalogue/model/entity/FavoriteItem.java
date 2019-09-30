package com.example.favoritemoviecatalogue.model.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.favoritemoviecatalogue.model.db.DatabaseContract;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.favoritemoviecatalogue.model.db.DatabaseContract.getColumnString;

public class FavoriteItem implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String date;
    private String photo;

    public FavoriteItem() {

    }

    public FavoriteItem(int id, String title, String description, String date, String photo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.photo = photo;
    }

    public FavoriteItem(Cursor cursor){
        this.id = Integer.parseInt(getColumnString(cursor, DatabaseContract.FavColumns.ID));
        this.title = getColumnString(cursor, DatabaseContract.FavColumns.TITLE);
        this.description = getColumnString(cursor, DatabaseContract.FavColumns.DESCRIPTION);
        this.date = getColumnString(cursor, DatabaseContract.FavColumns.DATE);
    }

    public FavoriteItem(JSONObject object, String type) {
        try {
            this.id = object.getInt("id");
            this.description = object.getString("overview");

            this.photo = object.getString("poster_path");
            if(type.equals("movie")){
                this.title = object.getString("title");
                this.date = object.getString("release_date");
            }else{
                this.title = object.getString("name");
                this.date = object.getString("first_air_date");
            }

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

    private FavoriteItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.photo = in.readString();
    }

    public static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>() {
        @Override
        public FavoriteItem createFromParcel(Parcel source) {
            return new FavoriteItem(source);
        }

        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };
}
