package com.example.dairyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;


// 主活动
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 设置布局

        Button newDiaryEntryButton = findViewById(R.id.newDiaryEntryButton); // 获取新日记条目按钮
        Button viewProfileButton = findViewById(R.id.viewProfileButton); // 获取查看个人页面按钮
        Button editSettingButton = findViewById(R.id.editSettingButton); // 获取编辑设置按钮
        Button testDatabaseButton = findViewById(R.id.testDatabaseButton);
        ListView diaryListView = findViewById(R.id.diaryListView); // 获取日记列表视图

        // 处理新日记条目按钮点击事件
        newDiaryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
                startActivity(intent); // 启动新日记条目活动
            }
        });

        // 处理查看个人页面按钮点击事件
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent); // 启动个人页面活动
            }
        });

        // 处理编辑设置按钮点击事件
        editSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent); // 启动设置活动
            }
        });

        testDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, testDatabase.class);
                startActivity(intent); // 启动数据库测试页面
            }
        });
    }
}
