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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private RecyclerView diaryRecyclerView;
    private DiaryAdapter adapter;
    private static final int REQUEST_CODE_EDIT_DIARY = 1;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 在 Activity 的 onCreate 方法中设置标题
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //从LoginActivity中接收UserID
        Intent IDintent = getIntent();
        userID = IDintent.getStringExtra("userID");





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
            // 将用户ID作为额外数据放入Intent
            intent.putExtra("USER_ID", userID);
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



    //基于UserID显示日记条目
    private void loadDiaryEntries() {
        // 使用currentUserId来获取日记条目
        Cursor cursor = databaseManager.queryDiaryEntriesByUserId(userID);

        // 创建适配器并设置点击监听器
        adapter = new DiaryAdapter(this, cursor, entryId -> {
            Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            intent.putExtra("entryId", entryId);
            startActivityForResult(intent, REQUEST_CODE_EDIT_DIARY);
        });

        // 设置RecyclerView的布局管理器和适配器
        diaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        diaryRecyclerView.setAdapter(adapter);
    }




    private void searchDiaryEntries(String query) {
        // 构建SQL查询的选择条件
        String selection = DatabaseHelper.COLUMN_USER_ID_FK + " = ? AND (" +
                DatabaseHelper.COLUMN_TITLE + " LIKE ? OR " +
                DatabaseHelper.COLUMN_CONTENT + " LIKE ?)";

        // 构建选择条件的参数数组
        String[] selectionArgs = new String[]{
                userID,
                "%" + query + "%",
                "%" + query + "%"
        };

        // 使用选择条件和参数查询日记条目
        Cursor cursor = databaseManager.queryDiaryEntries(selection, selectionArgs);

        // 创建适配器并设置点击监听器
        adapter = new DiaryAdapter(this, cursor, entryId -> {
            Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
            intent.putExtra("entryId", entryId);
            // 如果DiaryEntryActivity需要UserID，也可以传递它
            intent.putExtra("userId", userID);
            startActivityForResult(intent, REQUEST_CODE_EDIT_DIARY);
        });

        // 设置RecyclerView的适配器
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
