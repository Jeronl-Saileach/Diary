package com.example.dairyApplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smdiary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Database.DatabaseHelper;
import Database.DatabaseManager;

import androidx.appcompat.widget.SearchView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private RecyclerView diaryRecyclerView;
    private DiaryAdapter adapter;
    private static final int REQUEST_CODE_EDIT_DIARY = 1;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("USER_ID"); // 获取用户 ID

        if (userID == null) {
            Log.e("MainActivity", "User ID is null");
        }

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        diaryRecyclerView = findViewById(R.id.diaryListView);
        SearchView searchView = findViewById(R.id.searchView);
        FloatingActionButton addDiaryEntryButton = findViewById(R.id.addDiaryEntryButton);
        ImageButton viewProfileButton = findViewById(R.id.viewProfileButton);
        ImageButton editSettingButton = findViewById(R.id.editSettingButton);

        // 添加新日记按钮事件
        addDiaryEntryButton.setOnClickListener(v -> {
            Intent addDiaryIntent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            addDiaryIntent.putExtra("USER_ID", userID);
            startActivityForResult(addDiaryIntent, REQUEST_CODE_EDIT_DIARY);
        });

        // 个人信息按钮事件
        viewProfileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        // 设置按钮事件
        editSettingButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            settingsIntent.putExtra("USER_ID", userID); // 确保传递用户 ID
            startActivity(settingsIntent);
        });

        loadDiaryEntries();

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

    private void loadDiaryEntries() {
        Cursor cursor = databaseManager.queryDiaryEntriesByUserId(userID); // 根据用户 ID 查询

        adapter = new DiaryAdapter(this, cursor, entryId -> {
            Intent editDiaryIntent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            editDiaryIntent.putExtra("entryId", entryId);
            editDiaryIntent.putExtra("USER_ID", userID);
            startActivityForResult(editDiaryIntent, REQUEST_CODE_EDIT_DIARY);
        });

        diaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        diaryRecyclerView.setAdapter(adapter);
    }

    private void searchDiaryEntries(String query) {
        String selection = DatabaseHelper.COLUMN_USER_ID_FK + " = ? AND (" +
                DatabaseHelper.COLUMN_TITLE + " LIKE ? OR " +
                "strftime('%Y-%m-%d', datetime(" + DatabaseHelper.COLUMN_DATE + "/1000, 'unixepoch')) LIKE ? OR " +
                DatabaseHelper.COLUMN_CONTENT + " LIKE ?)";

        String[] selectionArgs = new String[]{
                userID,
                "%" + query + "%",
                "%" + query + "%"
        };

        Cursor cursor = databaseManager.queryDiaryEntries(selection, selectionArgs);

        adapter = new DiaryAdapter(this, cursor, entryId -> {
            Intent editDiaryIntent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            editDiaryIntent.putExtra("entryId", entryId);
            editDiaryIntent.putExtra("USER_ID", userID); // 传递用户 ID
            startActivityForResult(editDiaryIntent, REQUEST_CODE_EDIT_DIARY);
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
