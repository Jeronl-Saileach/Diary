package com.example.dairyApplication.Model;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

public class testDatabase extends AppCompatActivity {

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);

        databaseManager = new DatabaseManager(this);

        Button insertButton = findViewById(R.id.testInsertButton);
        Button deleteButton = findViewById(R.id.testDeleteButton);
        Button deleteButtonC = findViewById(R.id.testDeleteButtonC);
        Button updateButton = findViewById(R.id.testUpdateButton);
        Button queryButtonA = findViewById(R.id.testQueryButtonA);
        Button queryButtonE = findViewById(R.id.testQueryButtonE);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testInsertDiaryEntry();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDeleteById();
            }
        });

        deleteButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDeleteByCondition();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testUpdate();
            }
        });

        queryButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testQueryAllDiaryEntries();
            }
        });

        queryButtonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testQueryDiaryEntries();
            }
        });
    }

    private void testInsertDiaryEntry() {
        try {
            databaseManager.open();

            String title = "DairyEntry的插入测试";
            String content = "主要的内容是要测试dairyentry中的增方法是否能够正常实现";
            long date = System.currentTimeMillis(); // 使用当前时间戳
            String tags = "测试";
            String location = "四教";
            int categoryId = 1;
            String userId = "2"; // 根据上下文来定义用户ID

            String imagePath = ""; // 设置为图片路径，例如："/path/to/image"
            String videoPath = ""; // 设置为视频路径，例如："/path/to/video"

            long newEntryId = databaseManager.insertDiaryEntry(title, content, date, tags, location, categoryId, userId, imagePath, videoPath);

            if (newEntryId != -1) {
                Toast.makeText(this, "插入成功，ID: " + newEntryId, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DatabaseActivity", "插入操作错误", e);
        } finally {
            databaseManager.close();
        }
    }

    private void testDeleteById() {
        try {
            databaseManager.open();
            long entryId = 1; // 确保您使用的 ID 是正确的
            int rowsDeleted = databaseManager.deleteDiaryEntryById(entryId);
            if (rowsDeleted > 0) {
                Toast.makeText(this, "ID删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "没有找到ID匹配条目", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DatabaseActivity", "ID删除操作错误", e);
        } finally {
            databaseManager.close();
        }
    }

    private void testDeleteByCondition() {
        try {
            databaseManager.open();
            String selection = DatabaseHelper.COLUMN_TAGS + " = ?";
            String[] selectionArgs = {"测试"};
            int rowsDeleted = databaseManager.deleteDiaryEntryByCondition(selection, selectionArgs);
            if (rowsDeleted > 0) {
                Toast.makeText(this, "条件删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "没有找到符合条件的条目", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DatabaseActivity", "条件删除操作错误", e);
        } finally {
            databaseManager.close();
        }
    }

    private void testUpdate() {
        try {
            databaseManager.open();
            long entryId = 1; // 确保您使用的 ID 是正确的
            String title = "DairyEntry的更新测试";
            String content = "主要的内容是要测试dairyentry中的改方法是否能够正常实现";
            long date = System.currentTimeMillis(); // 使用当前时间戳
            String tags = "测试";
            String location = "四教";
            int categoryId = 2;
            String userId = "3"; // 使用适当的用户ID

            String imagePath = ""; // 更新时的图片路径，例如："/path/to/image"
            String videoPath = ""; // 更新时的 视频路径，例如："/path/to/video"

            int rowsUpdated = databaseManager.updateDiaryEntry(
                    entryId,
                    title,
                    content,
                    date,
                    tags,
                    location,
                    categoryId,
                    userId,
                    imagePath,  // 更新图片路径
                    videoPath   // 更新视频路径
            );

            if (rowsUpdated > 0) {
                Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "没有找到条目", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DatabaseActivity", "更新操作错误", e);
        } finally {
            databaseManager.close();
        }
    }


    private void testQueryAllDiaryEntries() {
        try {
            databaseManager.open();
            Cursor cursor = databaseManager.getAllDiaryEntries();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long entryId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ENTRY_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
                    Log.d("DatabaseActivity", "Entry ID: " + entryId + ", Title: " + title + ", Content: " + content);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DatabaseActivity", "查询操作错误", e);
        } finally {
            databaseManager.close();
        }
    }

    private void testQueryDiaryEntries() {
        try {
            databaseManager.open();
            String selection = DatabaseHelper.COLUMN_TAGS + " = ?";
            String[] selectionArgs = {"测试"};
            Cursor conditionCursor = databaseManager.queryDiaryEntries(selection, selectionArgs);
            if (conditionCursor != null) {
                while (conditionCursor.moveToNext()) {
                    long entryId = conditionCursor.getLong(conditionCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ENTRY_ID));
                    String title = conditionCursor.getString(conditionCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                    String content = conditionCursor.getString(conditionCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
                    Log.d("DatabaseActivity", "条件结果 - Entry ID: " + entryId + ", Title: " + title + ", Content: " + content);
                }
                conditionCursor.close();
            }
        } catch (Exception e) {
            Log.e("DatabaseActivity", "查询操作错误", e);
        } finally {
            databaseManager.close();
        }
    }
}
