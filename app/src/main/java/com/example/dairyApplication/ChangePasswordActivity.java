package com.example.dairyApplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

import Database.DatabaseManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private DatabaseManager dbManager;
    private EditText newPasswordInput;
    private Button saveButton;
    private String userId; // 存储用户 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dbManager = new DatabaseManager(this);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        saveButton = findViewById(R.id.saveButton);

        // 获取用户 ID
        userId = getIntent().getStringExtra("USER_ID");

        Log.d("ChangePasswordActivity", "Received userId: " + userId); // 输出用户 ID

        saveButton.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String newPassword = newPasswordInput.getText().toString().trim();

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == null) {
            Log.e("ChangePasswordActivity", "Cannot change password: userId is null");
            Toast.makeText(this, "无法修改密码，用户 ID 不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        dbManager.open();
        int updatedRows = dbManager.updateUserSettings(userId, newPassword, null, 0);
        dbManager.close();

        if (updatedRows > 0) {
            Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "密码修改失败", Toast.LENGTH_SHORT).show();
        }
    }
}
