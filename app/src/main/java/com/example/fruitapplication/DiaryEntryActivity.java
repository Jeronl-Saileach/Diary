package com.example.fruitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

public class DiaryEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        Button returnToMain = findViewById(R.id.returnToMain);

        // 处理 Return 按钮点击
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryEntryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Implement rich text editing and multimedia insert here later
    }
}
