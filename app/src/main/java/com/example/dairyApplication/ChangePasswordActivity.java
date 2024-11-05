package com.example.dairyApplication;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password); // 创建相应的布局文件

        dbManager = new DatabaseManager(this);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String newPassword = newPasswordInput.getText().toString().trim();
        String userId = "user1"; // 假设用户ID应该是一个字符串

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        dbManager.open();
        int updatedRows = dbManager.updateUserSettings(userId, newPassword, "", 0); // 修改为使用字符串 userId
        dbManager.close();

        if (updatedRows > 0) {
            Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
            finish(); // 关闭当前活动，返回设置页面
        } else {
            Toast.makeText(this, "密码修改失败", Toast.LENGTH_SHORT).show();
        }
    }

}
