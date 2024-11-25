package com.example.dairyApplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

import java.util.Objects;

import Database.DatabaseHelper;
import Database.DatabaseManager;

public class DiaryEntryActivity extends AppCompatActivity {

    private EditText diaryContent;
    private EditText diaryTitle;
    private DatabaseManager databaseManager;
    private long entryId = -1;
    private String[] colors = {"红色", "绿色", "蓝色"};
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        // 获取Intent
        Intent intent = getIntent();

        // 从Intent中获取用户ID
        userID = intent.getStringExtra("USER_ID");

        diaryContent = findViewById(R.id.diaryContent);
        diaryTitle = findViewById(R.id.diaryTitle);
        Button returnToMain = findViewById(R.id.returnToMain);
        Button customizeButton = findViewById(R.id.customizeButton);
        Button saveEntryButton = findViewById(R.id.saveEntryButton);
        Button deleteEntryButton = findViewById(R.id.deleteEntryButton);

        // 初始化数据库管理器
        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        // 获取传递的 entryId 参数并加载内容
        entryId = getIntent().getLongExtra("entryId", -1);
        if (entryId != -1) {
            loadDiaryEntry(entryId);
        }

        // 返回按钮点击事件
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 直接返回上一个页面
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

        // 删除按钮点击事件
        deleteEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entryId != -1) {
                    new AlertDialog.Builder(DiaryEntryActivity.this)
                            .setTitle("确认删除")
                            .setMessage("确定要删除这篇日记吗？删除后将无法恢复。")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int rowsDeleted = databaseManager.deleteDiaryEntryById(entryId);
                                    if (rowsDeleted > 0) {
                                        Toast.makeText(DiaryEntryActivity.this, "日记删除成功", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK); // 设置返回结果
                                        finish(); // 关闭Activity
                                    } else {
                                        Toast.makeText(DiaryEntryActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USER_ID", userID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userID = savedInstanceState.getString("USER_ID");
    }

    // 加载指定 ID 的日记内容
    private void loadDiaryEntry(long entryId) {
        Cursor cursor = databaseManager.queryDiaryEntries("EntryId = ?", new String[]{String.valueOf(entryId)});
        if (cursor != null && cursor.moveToFirst()) {
            diaryTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)));
            diaryContent.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT)));
            cursor.close();
        }
    }

    // 保存日记条目到数据库
    private void saveDiaryEntry() {
        String title = diaryTitle.getText().toString().trim();
        String content = diaryContent.getText().toString().trim();
        Log.d(userID, "userIBefore " + userID);

        // 非空验证
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        long date = System.currentTimeMillis();

        if (entryId == -1) {
            Log.d(userID, "userIdForInsert " + userID);
            // 新建日记
            long newEntryId = databaseManager.insertDiaryEntry(title, content, date, "无标签", "默认位置", 1,userID);
            if (newEntryId != -1) {
                Toast.makeText(this, "日记保存成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // 设置返回结果
                finish();
            } else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 更新现有日记
            Log.d(userID, "userIdForUpdate " + userID);
            userID = getIntent().getStringExtra("USER_ID");
            int rowsUpdated = databaseManager.updateDiaryEntry(entryId, title, content, date, "无标签", "默认位置", 1,userID);
            if (rowsUpdated > 0) {
                Toast.makeText(this, "日记更新成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // 设置返回结果
                finish();
            } else {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }
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
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                                break;
                            case 1:
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                break;
                            case 2:
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                                break;
                        }
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
                        applyColorToText(diaryContent.getText(), color);
                    }
                });
        builder.create().show();
    }

    // 应用选择的颜色到选中的文本
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
        Log.d("Debug", "userID in onResume: " + userID);
        if (databaseManager != null) {
            databaseManager.open();  // 重新打开数据库连接
        }
    }
}