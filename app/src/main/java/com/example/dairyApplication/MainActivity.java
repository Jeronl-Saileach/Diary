package com.example.dairyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.smdiary.R;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import Database.DatabaseHelper;
import Database.DatabaseManager;


// 主活动
public class MainActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;  // 用于与数据库交互
    private ListView diaryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diaryListView = findViewById(R.id.diaryListView);  // 获取ListView
        databaseManager = new DatabaseManager(this);
        databaseManager.open();  // 打开数据库连接

        FloatingActionButton addDiaryEntryButton = findViewById(R.id.addDiaryEntryButton);
        ImageButton viewProfileButton = findViewById(R.id.viewProfileButton);
        ImageButton editSettingButton = findViewById(R.id.editSettingButton);

        // 处理添加日记条目按钮点击事件
        addDiaryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
                startActivity(intent);  // 启动新日记条目活动
            }
        });

        // 加载并显示所有日记条目
        loadDiaryEntries();
    }

    private void loadDiaryEntries() {
        // 查询数据库中的所有日记条目
        Cursor cursor = databaseManager.getAllDiaryEntries();
        ArrayList<String> diaryEntries = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
                diaryEntries.add("标题: " + title + "\n内容: " + content);
            }
            cursor.close();
        }

        // 使用ArrayAdapter将数据绑定到ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diaryEntries);
        diaryListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseManager.close();  // 关闭数据库连接
    }
}