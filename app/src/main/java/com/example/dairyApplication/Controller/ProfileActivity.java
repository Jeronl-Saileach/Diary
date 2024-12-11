package com.example.dairyApplication.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dairyApplication.View.MainActivity;
import com.example.smdiary.R;

import java.util.Objects;

// 个人活动
public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView profileImage;
    //添加一个图片选择请求码以及 ImageView 的引用


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // 设置布局
        profileImage = findViewById(R.id.profileImage);

        Button returnToMain = findViewById(R.id.returnToMain); // 获取返回按钮

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


        // 处理返回按钮点击事件
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent); // 跳转回主活动
            }
        });
    }


    //重写 onActivityResult() 方法： 处理图片选择结果，将选中的图片设置到 profileImage 中显示。
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri); // 将选中的图片显示在 ImageView 中
        }
    }

}
