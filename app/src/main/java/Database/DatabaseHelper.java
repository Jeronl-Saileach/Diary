package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 创建 DiaryEntry 表
    public static final String CREATE_DairyEntry ="create table DairyEntry("
            + "EntryId integer primary key autoincrement,"
            + "Title text,"
            + "Content text,"
            + "Date integer,"
            + "Media text,"
            + "Tags text,"
            + "Location text)";

    // 创建 Media 表
    public static final String CREATE_Media = "create table Media("
            + "MediaID integer primary key autoincrement,"
            + "Type text,"
            + "FilePath text,"
            + "EntryID integer,"
            + "foreign key(EntryID) references DiaryEntry(EntryId))";

    // 创建 Tags 表
    public static final String CREATE_Tags = "create table Tags("
            + "TagID integer primary key autoincrement,"
            + "TagName text,"
            + "EntryID integer,"
            + "foreign key(EntryID) references DiaryEntry(EntryId))";

    // 创建 Categories 表
    public static final String CREATE_Categories = "create table Categories("
            + "CategoryID integer primary key autoincrement,"
            + "CategoryName text,"
            + "EntryID integer,"
            + "foreign key(EntryID) references DiaryEntry(EntryId))";

    // 创建 UserSettings 表
    public static final String CREATE_UserSettings = "create table UserSettings("
            + "UserID integer primary key autoincrement,"
            + "PasswordProtection integer,"
            + "ThemePreference text,"
            + "CloudSyncStatus integer)";

    private Context mContext;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DairyEntry);
        db.execSQL(CREATE_Media);
        db.execSQL(CREATE_Tags);
        db.execSQL(CREATE_Categories);
        db.execSQL(CREATE_UserSettings);
        Toast.makeText(mContext,"Create Succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}