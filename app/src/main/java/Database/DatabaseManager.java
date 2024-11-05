package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 此类主要是所有增删改查的方法
 */

public class DatabaseManager {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 打开数据库连接，获取一个可写的数据库
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // 关闭数据库连接，释放资源
    public void close() {
        dbHelper.close();
    }

    // DiaryEntry（增）
    public long insertDiaryEntry(String title, String content, long date, String tags, String location, int categoryId, String userId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_TAGS, tags);
        values.put(DatabaseHelper.COLUMN_LOCATION, location);
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);

        return database.insert(DatabaseHelper.TABLE_DIARY_ENTRY, null, values);
    }

    // DiaryEntry（删除）
    public int deleteDiaryEntryById(long entryId) {
        return database.delete(DatabaseHelper.TABLE_DIARY_ENTRY, DatabaseHelper.COLUMN_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
    }

    public int deleteDiaryEntryByCondition(String selection, String[] selectionArgs) {
        return database.delete(DatabaseHelper.TABLE_DIARY_ENTRY, selection, selectionArgs);
    }

    // DiaryEntry（改）
    public int updateDiaryEntry(long entryId, String title, String content, long date, String tags, String location, int categoryId, String userId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_TAGS, tags);
        values.put(DatabaseHelper.COLUMN_LOCATION, location);
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);

        return database.update(DatabaseHelper.TABLE_DIARY_ENTRY, values, DatabaseHelper.COLUMN_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
    }

    // DiaryEntry（查）
    public Cursor getAllDiaryEntries() {
        return database.query(DatabaseHelper.TABLE_DIARY_ENTRY, null, null, null, null, null, null);
    }

    public Cursor queryDiaryEntries(String selection, String[] selectionArgs) {
        return database.query(DatabaseHelper.TABLE_DIARY_ENTRY, null, selection, selectionArgs, null, null, null);
    }

    // 根据UserID查询DiaryEntry
    public Cursor queryDiaryEntriesByUserId(String userId) {
        String selection = DatabaseHelper.COLUMN_USER_ID_FK + " = ?";
        String[] selectionArgs = new String[]{userId};
        return database.query(DatabaseHelper.TABLE_DIARY_ENTRY, null, selection, selectionArgs, null, null, null);
    }

    // UserSettings（改）
    public int updateUserSettings(String userId, String password, String themePreference, int cloudSyncStatus) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_THEME_PREFERENCE, themePreference);
        values.put(DatabaseHelper.COLUMN_CLOUD_SYNC_STATUS, cloudSyncStatus);

        return database.update(DatabaseHelper.TABLE_USER_SETTINGS, values, DatabaseHelper.COLUMN_USER_ID + " = ?", new String[]{userId});
    }


    // 以下三个方法为UserSettings（查）
    public Cursor getAllUserSettings() {
        return database.query(DatabaseHelper.TABLE_USER_SETTINGS, null, null, null, null, null, null);
    }


    public Cursor queryUserSettings(String selection, String[] selectionArgs) {
        return database.query(DatabaseHelper.TABLE_USER_SETTINGS, null, selection, selectionArgs, null, null, null);
    }


    // 查询并检查用户的ID和密码是否存在
    public boolean checkUserCredentials(String userId, String password) {
        // 设置查询条件
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {userId, password};

        // 查询数据库
        try (Cursor cursor = queryUserSettings(selection, selectionArgs)) {
            // 判断是否有结果，如果有则表示凭据有效
            return cursor != null && cursor.getCount() > 0;
        }
    }

}