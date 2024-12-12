package com.example.dairyApplication.View;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.dairyApplication.Model.DatabaseManager;
import com.example.smdiary.R;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private DatabaseManager dbManager;
    private Button passwordProtectionButton;
    private String userId; // 保存 userId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        setContentView(R.layout.activity_settings); // 设置布局

        dbManager = new DatabaseManager(this);
        dbManager.open();

        // 从 Intent 中获取用户 ID
        userId = getIntent().getStringExtra("USER_ID"); // 确保在启动时传递用户 ID

        // 确保 userId 不为空
        if (userId == null) {
            Log.e("SettingsActivity", "User ID is null");
            Toast.makeText(this, "用户 ID 丢失", Toast.LENGTH_SHORT).show();
        }

        Button themeButton = findViewById(R.id.themeButton);
        passwordProtectionButton = findViewById(R.id.passwordProtectionButton);
        Button cloudSyncButton = findViewById(R.id.cloudSyncButton);
        Button returnToMain = findViewById(R.id.returnToMain);

        returnToMain.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent); // 跳转回主活动
        });

        themeButton.setOnClickListener(v -> selectImage());

        setupPasswordButton(); // 设置密码保护按钮点击事件

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USER_ID", userId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userId = savedInstanceState.getString("USER_ID");
        Log.d("ChangePasswordActivity", "Restored userId: " + userId);
    }

    private void setupPasswordButton() {
        passwordProtectionButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            intent.putExtra("USER_ID", userId); // 传递 userId
            startActivity(intent); // 启动 ChangePasswordActivity
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // 保存图片路径到数据库
            saveSelectedImageUri(selectedImageUri);
        }
    }

    private void saveSelectedImageUri(Uri selectedImageUri) {
        String imagePath = getRealPathFromURI(selectedImageUri);

        if (userId != null) {
            int updatedRows = dbManager.updateUserSettings(userId, "", imagePath, 0);
            if (updatedRows > 0) {
                Toast.makeText(this, "图片路径保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "保存图片路径失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "用户 ID 丢失，无法保存图片路径", Toast.LENGTH_SHORT).show();
        }

        dbManager.close(); // 确保关闭数据库连接
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        }

        return null; // 如果没有结果，返回 null
    }
}
