package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        //dbHelper.close();
    }

    public long insertDiaryEntry(String title, String content, long date, String tags, String location, int categoryId, String userId, String imagePath, String videoPath) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_TAGS, tags);
        values.put(DatabaseHelper.COLUMN_LOCATION, location);
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, imagePath);
        values.put(DatabaseHelper.COLUMN_VIDEO_PATH, videoPath); // 添加视频路径

        return database.insert(DatabaseHelper.TABLE_DIARY_ENTRY, null, values);
    }


    // DiaryEntry（删除）
    public int deleteDiaryEntryById(long entryId) {
        return database.delete(DatabaseHelper.TABLE_DIARY_ENTRY, DatabaseHelper.COLUMN_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
    }

    public int deleteDiaryEntryByCondition(String selection, String[] selectionArgs) {
        return database.delete(DatabaseHelper.TABLE_DIARY_ENTRY, selection, selectionArgs);
    }

    public int updateDiaryEntry(long entryId, String title, String content, long date, String tags, String location, int categoryId, String userId, String imagePath, String videoPath) {
        open(); // 确保数据库连接是打开的
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_TAGS, tags);
        values.put(DatabaseHelper.COLUMN_LOCATION, location);
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, imagePath);
        values.put(DatabaseHelper.COLUMN_VIDEO_PATH, videoPath); // 添加视频路径

        int result =  database.update(DatabaseHelper.TABLE_DIARY_ENTRY, values, DatabaseHelper.COLUMN_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
        close();
        return result;
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

    public int updateUserSettings(String userId, String password, String themePreference, int cloudSyncStatus) {
        ContentValues values = new ContentValues();
        if (password != null && !password.isEmpty()) {
            values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        }
        if (themePreference != null) {
            values.put(DatabaseHelper.COLUMN_THEME_PREFERENCE, themePreference);
        }
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

    // 更新日记条目的字体大小
    public int updateDiaryEntryFontSize(long entryId, String fontSize) {
        open(); // 确保数据库连接是打开的
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FONT_SIZE, fontSize);

        return database.update(DatabaseHelper.TABLE_DIARY_ENTRY, values, DatabaseHelper.COLUMN_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
    }

    // 更新日记条目的字体颜色
    public int updateDiaryEntryFontColor(long entryId, String fontColor) {
        open(); // 确保数据库连接是打开的
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FONT_COLOR, fontColor);

        return database.update(DatabaseHelper.TABLE_DIARY_ENTRY, values, DatabaseHelper.COLUMN_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
    }

    public String queryFontSizeByDiaryEntryId(long diaryEntryId) {
        String fontSize = null;
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_DIARY_ENTRY,
                new String[]{DatabaseHelper.FONT_SIZE}, // 仅选择字体大小列
                DatabaseHelper.COLUMN_ENTRY_ID + " = ?", // WHERE子句
                new String[]{String.valueOf(diaryEntryId)}, // WHERE子句的参数
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.FONT_SIZE);
            if (columnIndex >= 0) {
                fontSize = cursor.getString(columnIndex);
            } else {
                // 处理列不存在的错误情况
                // 例如，你可以记录一条日志或抛出一个异常
                Log.e("DatabaseError", "Column " + DatabaseHelper.FONT_SIZE + " does not exist.");
            }
            cursor.close();
        }

        return fontSize;
    }

    public String queryFontColorByDiaryEntryId(long diaryEntryId) {
        String fontColor = null;
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_DIARY_ENTRY,
                new String[]{DatabaseHelper.FONT_COLOR}, // 仅选择字体颜色列
                DatabaseHelper.COLUMN_ENTRY_ID + " = ?", // WHERE子句
                new String[]{String.valueOf(diaryEntryId)}, // WHERE子句的参数
                null, null, null
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DatabaseHelper.FONT_COLOR);
                if (columnIndex >= 0) {
                    fontColor = cursor.getString(columnIndex);
                } else {
                    // 处理列不存在的错误情况
                    Log.e("DatabaseError", "Column " + DatabaseHelper.FONT_COLOR + " does not exist.");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return fontColor;
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