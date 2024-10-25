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


// 主活动
public class MainActivity extends AppCompatActivity {

    // 在MainActivity的onCreate方法中
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 设置布局

        FloatingActionButton addDiaryEntryButton = findViewById(R.id.addDiaryEntryButton);
        ImageButton viewProfileButton = findViewById(R.id.viewProfileButton);
        ImageButton editSettingButton = findViewById(R.id.editSettingButton);

        // 处理添加日记条目按钮点击事件
        addDiaryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
                startActivity(intent); // 启动新日记条目活动
            }
        });

        // 处理个人页面按钮点击事件
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent); // 启动个人页面活动
            }
        });

        // 处理设置按钮点击事件
        editSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent); // 启动设置页面
            }
        });
    }


}
