package com.example.dairyApplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "diary.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";

    public static final String TABLE_DIARY = "diary";
    public static final String COLUMN_DIARY_ID = "_id";
    public static final String COLUMN_DIARY_TITLE = "title";
    public static final String COLUMN_DIARY_CONTENT = "content";
    public static final String COLUMN_DIARY_TIMESTAMP = "timestamp";
    public static final String COLUMN_USER_ID_FK = "user_id";

    public DiaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT"
                + ")";

        String CREATE_DIARY_TABLE = "CREATE TABLE " + TABLE_DIARY + "("
                + COLUMN_DIARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DIARY_TITLE + " TEXT,"
                + COLUMN_DIARY_CONTENT + " TEXT,"
                + COLUMN_DIARY_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_USER_ID_FK + " INTEGER,"
                + " FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
                + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_DIARY_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY);

        onCreate(db);
    }

    // Add user method
    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, username);
        values.put(COLUMN_USER_PASSWORD, password);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Add diary entry method
    public void addDiaryEntry(String title, String content, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DIARY_TITLE, title);
        values.put(COLUMN_DIARY_CONTENT, content);
        values.put(COLUMN_USER_ID_FK, userId);

        db.insert(TABLE_DIARY, null, values);
        db.close();
    }

}