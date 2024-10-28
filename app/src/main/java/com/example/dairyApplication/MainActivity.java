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

import Database.DatabaseHelper;
import Database.DatabaseManager;

import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private RecyclerView diaryRecyclerView;
    private DiaryAdapter adapter;
    private static final int REQUEST_CODE_EDIT_DIARY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 打开数据库连接
        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        diaryRecyclerView = findViewById(R.id.diaryListView);
        SearchView searchView = findViewById(R.id.searchView);
        FloatingActionButton addDiaryEntryButton = findViewById(R.id.addDiaryEntryButton);
        ImageButton viewProfileButton = findViewById(R.id.viewProfileButton);
        ImageButton editSettingButton = findViewById(R.id.editSettingButton);

        // 添加新日记按钮事件
        addDiaryEntryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            startActivityForResult(intent, REQUEST_CODE_EDIT_DIARY);
        });

        // 个人信息按钮事件
        viewProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // 设置按钮事件
        editSettingButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // 加载并显示所有日记条目
        loadDiaryEntries();

        // 设置搜索栏的查询监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDiaryEntries(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDiaryEntries(newText);
                return false;
            }
        });
    }

    // 加载所有日记条目
    private void loadDiaryEntries() {
        Cursor cursor = databaseManager.getAllDiaryEntries();
        adapter = new DiaryAdapter(this, cursor, entryId -> {
            Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            intent.putExtra("entryId", entryId);
            startActivityForResult(intent, REQUEST_CODE_EDIT_DIARY);
        });

        diaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        diaryRecyclerView.setAdapter(adapter);
    }

    // 根据关键字搜索日记条目
    private void searchDiaryEntries(String query) {
        String selection = DatabaseHelper.COLUMN_TITLE + " LIKE ? OR " +
                DatabaseHelper.COLUMN_CONTENT + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};
        Cursor cursor = databaseManager.queryDiaryEntries(selection, selectionArgs);
        adapter = new DiaryAdapter(this, cursor, entryId -> {
            Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            intent.putExtra("entryId", entryId);
            startActivityForResult(intent, REQUEST_CODE_EDIT_DIARY);
        });
        diaryRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_DIARY && resultCode == RESULT_OK) {
            loadDiaryEntries();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}
