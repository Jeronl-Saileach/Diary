package com.example.dairyApplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;
import Database.DatabaseManager;

public class DiaryEntryActivity extends AppCompatActivity {

    private EditText diaryContent;
    private EditText diaryTitle;
    private DatabaseManager databaseManager;
    private String[] colors = {"红色", "绿色", "蓝色"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);

        diaryContent = findViewById(R.id.diaryContent);
        diaryTitle = findViewById(R.id.diaryTitle);
        Button returnToMain = findViewById(R.id.returnToMain);
        Button customizeButton = findViewById(R.id.customizeButton);
        Button saveEntryButton = findViewById(R.id.saveEntryButton);

        // 初始化数据库管理器
        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        // 返回按钮点击事件
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryEntryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 自定义按钮点击事件
        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomizationDialog();
            }
        });

        // 保存按钮点击事件
        saveEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiaryEntry();
            }
        });
    }

    // 保存日记条目到数据库
    private void saveDiaryEntry() {
        String title = diaryTitle.getText().toString().trim();
        String content = diaryContent.getText().toString().trim();

        // 非空验证
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        long date = System.currentTimeMillis();

        // 插入日记到数据库，移除 entryId 参数，让数据库自动生成
        long entryId = databaseManager.insertDiaryEntry(title, content, date, "无标签", "默认位置", 1);
        if (entryId != -1) {
            Toast.makeText(this, "日记保存成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DiaryEntryActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 自定义对话框
    private void showCustomizationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] fontSizes = {"小", "中", "大"};

        // 字体大小选择对话框
        builder.setTitle("选择字体大小")
                .setItems(fontSizes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); // 小
                                break;
                            case 1:
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // 中
                                break;
                            case 2:
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22); // 大
                                break;
                        }

                        // 字体选择后，立即显示颜色选择对话框
                        showColorDialog();
                    }
                });
        builder.create().show();
    }

    // 颜色选择对话框
    private void showColorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择文本颜色")
                .setItems(colors, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int color = Color.BLACK;
                        switch (which) {
                            case 0:
                                color = Color.RED;
                                break;
                            case 1:
                                color = Color.GREEN;
                                break;
                            case 2:
                                color = Color.BLUE;
                                break;
                        }

                        // 应用选择的颜色到选中的文本
                        applyColorToText(diaryContent.getText(), color);
                    }
                });
        builder.create().show();
    }

    // 应用选择的颜色到文本
    private void applyColorToText(Editable text, int color) {
        int start = diaryContent.getSelectionStart();
        int end = diaryContent.getSelectionEnd();
        if (start >= 0 && end > start) {
            Spannable spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            diaryContent.setText(spannable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (databaseManager != null) {
            databaseManager.close();  // 关闭数据库连接
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (databaseManager != null) {
            databaseManager.open();  // 重新打开数据库连接
        }
    }
}
