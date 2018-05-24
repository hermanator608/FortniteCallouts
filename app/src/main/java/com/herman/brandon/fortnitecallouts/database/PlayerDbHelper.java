package com.herman.brandon.fortnitecallouts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayerDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_PLAYER =
            "CREATE TABLE " + Player.PlayerEntry.TABLE_NAME + " (" +
                    Player.PlayerEntry._ID + " INTEGER PRIMARY KEY," +
                    Player.PlayerEntry.COLUMN_NAME_EPIC_ACCOUNT_NAME + " TEXT," +
                    Player.PlayerEntry.COLUMN_NAME_IS_FRIEND + " INTEGER)";

    private static final String SQL_DELETE_PLAYER =
            "DROP TABLE IF EXISTS " + Player.PlayerEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Player.db";

    public PlayerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PLAYER);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_PLAYER);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
