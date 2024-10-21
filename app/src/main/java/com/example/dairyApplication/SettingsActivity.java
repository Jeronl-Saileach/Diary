package com.example.dairyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button themeButton = findViewById(R.id.themeButton);
        Button passwordProtectionButton = findViewById(R.id.passwordProtectionButton);
        Button cloudSyncButton = findViewById(R.id.cloudSyncButton);
        Button returnToMain = findViewById(R.id.returnToMain);

        // 处理 Return 按钮点击
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Add button listeners here if needed
    }
}
