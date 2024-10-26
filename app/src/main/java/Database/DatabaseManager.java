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


    /**
     * DiaryEntry的增删改查
     * 其中，删除方法包含一个依据主键删除，和依据任意一属性删除
     * 其中，查询方法包含一个遍历所有属性查询，和依据任意一属性查询
     * 后面几个表也是一样，之后不再赘述。
     */
    // DiaryEntry（增）
    // DiaryEntry（增）
    public long insertDiaryEntry(String title, String content, long date, String tags, String location, int categoryId) {
        ContentValues values = new ContentValues();
        // 不需要添加 EntryId，因为它会自动生成
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_TAGS, tags);
        values.put(DatabaseHelper.COLUMN_LOCATION, location);
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, categoryId);

        // 执行插入并返回新插入行的ID
        return database.insert(DatabaseHelper.TABLE_DIARY_ENTRY, null, values);
    }



    // DiaryEntry（删除）
    // 通过EntryId删除特定日记条目
    public int deleteDiaryEntryById(long entryId) {
        // 执行删除操作，通过EntryId指定要删除的行
        return database.delete(DatabaseHelper.TABLE_DIARY_ENTRY, DatabaseHelper.COLUMN_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
    }

    // 根据特定条件删除DiaryEntry
    public int deleteDiaryEntryByCondition(String selection, String[] selectionArgs) {
        // 根据条件删除记录，selection和selectionArgs定义了删除的条件
        // 可以根据传入的条件进行删除操作。例如，可以通过日期、标签或其他属性来指定要删除的记录。
        return database.delete(DatabaseHelper.TABLE_DIARY_ENTRY, selection, selectionArgs);
    }


    // DiaryEntry（改）
    public int updateDiaryEntry(long entryId, String title, String content, long date, String tags, String location, int categoryId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_TAGS, tags);
        values.put(DatabaseHelper.COLUMN_LOCATION, location);
        values.put(DatabaseHelper.COLUMN_CATEGORY_ID, categoryId);

        // 更新指定EntryId的记录，并返回受影响的行数
        return database.update(DatabaseHelper.TABLE_DIARY_ENTRY, values, DatabaseHelper.COLUMN_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
    }

    // DiaryEntry（查）
    // 获取所有日记条目
    public Cursor getAllDiaryEntries() {
        // 查询表中所有数据，返回Cursor对象以遍历结果
        return database.query(DatabaseHelper.TABLE_DIARY_ENTRY, null, null, null, null, null, null);
    }

    // 根据指定条件查询DiaryEntry
    public Cursor queryDiaryEntries(String selection, String[] selectionArgs) {
        // 根据条件查询数据，并返回包含结果的Cursor对象
        // 可以基于任何属性组合查询条目。通过selection和selectionArgs，可以实现灵活的查询。
        // 例如：
        // 查询所有在特定日期的条目
        // 根据标签查询
        return database.query(DatabaseHelper.TABLE_DIARY_ENTRY, null, selection, selectionArgs, null, null, null);
    }
    //selection：这是一段SQL样式的条件语句（类似于WHERE子句，但不需要包含WHERE关键字本身）。该条件使用占位符（例如?）来表示需要填入具体值的位置
    //selectionArgs：这是一个字符串数组，与selection匹配。数组中的每个值将依次替换selection中的问号（?）占位符。


    /**
     * Category的增删改查
     */
    // Category（增）
    public long insertCategory(String categoryName, int userId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, categoryName);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);

        return database.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
    }

    // Category（删）
    public int deleteCategory(long categoryId) {
        return database.delete(DatabaseHelper.TABLE_CATEGORIES, DatabaseHelper.COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
    }
    // 根据特定条件删除Category
    public int deleteCategoryByCondition(String selection, String[] selectionArgs) {
        return database.delete(DatabaseHelper.TABLE_CATEGORIES, selection, selectionArgs);
    }

    // Category（改）
    public int updateCategory(long categoryId, String categoryName, int userId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, categoryName);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);

        return database.update(DatabaseHelper.TABLE_CATEGORIES, values, DatabaseHelper.COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
    }

    // Category（查）
    public Cursor getAllCategories() {
        return database.query(DatabaseHelper.TABLE_CATEGORIES, null, null, null, null, null, null);
    }
    public Cursor queryCategories(String selection, String[] selectionArgs) {
        return database.query(DatabaseHelper.TABLE_CATEGORIES, null, selection, selectionArgs, null, null, null);
    }


    /**
     * Media的增删改查
     */
    // Media（增）
    public long insertMedia(String type, String filePath, int entryId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TYPE, type);
        values.put(DatabaseHelper.COLUMN_FILE_PATH, filePath);
        values.put(DatabaseHelper.COLUMN_ENTRY_ID_FK, entryId);

        return database.insert(DatabaseHelper.TABLE_MEDIA, null, values);
    }

    // Media（删）
    public int deleteMedia(long mediaId) {
        return database.delete(DatabaseHelper.TABLE_MEDIA, DatabaseHelper.COLUMN_MEDIA_ID + " = ?", new String[]{String.valueOf(mediaId)});
    }
    // 根据特定条件删除Media
    public int deleteMediaByCondition(String selection, String[] selectionArgs) {
        return database.delete(DatabaseHelper.TABLE_MEDIA, selection, selectionArgs);
    }

    // Media（改）
    public int updateMedia(long mediaId, String type, String filePath, int entryId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TYPE, type);
        values.put(DatabaseHelper.COLUMN_FILE_PATH, filePath);
        values.put(DatabaseHelper.COLUMN_ENTRY_ID_FK, entryId);

        return database.update(DatabaseHelper.TABLE_MEDIA, values, DatabaseHelper.COLUMN_MEDIA_ID + " = ?", new String[]{String.valueOf(mediaId)});
    }

    // Media（查）
    public Cursor getAllMedia() {
        return database.query(DatabaseHelper.TABLE_MEDIA, null, null, null, null, null, null);
    }
    public Cursor queryMedia(String selection, String[] selectionArgs) {
        return database.query(DatabaseHelper.TABLE_MEDIA, null, selection, selectionArgs, null, null, null);
    }


    /**
     * UserSettings的增删改查
     */


    // UserSettings（通过id和密码增加用户）
    public long addUserWithCredentials(int userId, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_ID, userId);        // 设置用户ID
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);     // 设置密码

        // 其他字段暂时为空或默认值
        values.putNull(DatabaseHelper.COLUMN_THEME_PREFERENCE);   // 主题偏好为空
        values.put(DatabaseHelper.COLUMN_CLOUD_SYNC_STATUS, 0);   // 云同步状态默认为0

        return database.insert(DatabaseHelper.TABLE_USER_SETTINGS, null, values);
    }

    // UserSettings（在设置中补充更多信息）
    public long insertUserSettings(String password, String themePreference, int cloudSyncStatus) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_THEME_PREFERENCE, themePreference);
        values.put(DatabaseHelper.COLUMN_CLOUD_SYNC_STATUS, cloudSyncStatus);

        return database.insert(DatabaseHelper.TABLE_USER_SETTINGS, null, values);
    }

    // UserSettings（删）
    public int deleteUserSettings(long userId) {
        return database.delete(DatabaseHelper.TABLE_USER_SETTINGS, DatabaseHelper.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }
    // 根据特定条件删除UserSettings
    public int deleteUserSettingsByCondition(String selection, String[] selectionArgs) {
        return database.delete(DatabaseHelper.TABLE_USER_SETTINGS, selection, selectionArgs);
    }

    // UserSettings（改）
    public int updateUserSettings(long userId, String password, String themePreference, int cloudSyncStatus) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_THEME_PREFERENCE, themePreference);
        values.put(DatabaseHelper.COLUMN_CLOUD_SYNC_STATUS, cloudSyncStatus);

        return database.update(DatabaseHelper.TABLE_USER_SETTINGS, values, DatabaseHelper.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
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