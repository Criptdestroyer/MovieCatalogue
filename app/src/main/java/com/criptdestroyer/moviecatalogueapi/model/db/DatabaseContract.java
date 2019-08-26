package com.criptdestroyer.moviecatalogueapi.model.db;


class DatabaseContract {
    static String TABLE_FAV = "favorite";

    static final class FavColumns {
        static String ID = "id";
        static String TITLE = "title";
        static String DESCRIPTION = "description";
        static String DATE = "date";
        static String PHOTO = "photo";
        static String TYPE = "type";
    }
}
