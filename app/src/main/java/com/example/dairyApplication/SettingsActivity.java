package com.example.dairyApplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import Database.DatabaseHelper;
import Database.DatabaseManager;
import com.example.smdiary.R;

import java.util.Objects;

// 设置活动
public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private DatabaseManager dbManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // 设置布局

        dbManager = new DatabaseManager(this);
        dbManager.open();

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

        // 处理主题按钮点击事件
        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
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
        String userId = "user1"; // 假设用户ID为"user1"，实际应用中应从登录信息获取
        dbManager.updateUserSettings(Long.parseLong(userId), "", imagePath, 0); // 更新主题偏好
        dbManager.close();
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}






