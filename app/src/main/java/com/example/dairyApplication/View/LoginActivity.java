package com.example.dairyApplication.View;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

import com.example.dairyApplication.Model.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            } else {
                if (validateLogin(username, password)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", username); // 传递用户 ID
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button registerPageButton = findViewById(R.id.registerButton);
        registerPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean isValid = false;
        Cursor cursor = null;

        try {
            String[] columns = {"Password"};
            String selection = "UserID = ?";
            String[] selectionArgs = {username};

            cursor = db.query("UserSettings", columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow("Password"); // 获取 Password 列的索引
                String storedPassword = cursor.getString(columnIndex);
                isValid = storedPassword.equals(password);
            } else {
                Log.e("DBTest", "User not found or cursor is null");
            }
        } catch (Exception e) {
            Log.e("DBTest", "验证用户登录时出错", e);
        } finally {
            if (cursor != null) {
                cursor.close(); // 关闭 cursor
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
