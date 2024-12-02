package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// 数据库助手类
public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库名字及版本
    private static final String DATABASE_NAME = "diary.db";
    private static final int DATABASE_VERSION = 10; // 升级版本号以适应数据库结构的变化

    // DiaryEntry 表常量
    public static final String TABLE_DIARY_ENTRY = "DiaryEntry";
    public static final String COLUMN_ENTRY_ID = "EntryId";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_CONTENT = "Content";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_TAGS = "Tags";
    public static final String COLUMN_LOCATION = "Location";
    public static final String COLUMN_CATEGORY_ID = "CategoryID";
    public static final String COLUMN_IMAGE_PATH = "ImagePath";
    public static final String COLUMN_USER_ID_FK = "UserID"; // 新增的UserID外键

    // Media 表常量
    public static final String TABLE_MEDIA = "Media";
    public static final String COLUMN_MEDIA_ID = "MediaID";
    public static final String COLUMN_TYPE = "Type";
    public static final String COLUMN_FILE_PATH = "FilePath";
    public static final String COLUMN_ENTRY_ID_FK = "EntryID";

    // Categories 表常量
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String COLUMN_CATEGORY_NAME = "CategoryName";

    // UserSettings 表常量
    public static final String TABLE_USER_SETTINGS = "UserSettings";
    public static final String COLUMN_USER_ID = "UserID";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_THEME_PREFERENCE = "ThemePreference";
    public static final String COLUMN_CLOUD_SYNC_STATUS = "CloudSyncStatus";

    // SQL 创建表语句
    private static final String CREATE_DIARY_ENTRY = "CREATE TABLE " + TABLE_DIARY_ENTRY + " ("
            + COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_CONTENT + " TEXT, "
            + COLUMN_DATE + " INTEGER, "
            + COLUMN_TAGS + " TEXT, "
            + COLUMN_LOCATION + " TEXT, "
            + COLUMN_CATEGORY_ID + " INTEGER, "
            + COLUMN_IMAGE_PATH + " TEXT, "
            + COLUMN_USER_ID_FK + " TEXT, " // 新增的UserID列
            + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES "
            + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "), "
            + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
            + TABLE_USER_SETTINGS + "(" + COLUMN_USER_ID + "))";

    private static final String CREATE_MEDIA = "CREATE TABLE " + TABLE_MEDIA + " ("
            + COLUMN_MEDIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TYPE + " TEXT, "
            + COLUMN_FILE_PATH + " TEXT, "
            + COLUMN_ENTRY_ID_FK + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_ENTRY_ID_FK + ") REFERENCES "
            + TABLE_DIARY_ENTRY + "(" + COLUMN_ENTRY_ID + "))";

    private static final String CREATE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + " ("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_NAME + " TEXT)";
    // 移除了UserID外键相关的代码

    private static final String CREATE_USER_SETTINGS = "CREATE TABLE " + TABLE_USER_SETTINGS + " ("
            + COLUMN_USER_ID + " TEXT PRIMARY KEY, "
            + COLUMN_PASSWORD + " TEXT, "
            + COLUMN_THEME_PREFERENCE + " TEXT, "
            + COLUMN_CLOUD_SYNC_STATUS + " INTEGER)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_SETTINGS);
        db.execSQL(CREATE_CATEGORIES);
        db.execSQL(CREATE_DIARY_ENTRY);
        db.execSQL(CREATE_MEDIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果旧版本数据库存在，则删除所有表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY_ENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_SETTINGS);

        // 重新创建表
        onCreate(db);
    }
}