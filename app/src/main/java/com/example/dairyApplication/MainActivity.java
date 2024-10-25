package com.example.dairyApplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smdiary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;

import Database.DatabaseManager;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private RecyclerView diaryRecyclerView;
    private static final int REQUEST_CODE_EDIT_DIARY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 打开数据库连接
        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        diaryRecyclerView = findViewById(R.id.diaryListView);
        FloatingActionButton addDiaryEntryButton = findViewById(R.id.addDiaryEntryButton);
        ImageButton viewProfileButton = findViewById(R.id.viewProfileButton);
        ImageButton editSettingButton = findViewById(R.id.editSettingButton);

        // 添加新日记按钮事件
        addDiaryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_DIARY); // 使用请求码启动Activity
            }
        });

        // 个人信息按钮事件
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // 设置按钮事件
        editSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // 加载并显示所有日记条目
        loadDiaryEntries();
    }

    private void loadDiaryEntries() {
        Cursor cursor = databaseManager.getAllDiaryEntries();
        DiaryAdapter adapter = new DiaryAdapter(this, cursor, entryId -> {
            Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            intent.putExtra("entryId", entryId); // 传递 entryId 到编辑页面
            startActivityForResult(intent, REQUEST_CODE_EDIT_DIARY); // 使用请求码启动Activity
        });

        diaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        diaryRecyclerView.setAdapter(adapter);
    }

    // 刷新日记条目列表
    private void refreshDiaryEntries() {
        Cursor cursor = databaseManager.getAllDiaryEntries();
        DiaryAdapter adapter = new DiaryAdapter(this, cursor, entryId -> {
            Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            intent.putExtra("entryId", entryId); // 传递 entryId 到编辑页面
            startActivityForResult(intent, REQUEST_CODE_EDIT_DIARY); // 使用请求码启动Activity
        });
        diaryRecyclerView.setAdapter(adapter);
    }

    // 处理Activity结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_DIARY && resultCode == RESULT_OK) {
            refreshDiaryEntries(); // 更新列表视图
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseManager != null) {
            databaseManager.close();  // 关闭数据库连接
        }
    }
}
