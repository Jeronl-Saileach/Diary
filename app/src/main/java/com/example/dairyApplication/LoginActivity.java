package com.example.dairyApplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

import Database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化数据库助手
        dbHelper = new DatabaseHelper(this);


        // 插入测试用户（仅在开发阶段使用，发布时可以移除）
        insertTestUser("testuser", "12345");

        // 获取UI元素
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        // 登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // 验证输入框是否为空
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                } else {
                    if (validateLogin(username, password)) {
                        // 登录成功，跳转到主界面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 登录失败，提示用户
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void insertTestUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", username);     // 假设 UserID 字段存储用户名
        values.put("Password", password);    // 存储密码

        try {
            // 插入测试用户
            long result = db.insert("UserSettings", null, values);
            if (result != -1) {
                Log.d("DBTest", "测试用户已插入: " + username);
            } else {
                Log.d("DBTest", "测试用户插入失败");
            }
        } catch (Exception e) {
            Log.e("DBTest", "插入测试用户时出错", e);
        } finally {
            db.close();
        }
    }




    // 验证登录的方法
    private boolean validateLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean isValid = false;

        Cursor cursor = null;
        try {
            String[] columns = { "Password" }; // 查询密码列
            String selection = "UserID = ?";
            String[] selectionArgs = { username };

            cursor = db.query("UserSettings", columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex("Password"));
                isValid = storedPassword.equals(password);
                Log.d("DBTest", "登录验证成功");
            } else {
                Log.d("DBTest", "用户未找到");
            }
        } catch (Exception e) {
            Log.e("DBTest", "验证用户登录时出错", e);
        } finally {
            if (cursor != null) {
                cursor.close(); // 关闭游标，释放资源
            }
            db.close(); // 关闭数据库
        }

        return isValid;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}