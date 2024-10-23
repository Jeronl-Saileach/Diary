package com.example.dairyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

// 登录活动
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 设置布局

        EditText username = findViewById(R.id.username); // 获取用户名输入框
        EditText password = findViewById(R.id.password); // 获取密码输入框
        Button loginButton = findViewById(R.id.loginButton); // 获取登录按钮

        // 处理登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent); // 跳转到主活动
                finish(); // 关闭当前活动
            }
        });
    }
}
