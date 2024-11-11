package com.example.dairyApplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

import java.util.Objects;

import Database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化数据库助手
        dbHelper = new DatabaseHelper(this);

        // 获取用户名和密码输入框及注册按钮
        EditText usernameEditText = findViewById(R.id.registerUsername);
        EditText passwordEditText = findViewById(R.id.registerPassword);
        Button registerButton = findViewById(R.id.registerButton);

        // 注册按钮点击事件
        registerButton.setOnClickListener(v -> {
            // 获取用户名和密码，并去除空格
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // 检查用户名和密码是否为空
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            } else {
                // 注册用户成功则跳转到登录界面，失败则提示
                if (registerUser(username, password)) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败，用户名可能已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button backButton = findViewById(R.id.goBackButton);
        backButton.setOnClickListener(v -> {
            //返回上一页
            finish();
        });
    }

    // 注册用户，将用户名和密码插入数据库
    private boolean registerUser(String username, String password) {
        // 获取可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", username);
        values.put("Password", password);

        try {
            // 尝试将新用户插入数据库
            long result = db.insert("UserSettings", null, values);
            return result != -1; // 插入成功返回 true
        } catch (Exception e) {
            Log.e("DBTest", "用户注册时出错", e);
            return false;
        } finally {
            db.close(); // 关闭数据库连接
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close(); // 释放数据库资源
    }
}

