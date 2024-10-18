package com.example.fruitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newDiaryEntryButton = findViewById(R.id.newDiaryEntryButton);
        Button viewProfileButton = findViewById(R.id.viewProfileButton);
        Button editSettingButton = findViewById(R.id.editSettingButton);
        ListView diaryListView = findViewById(R.id.diaryListView);

        // 处理 New Diary Entry 按钮点击
        newDiaryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
                startActivity(intent);
            }
        });

        // 处理 View Profile 按钮点击
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // 处理 Edit Setting 按钮点击
        editSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
