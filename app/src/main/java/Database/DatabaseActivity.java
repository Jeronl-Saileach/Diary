package Database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.util.Log;

import com.example.smdiary.R;
/*
import com.sqlitestudioremote.SQLiteStudioService;
*/


public class DatabaseActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database); // 你的布局文件
        /*SQLiteStudioService.instance().start(this);*/

        // 初始化数据库帮助类
        dbHelper = new DatabaseHelper(this, "Dairy.db", null, 1);

        // 获取按钮并设置点击事件
        Button createDatabase=(Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当按钮点击时，创建数据库
                dbHelper.getWritableDatabase();
                Toast.makeText(DatabaseActivity.this, "Database Created", Toast.LENGTH_SHORT).show();
            }
        });

        // 添加按钮并向表中添加信息
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                insertIntoDiaryEntry(db);
                insertIntoMedia(db);
                insertIntoTags(db);
                insertIntoCategories(db);
                insertIntoUserSettings(db);
            }
        });

        //查找按钮查找表中信息
        Button queryData = findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                selectFromDiaryEntry(db);
                selectFromMedia(db);
                selectFromTags(db);
                selectFromCategories(db);
                selectFromUserSettings(db);
            }
        });

    }
    // 插入 DiaryEntry 表数据
    private void insertIntoDiaryEntry(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put("Title", "My First Diary");
        values1.put("Content", "Today was a great day.");
        values1.put("Date", System.currentTimeMillis());
        values1.put("Media", "media1.jpg");
        values1.put("Tags", "happy,fun");
        values1.put("Location", "Park");
        long result1 = db.insert("DiaryEntry", null, values1);
        showToast(result1, "Diary Entry 1");

        ContentValues values2 = new ContentValues();
        values2.put("Title", "A Rainy Day");
        values2.put("Content", "It rained heavily today.");
        values2.put("Date", System.currentTimeMillis());
        values2.put("Media", "media2.jpg");
        values2.put("Tags", "rain,cozy");
        values2.put("Location", "Home");
        long result2 = db.insert("DiaryEntry", null, values2);
        showToast(result2, "Diary Entry 2");

        ContentValues values3 = new ContentValues();
        values3.put("Title", "Mountain Hike");
        values3.put("Content", "Hiking in the mountains was exhausting but rewarding.");
        values3.put("Date", System.currentTimeMillis());
        values3.put("Media", "media3.jpg");
        values3.put("Tags", "adventure,exercise");
        values3.put("Location", "Mountain");
        long result3 = db.insert("DiaryEntry", null, values3);
        showToast(result3, "Diary Entry 3");
    }

    // 插入 Media 表数据
    private void insertIntoMedia(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put("Type", "image");
        values1.put("FilePath", "path/to/image1.jpg");
        values1.put("EntryID", 1);
        long result1 = db.insert("Media", null, values1);
        showToast(result1, "Media 1");

        ContentValues values2 = new ContentValues();
        values2.put("Type", "video");
        values2.put("FilePath", "path/to/video1.mp4");
        values2.put("EntryID", 2);
        long result2 = db.insert("Media", null, values2);
        showToast(result2, "Media 2");

        ContentValues values3 = new ContentValues();
        values3.put("Type", "audio");
        values3.put("FilePath", "path/to/audio1.mp3");
        values3.put("EntryID", 3);
        long result3 = db.insert("Media", null, values3);
        showToast(result3, "Media 3");
    }

    // 插入 Tags 表数据
    private void insertIntoTags(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put("TagName", "happy");
        values1.put("EntryID", 1);
        long result1 = db.insert("Tags", null, values1);
        showToast(result1, "Tag 1");

        ContentValues values2 = new ContentValues();
        values2.put("TagName", "cozy");
        values2.put("EntryID", 2);
        long result2 = db.insert("Tags", null, values2);
        showToast(result2, "Tag 2");

        ContentValues values3 = new ContentValues();
        values3.put("TagName", "adventure");
        values3.put("EntryID", 3);
        long result3 = db.insert("Tags", null, values3);
        showToast(result3, "Tag 3");
    }

    // 插入 Categories 表数据
    private void insertIntoCategories(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put("CategoryName", "Personal");
        values1.put("EntryID", 1);
        long result1 = db.insert("Categories", null, values1);
        showToast(result1, "Category 1");

        ContentValues values2 = new ContentValues();
        values2.put("CategoryName", "Work");
        values2.put("EntryID", 2);
        long result2 = db.insert("Categories", null, values2);
        showToast(result2, "Category 2");

        ContentValues values3 = new ContentValues();
        values3.put("CategoryName", "Travel");
        values3.put("EntryID", 3);
        long result3 = db.insert("Categories", null, values3);
        showToast(result3, "Category 3");
    }

    // 插入 UserSettings 表数据
    private void insertIntoUserSettings(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put("PasswordProtection", 1);
        values1.put("ThemePreference", "Dark");
        values1.put("CloudSyncStatus", 0);
        long result1 = db.insert("UserSettings", null, values1);
        showToast(result1, "User Setting 1");

        ContentValues values2 = new ContentValues();
        values2.put("PasswordProtection", 0);
        values2.put("ThemePreference", "Light");
        values2.put("CloudSyncStatus", 1);
        long result2 = db.insert("UserSettings", null, values2);
        showToast(result2, "User Setting 2");

        ContentValues values3 = new ContentValues();
        values3.put("PasswordProtection", 1);
        values3.put("ThemePreference", "Auto");
        values3.put("CloudSyncStatus", 1);
        long result3 = db.insert("UserSettings", null, values3);
        showToast(result3, "User Setting 3");
    }

    // 显示插入结果的 Toast
    private void showToast(long result, String tableName) {
        if (result != -1) {
            Toast.makeText(this, tableName + " Inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to Insert " + tableName, Toast.LENGTH_SHORT).show();
        }
    }


    private void selectFromDiaryEntry(SQLiteDatabase db) {
        Cursor cursor = db.query("DiaryEntry", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("Title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                long date = cursor.getLong(cursor.getColumnIndexOrThrow("Date"));
                String media = cursor.getString(cursor.getColumnIndexOrThrow("Media"));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow("Tags"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("Location"));

                Log.d("DiaryEntry", "Title: " + title + ", Content: " + content + ", Date: " + date + ", Media: " + media + ", Tags: " + tags + ", Location: " + location);
            } while (cursor.moveToNext());
        } else {
            Log.d("DiaryEntry", "No entries found");
        }
        cursor.close();
    }

    private void selectFromMedia(SQLiteDatabase db) {
        Cursor cursor = db.query("Media", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow("Type"));
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow("FilePath"));
                int entryId = cursor.getInt(cursor.getColumnIndexOrThrow("EntryID"));

                Log.d("Media", "Type: " + type + ", FilePath: " + filePath + ", EntryID: " + entryId);
            } while (cursor.moveToNext());
        } else {
            Log.d("Media", "No entries found");
        }
        cursor.close();
    }

    private void selectFromTags(SQLiteDatabase db) {
        Cursor cursor = db.query("Tags", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String tagName = cursor.getString(cursor.getColumnIndexOrThrow("TagName"));
                int entryId = cursor.getInt(cursor.getColumnIndexOrThrow("EntryID"));

                Log.d("Tags", "TagName: " + tagName + ", EntryID: " + entryId);
            } while (cursor.moveToNext());
        } else {
            Log.d("Tags", "No entries found");
        }
        cursor.close();
    }

    private void selectFromCategories(SQLiteDatabase db) {
        Cursor cursor = db.query("Categories", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("CategoryName"));
                int entryId = cursor.getInt(cursor.getColumnIndexOrThrow("EntryID"));

                Log.d("Categories", "CategoryName: " + categoryName + ", EntryID: " + entryId);
            } while (cursor.moveToNext());
        } else {
            Log.d("Categories", "No entries found");
        }
        cursor.close();
    }

    private void selectFromUserSettings(SQLiteDatabase db) {
        Cursor cursor = db.query("UserSettings", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int passwordProtection = cursor.getInt(cursor.getColumnIndexOrThrow("PasswordProtection"));
                String themePreference = cursor.getString(cursor.getColumnIndexOrThrow("ThemePreference"));
                int cloudSyncStatus = cursor.getInt(cursor.getColumnIndexOrThrow("CloudSyncStatus"));

                Log.d("UserSettings", "PasswordProtection: " + passwordProtection + ", ThemePreference: " + themePreference + ", CloudSyncStatus: " + cloudSyncStatus);
            } while (cursor.moveToNext());
        } else {
            Log.d("UserSettings", "No entries found");
        }
        cursor.close();
    }


}