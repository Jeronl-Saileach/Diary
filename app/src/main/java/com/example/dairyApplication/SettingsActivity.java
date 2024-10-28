package com.example.dairyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

import java.util.Objects;

// 设置活动
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // 设置布局

        Button themeButton = findViewById(R.id.themeButton); // 获取主题按钮
        Button passwordProtectionButton = findViewById(R.id.passwordProtectionButton); // 获取密码保护按钮
        Button cloudSyncButton = findViewById(R.id.cloudSyncButton); // 获取云同步按钮
        Button returnToMain = findViewById(R.id.returnToMain); // 获取返回按钮

        // 处理返回按钮点击事件
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent); // 跳转回主活动
            }
        });

        // 这里可以添加按钮的点击事件来处理主题、密码保护及云同步逻辑
    }
}
